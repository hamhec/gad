package fr.lirmm.graphik.gad.api.test;

import fr.lirmm.graphik.gad.api.core.GADEdge;
import fr.lirmm.graphik.gad.api.core.GraphOfAtomDependency;
import fr.lirmm.graphik.gad.api.core.RuleApplicationTuple;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.io.ParseException;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomSetFactory;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;

public final class GADGenerator {

	private GADGenerator() {}
	
	public static void generate1Source1Target(GraphOfAtomDependency gad, Atom source, String targetPredicate) throws AtomSetException, ParseException {
		
		AtomSet from = DefaultAtomSetFactory.instance().create();
		AtomSet to = DefaultAtomSetFactory.instance().create();
		from.add(source);
		Atom target = DlgpParser.parseAtom(targetPredicate + "(a).");
		to.add(target);
		Rule rule = DlgpParser.parseRule(targetPredicate + "(X) :- " + source.getPredicate().getIdentifier() + "(X).");
		gad.addEdge(new GADEdge(new RuleApplicationTuple(from, to, rule, null), target));
	}
	
	public static void generate1Source2Target(GraphOfAtomDependency gad, Atom source,
			String targetPredicate1, String targetPredicate2) throws AtomSetException, ParseException {
		AtomSet from = DefaultAtomSetFactory.instance().create();
		AtomSet to = DefaultAtomSetFactory.instance().create();
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
	
	public static void generate2Source1Target(GraphOfAtomDependency gad, Atom source1, Atom source2, String targetPredicate) throws AtomSetException, ParseException {
		AtomSet from = DefaultAtomSetFactory.instance().create();
		AtomSet to = DefaultAtomSetFactory.instance().create();
		from.add(source1);
		from.add(source2);
		Atom target = DlgpParser.parseAtom(targetPredicate + "(a).");
		to.add(target);
		
		gad.addEdge(new GADEdge(new RuleApplicationTuple(from, to, 
				DlgpParser.parseRule(targetPredicate + "(X) :- " + source1.getPredicate().getIdentifier() + "(X),"+ source2.getPredicate().getIdentifier() +"(X)."), null), target));
	}
	
	
}
