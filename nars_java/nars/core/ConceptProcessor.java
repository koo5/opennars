package nars.core;

import java.util.Collection;
import nars.entity.BudgetValue;
import nars.entity.Concept;
import nars.language.Term;


/** A ConceptProcessor implements a model for storing Concepts and
 *  activating them during a memory cycle.  In essence it forms the very core of the memory,
 *  responsible for efficiently storing all NAR Concept's and which of those will activate
 *  at the beginning of each cycle.*/
public interface ConceptProcessor {

    /** An iteration of the main loop, called during each memory cycle. */
    public void cycle(Memory m);

    /** All known concepts */
    public Collection<? extends Concept> getConcepts();

    /** Invoked during a memory reset to empty all concepts */
    public void clear();

    /** Maps a concept name (a subclass of CharSequence) to a Concept */
    public Concept concept(CharSequence name);

    /**
     * Creates and adds new concept to the memory
     * @return the new concept, or null if the memory is full
     */
    public Concept addConcept(Term term, Memory memory);

    /** Activates a concept, adjusting its budget.  
     *  May be invoked by the concept processor or at certain points in the reasoning process.
     */
    public void conceptActivate(Concept c, BudgetValue b);

    /**
     * Provides a "next" concept for sampling during inference. 
     */
    public Concept sampleNextConcept();
    
}