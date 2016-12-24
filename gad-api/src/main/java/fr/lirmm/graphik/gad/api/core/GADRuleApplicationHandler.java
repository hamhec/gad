package fr.lirmm.graphik.gad.api.core;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseHaltingCondition;
import fr.lirmm.graphik.graal.api.forward_chaining.RuleApplicationHandler;
import fr.lirmm.graphik.graal.api.forward_chaining.RuleApplier;
import fr.lirmm.graphik.graal.core.factory.AtomSetFactory;
import fr.lirmm.graphik.graal.forward_chaining.halting_condition.ChaseStopConditionWithHandler;
import fr.lirmm.graphik.graal.forward_chaining.halting_condition.RestrictedChaseStopCondition;
import fr.lirmm.graphik.graal.forward_chaining.rule_applier.ExhaustiveRuleApplier;
import fr.lirmm.graphik.graal.homomorphism.StaticHomomorphism;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public class GADRuleApplicationHandler implements RuleApplicationHandler{
	
	private GraphOfAtomDependency graph;
	
	public GADRuleApplicationHandler(GraphOfAtomDependency graph) {
		super();
		this.graph = graph;
	}
	
	public boolean preRuleApplication(Rule rule, Substitution substitution,
			AtomSet data) {
		return true;
	}

	public CloseableIterator<Atom> postRuleApplication(Rule rule,
			Substitution substitution, AtomSet data, CloseableIterator<Atom> atomsToAdd) {
		
		AtomSet newAtoms = AtomSetFactory.instance().create();
		try {
			while(atomsToAdd.hasNext()) {
				newAtoms.add(atomsToAdd.next());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		RuleApplicationTuple ruleApplication = new RuleApplicationTuple(data, newAtoms, rule, substitution);
		
		graph.addEdges(ruleApplication); 
		
		return newAtoms.iterator();
	}
	
	
	public RuleApplier<Rule, AtomSet> getRuleApplier() {
		return getRuleApplier(new RestrictedChaseStopCondition());
	}
	
	public RuleApplier<Rule, AtomSet> getRuleApplier(ChaseHaltingCondition chaseCondition) {
		ChaseStopConditionWithHandler chaseConditionHandler = new ChaseStopConditionWithHandler(chaseCondition, this);
		RuleApplier<Rule, AtomSet> ruleApplier = new ExhaustiveRuleApplier<AtomSet>(StaticHomomorphism.instance(), chaseConditionHandler); 
		
		return ruleApplier;
	}
	
}
