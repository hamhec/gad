package fr.lirmm.graphik.gad.api.core;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.util.stream.CloseableIterator;

public class RuleApplicationTuple {
	private Rule rule;
	private Substitution substitution;
	
	private AtomSet from;
	private AtomSet to;
	
	/* --------------------------------
	 * Constructors
	 * -------------------------------- */
	
	public RuleApplicationTuple(AtomSet from, AtomSet to, Rule rule, Substitution substitution) {
		this.rule = rule;
		this.substitution = substitution;
		this.from = from;
		this.to = to;
	}
	
	/* --------------------------------
	 * Public Methods
	 * -------------------------------- */
	public Rule getRule() {
		return this.rule;
	}
	public Substitution getSubstitution() {
		return this.substitution;
	}
	public AtomSet getSources(){
		return this.from;
	}
	public AtomSet getResultingAtoms() {
		return this.to;
	}
	
	public String toString() {
		return rule.toString();
	}
}
