# The current status and the plan for the near future of the project.

# Current situation

The main Open-NARS project (under http://code.google.com/p/open-nars/source/browse/#svn/trunk/nars_core_java and http://code.google.com/p/open-nars/source/browse/#svn/trunk/nars_gui) has been updated to version 1.5.5 in July 2013. This version implementes NAL-1 to NAL-6 as described in [Non-Axiomatic Logic: A Model of Intelligent Reasoning](http://www.worldscientific.com/worldscibooks/10.1142/8665). This version is a major refactoring of the previous version, and it also realizes some ideas suggested by Joe Geldart, Jeff Thompson, and Jean-Marc Vanel.

There are several ongoing activities:
  *. **Testing the current design.** Various domains have been chosen to test the expressive power of the grammar rules and the inferential powers of the inference rules, as currently designed and implemented. Some of the results are summrized in the [reports](http://www.cis.temple.edu/~pwang/demos.html) of the testers. Active experiments include forensic reasoning, improvisational decision-making, reasoning on semantic web, and natural language processing.
  *. **Top-layers implementation.** NAL-7, NAL-8, and NAL-9 will be re-implemented in version 1.5, probably in a form that is different from how they were implemented in versions 1.3 and 1.4. In particular, the implementation of NAL-9 may require further refactoring of the existing code. 
  *. **Control mechanism.** The resource allocation strategies of NARS needs a refined conceptual design, then the related code will be modified. In particular, the *automatic* activities managed by the control mechanism will coordinate with the *voluntary* activities introduced by NAL-9.

# Future tasks

After the above tasks are accomplished, there are several further possibilities to be explored. 
  **. Implementing the inference rules in a declarative manner, similar to [the Prolog version of NAL](http://www.cis.temple.edu/~pwang/Implementation/NAL/nal.pl). The current approach in the Java version, mainly shown in nars.inference.RuleTables, lacks clarity and elegance, and is also hard to maintain. 
  **. Interfacing with other systems via various types of input/output channels, which is about how NARS can use other systems. It is also related to the ideas presented in AbstractDesignProposal, which is about how other systems can use NARS. 
  *. System refactoring, which is related to the above topics, as well as those discussed in Modules.