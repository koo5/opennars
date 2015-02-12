package nars.logic.reason.concept;


import nars.io.Symbols;
import nars.logic.BudgetFunctions;
import nars.logic.Terms;
import nars.logic.TruthFunctions;
import nars.logic.entity.*;
import nars.logic.nal5.Conjunction;
import nars.logic.nal5.Implication;
import nars.logic.reason.ConceptFire;

import java.util.HashSet;
import java.util.Set;

import static nars.logic.nal7.TemporalRules.ORDER_CONCURRENT;
import static nars.logic.nal7.TemporalRules.ORDER_FORWARD;

/*
 if the premise task is a =/>
    <(&/,<a --> b>,<b --> c>,<x --> y>,pick(a)) =/> <goal --> reached>>.
    (&/,<a --> b>,<b --> c>,<x --> y>). :|:
    |-
    <pick(a) =/> <goal --> reached>>. :|:
*/
public class ForwardImplicationProceed extends ConceptFireTaskTerm {

    /**
     * //new inference rule accelerating decision making: https://groups.google.com/forum/#!topic/open-nars/B8veE-WDd8Q
     */
    public static int PERCEPTION_DECISION_ACCEL_SAMPLES = 1;

    @Override
    public boolean apply(ConceptFire f, TaskLink taskLink, TermLink termLink) {
        if (!f.nal(7)) return true;

        Concept concept = f.getCurrentBeliefConcept();
        if (concept == null) return true;

        Task task = taskLink.getTask();

        if (!(task.sentence.isJudgment() || task.sentence.isGoal())) return true;

        Term taskTerm = task.sentence.term;
        if (!(taskTerm instanceof Implication)) return true;
        Implication imp = (Implication) task.sentence.term;

        if (!((imp.getTemporalOrder() == ORDER_FORWARD || (imp.getTemporalOrder() == ORDER_CONCURRENT))))
            return true;


        if (!(imp.getSubject() instanceof Conjunction)) return true;
        Conjunction conj = (Conjunction) imp.getSubject();


        //the conjunction must be of size > 2 in order for a smaller one to match its beginning subsequence
        if (conj.size() <= 2)
            return true;

        if (!((conj.getTemporalOrder() == ORDER_FORWARD) || (conj.getTemporalOrder() == ORDER_CONCURRENT)))
            return true;

        Set<Term> alreadyInducted = new HashSet();

        for (int i = 0; i < PERCEPTION_DECISION_ACCEL_SAMPLES; i++) {

            //prevent duplicate derivations
            alreadyInducted.clear();

            Concept next = f.memory.concepts.sampleNextConcept();

            if ((next == null) || (next.equals(concept))) continue;

            Term t = next.getTerm();

            if (alreadyInducted.add(t) && (t instanceof Conjunction)) {

                Sentence s = null;
                if (task.sentence.punctuation == Symbols.JUDGMENT) {
                    s = next.getBestBelief(true, false);
                }
                else if (task.sentence.punctuation == Symbols.GOAL) {
                    s = next.getBestGoal(true, false);
                }
                if (s == null) continue;

                Conjunction conj2 = (Conjunction) t;

                //ok check if it is a right conjunction
                if (conj.getTemporalOrder() == conj2.getTemporalOrder()) {

                    //conj2 conjunction has to be a minor of conj
                    //the case where its equal is already handled by other inference rule
                    if (conj2.term.length < conj.term.length) {

                        boolean equal = true;

                        //ok now check if it is really a minor (subsequence)
                        for (int j = 0; j < conj2.term.length; j++) {
                            if (!conj.term[j].equals(conj2.term[j])) {
                                equal = false;
                            }
                        }
                        if (!equal) continue;

                        //ok its a minor, we have to construct the residue implication now
                        Term[] residue = new Term[conj.term.length - conj2.term.length];
                        for (int k = 0; k < residue.length; k++) {
                            residue[k] = conj.term[conj2.term.length + k];
                        }

                        Term C = Conjunction.make(residue, conj.getTemporalOrder());
                        Implication resImp = Implication.make(C, imp.getPredicate(), imp.getTemporalOrder());

                        //todo add
                        TruthValue truth = TruthFunctions.deduction(s.truth, task.sentence.truth);

                        Stamp st = new Stamp(task.sentence.stamp, f.memory.time());
                        st.getChain().add(t);

                        Sentence newSentence = new Sentence(resImp, s.punctuation, truth, st);

                        Task newTask = new Task(newSentence, new BudgetValue(BudgetFunctions.forward(truth, f)));
                        f.deriveTask(newTask, false, false, task, null);

                    }
                }
            }
        }


        return true;
    }
}