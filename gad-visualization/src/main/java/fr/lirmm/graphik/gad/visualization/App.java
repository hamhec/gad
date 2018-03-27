package fr.lirmm.graphik.gad.visualization;

import fr.lirmm.graphik.gad.api.core.GADEdge;
import fr.lirmm.graphik.gad.api.core.GADRuleApplicationHandler;
import fr.lirmm.graphik.gad.api.core.GraphOfAtomDependency;
import fr.lirmm.graphik.gad.api.test.GADGenerator;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.Chase;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.io.ParseException;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomSetFactory;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.forward_chaining.SccChase;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, AtomSetException, IteratorException, ChaseException
    {
        System.out.println( "Hello World!" );
        
        /*System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        
        Graph graph = new SingleGraph("Tutorial 1");
        
        graph.addAttribute("ui.stylesheet", "url('file:///home/abidech/workspace/java/gad/gad-visualization/src/main/java/fr/lirmm/graphik/gad/visualization/styles.css')");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        
        graph.display();
        
		Node p0 = graph.addNode("p0(a)"); p0.addAttribute("isAtom"); p0.addAttribute("isFact");
		Node p1 = graph.addNode("p1(a)"); p1.addAttribute("isAtom"); p1.addAttribute("isFact");
		Node p2_0 = graph.addNode("p2_0(a)"); p2_0.addAttribute("isAtom"); p2_0.addAttribute("isFact");
		Node p2_1 = graph.addNode("p2_1(a)"); p2_1.addAttribute("isAtom"); p2_1.addAttribute("isFact");
		graph.addNode("q0(a)").addAttribute("isAtom");
		graph.addNode("q1_0(a)").addAttribute("isAtom");
		graph.addNode("q1_1(a)").addAttribute("isAtom");
		graph.addNode("q2(a)").addAttribute("isAtom");
		graph.addNode("r0(a)").addAttribute("isAtom");
		graph.addNode("r2(a)").addAttribute("isAtom");
		
		
		graph.addNode("q0(X) :- p0(X)");
		graph.addNode("r0(X) :- q0(X)");
		graph.addNode("q2(X) :- p0(X)");
		
		graph.addNode("q1_0(X), q1_1(X) :- p1(X)");
		
		graph.addNode("q2(X) :- p2_0(X), p2_1(X)");
		graph.addNode("r2(X) :- q2(X)");
		
		
		for (Node node : graph) {
			if(node.hasAttribute("isAtom")) {
		        node.addAttribute("ui.label", node.getId());
		        node.addAttribute("ui.class", "atom");
			} else { // An Edge
				node.addAttribute("ui.label", node.getId());
				node.addAttribute("ui.class", "ruleApplication");
				//node.addAttribute("layout.weight", 5.0f);
			}
			
			if(node.hasAttribute("isFact")) {
				node.addAttribute("ui.class", "fact");
			}
	    }
		
		graph.addEdge("p0q0rule", "p0(a)", "q0(X) :- p0(X)");
		graph.addEdge("p0q0", "q0(X) :- p0(X)", "q0(a)", true);
		
		
		graph.addEdge("q0r0rule", "q0(a)", "r0(X) :- q0(X)");
		graph.addEdge("q0r0", "r0(X) :- q0(X)", "r0(a)", true);
		
		graph.addEdge("p0r2rule", "p0(a)", "q2(X) :- p0(X)");
		graph.addEdge("p0r2", "q2(X) :- p0(X)", "q2(a)", true);
		
		
		graph.addEdge("p1q1_0rule", "p1(a)", "q1_0(X), q1_1(X) :- p1(X)");
		graph.addEdge("p1q1_0", "q1_0(X), q1_1(X) :- p1(X)", "q1_0(a)", true);
		graph.addEdge("p1q1_1", "q1_0(X), q1_1(X) :- p1(X)", "q1_1(a)", true);
		
		graph.addEdge("p2_0q2rule", "p2_0(a)", "q2(X) :- p2_0(X), p2_1(X)");
		graph.addEdge("p1_0q2rule", "p2_1(a)", "q2(X) :- p2_0(X), p2_1(X)");		
		graph.addEdge("p2_0q2", "q2(X) :- p2_0(X), p2_1(X)", "q2(a)", true);
		
		graph.addEdge("q2r2rule", "q2(a)", "r2(X) :- q2(X)");
		graph.addEdge("q2r2", "r2(X) :- q2(X)", "r2(a)", true);
		
		
		for(Edge edge : graph.getEachEdge()) {
			edge.addAttribute("layout.weight", 1.5f);
		}*/
        
//        GraphOfAtomDependency gad = new GraphOfAtomDependency();

		/*
		 * r0(a) <-- [q0(a)] <-- [p0(a)]
		 * 			q1_0(a)  <-- [p1(a)]
		 * 			q1_1(a)  <-- [p1(a)]
		 * r2(a) <-- [q2(a)] <-- [p2_0(a), p2_1(a)] | [p0(a)]
		 */
		
		// generate1Source1Target
//		Atom source1 = DlgpParser.parseAtom("p0(a).");
//		gad.addEdge(new GADEdge(null, source1));
//		GADGenerator.generate1Source1Target(gad, source1, "q0");
//			
//		Atom q0 = DlgpParser.parseAtom("q0(a).");
//		GADGenerator.generate1Source1Target(gad, q0, "r0");
//		
//		
//		// generate1Source2Target
//		Atom source2 = DlgpParser.parseAtom("p1(a).");
//		gad.addEdge(new GADEdge(null, source2));
//		GADGenerator.generate1Source2Target(gad, source2, "q1_0", "q1_1");
//		
//		// generate2Source1Target
//		Atom source3 = DlgpParser.parseAtom("p2_0(a).");
//		Atom source4 = DlgpParser.parseAtom("p2_1(a).");
//		gad.addEdge(new GADEdge(null, source3));
//		gad.addEdge(new GADEdge(null, source4));
//		GADGenerator.generate2Source1Target(gad, source3, source4, "q2");
//		
//		// generate chain
//		Atom q2 = DlgpParser.parseAtom("q2(a).");
//		GADGenerator.generate1Source1Target(gad, q2, "r2");
//		
//		// generate simple fact
//		Atom simpleAtom = DlgpParser.parseAtom("o(a).");
//		gad.addEdge(new GADEdge(null, simpleAtom));
//		
//		// generate a second path for q2
//		GADGenerator.generate1Source1Target(gad, source1, "q2");
//		
//		System.out.println(gad.toString());
		
        
        GraphOfAtomDependency gad = new GraphOfAtomDependency();
        // generate Facts
 		CloseableIterator<Atom> itFacts = DlgpParser.parseAtomSet("a(a), b(a), c(a), e(a), f(a), g(a), h(a), i(a), j(a).");
 		AtomSet facts = DefaultAtomSetFactory.instance().create();
 		while(itFacts.hasNext()) {
 			facts.add(itFacts.next());
 		}
 		
 		// generate Rules
 		RuleSet rules = new LinkedListRuleSet();
 		rules.add(DlgpParser.parseRule("[r1] k(X) :- f(X), h(X)."));
 		rules.add(DlgpParser.parseRule("[r2] l(X) :- i(X), j(X)."));
 		
 		gad.initialise(facts);
 		
 		Chase chase = new SccChase<AtomSet>(rules.iterator(), facts,
 				new GADRuleApplicationHandler(gad).getRuleApplier());
 		chase.execute();
 		
 		System.out.println("Begin Tests");
 		System.out.println(gad.toString());
     		
		GADVisualizer gadVis = new GADVisualizer(gad);
		gadVis.display();
    }
}
