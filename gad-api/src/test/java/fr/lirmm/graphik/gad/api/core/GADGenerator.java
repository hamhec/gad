package fr.lirmm.graphik.gad.api.core;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.core.factory.AtomSetFactory;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;

public final class GADGenerator {
	public static void main(String args[]) throws AtomSetException {
		GraphOfAtomDependency gad = new GraphOfAtomDependency();
		
		// Test generate1Source1Target
		Atom source1 = DlgpParser.parseAtom("p0(a).");
		gad.addEdge(new GADEdge(null, source1));
		GADGenerator.generate1Source1Target(gad, source1, "q0");
		
		// Test generate1Source2Target
		Atom source2 = DlgpParser.parseAtom("p1(a).");
		gad.addEdge(new GADEdge(null, source2));
		GADGenerator.generate1Source2Target(gad, source2, "q1_0", "q1_1");
		
		// Test generate1Source2Target
		Atom source3 = DlgpParser.parseAtom("p2_0(a).");
		Atom source4 = DlgpParser.parseAtom("p2_1(a).");
		gad.addEdge(new GADEdge(null, source3));
		gad.addEdge(new GADEdge(null, source4));
		GADGenerator.generate2Source1Target(gad, source3, source4, "q2");
		
		// Test combining paths
		Atom q2 = DlgpParser.parseAtom("q2(a).");
		GADGenerator.generate1Source1Target(gad, q2, "r");
		System.out.println(gad.toString());
	}
	
	private GADGenerator() {}
	
	static void generate1Source1Target(GraphOfAtomDependency gad, Atom source, String targetPredicate) throws AtomSetException {
		
		AtomSet from = AtomSetFactory.instance().create();
		AtomSet to = AtomSetFactory.instance().create();
		from.add(source);
		Atom target = DlgpParser.parseAtom(targetPredicate + "(a).");
		to.add(target);
		Rule rule = DlgpParser.parseRule(targetPredicate + "(X) :- " + source.getPredicate().getIdentifier() + "(X).");
		gad.addEdge(new GADEdge(new RuleApplicationTuple(from, to, rule, null), target));
	}
	
	static void generate1Source2Target(GraphOfAtomDependency gad, Atom source,
			String targetPredicate1, String targetPredicate2) throws AtomSetException {
		AtomSet from = AtomSetFactory.instance().create();
		AtomSet to = AtomSetFactory.instance().create();
		from.add(source);
		Atom target1 = DlgpParser.parseAtom(targetPredicate1 + "(a).");
		to.add(target1);
		Atom target2 = DlgpParser.parseAtom(targetPredicate2 + "(a).");
		to.add(target2);
		
		RuleApplicationTuple  ruleApplication = new RuleApplicationTuple(from, to,
				DlgpParser.parseRule(targetPredicate1 + "(X), "+ targetPredicate2 +"(X) :- " + source.getPredicate().getIdentifier() + "(X)."), null);
		gad.addEdge(new GADEdge(ruleApplication, target1));
		gad.addEdge(new GADEdge(ruleApplication, target2));
	}
	
	static void generate2Source1Target(GraphOfAtomDependency gad, Atom source1, Atom source2, String targetPredicate) throws AtomSetException {
		AtomSet from = AtomSetFactory.instance().create();
		AtomSet to = AtomSetFactory.instance().create();
		from.add(source1);
		from.add(source2);
		Atom target = DlgpParser.parseAtom(targetPredicate + "(a).");
		to.add(target);
		
		gad.addEdge(new GADEdge(new RuleApplicationTuple(from, to, 
				DlgpParser.parseRule(targetPredicate + "(X) :- " + source1.getPredicate().getIdentifier() + "(X),"+ source2.getPredicate().getIdentifier() +"(X)."), null), target));
	}
	
	
}
