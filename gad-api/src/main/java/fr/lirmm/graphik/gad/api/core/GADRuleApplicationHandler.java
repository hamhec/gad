package fr.lirmm.graphik.gad.api.core;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseHaltingCondition;
import fr.lirmm.graphik.graal.api.forward_chaining.RuleApplicationHandler;
import fr.lirmm.graphik.graal.api.forward_chaining.RuleApplier;
import fr.lirmm.graphik.graal.forward_chaining.halting_condition.FrontierRestrictedChaseHaltingCondition;
import fr.lirmm.graphik.graal.forward_chaining.halting_condition.HaltingConditionWithHandler;
import fr.lirmm.graphik.graal.forward_chaining.rule_applier.ExhaustiveRuleApplier;
import fr.lirmm.graphik.graal.homomorphism.SmartHomomorphism;
import fr.lirmm.graphik.util.stream.CloseableIterator;

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
		
		AtomSet newAtoms = substitution.createImageOf(rule.getHead());
		AtomSet sources = substitution.createImageOf(rule.getBody());
		
		RuleApplicationTuple ruleApplication = new RuleApplicationTuple(sources, newAtoms, rule, substitution);
		
		graph.addEdges(ruleApplication); 
		
		return atomsToAdd;
	}
	
	
//	public RuleApplier<Rule, AtomSet> getRuleApplier() {
//		return getRuleApplier(new RestrictedChaseStopCondition());
//	}
//	
//	public RuleApplier<Rule, AtomSet> getRuleApplier(ChaseHaltingCondition chaseCondition) {
//		ChaseStopConditionWithHandler chaseConditionHandler = new ChaseStopConditionWithHandler(chaseCondition, this);
//		RuleApplier<Rule, AtomSet> ruleApplier = new ExhaustiveRuleApplier<AtomSet>(StaticHomomorphism.instance(), chaseConditionHandler); 
//		
//		return ruleApplier;
//	}
	
	public RuleApplier<Rule, AtomSet> getRuleApplier() {
		return getRuleApplier(new FrontierRestrictedChaseHaltingCondition());
	}
	
	public RuleApplier<Rule, AtomSet> getRuleApplier(ChaseHaltingCondition chaseCondition) {
		HaltingConditionWithHandler chaseConditionHandler = new HaltingConditionWithHandler(chaseCondition, this);
		RuleApplier<Rule, AtomSet> ruleApplier = new ExhaustiveRuleApplier<AtomSet>(SmartHomomorphism.instance(), chaseConditionHandler); 
		
		return ruleApplier;
	}
	
}
