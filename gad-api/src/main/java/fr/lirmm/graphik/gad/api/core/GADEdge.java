package fr.lirmm.graphik.gad.api.core;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public class GADEdge {
	private RuleApplicationTuple ruleApplicationTuple;
	private Atom target;
	
	/* --------------------------------
	 * Constructors
	 * -------------------------------- */
	public GADEdge(RuleApplicationTuple ruleApplication, Atom target) {
		this.ruleApplicationTuple = ruleApplication;
		this.target = target;
	}
	
	/* --------------------------------
	 * Public Methods
	 * -------------------------------- */
	public Atom getTarget() {
		return this.target;
	}
	public AtomSet getSources() {
		if(this.ruleApplicationTuple == null) return null;
		return this.ruleApplicationTuple.getSources();
	}
	public Rule getRule() {
		return this.ruleApplicationTuple.getRule();
	}
	public Substitution getSubstitution() {
		return this.ruleApplicationTuple.getSubstitution();
	}
	
	public RuleApplicationTuple getRuleApplicationTuple() {
		return this.ruleApplicationTuple;
	}
	
	public boolean isFact() {
		return (this.getRuleApplicationTuple() == null);
	}
	
	public boolean equals(GADEdge edge) {
		boolean equal = true;
		if(this.getRuleApplicationTuple() != edge.getRuleApplicationTuple()) equal = false;
		if(this.getTarget() != edge.getTarget()) equal = false;
		return equal;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.getTarget().getPredicate().getIdentifier());
		s.append(this.getTarget().getTerms());
		
		s.append(" <--");
		
		if(null != this.getSources()) {
			s.append(this.getRule().getLabel());
			s.append("-- ");
			s.append("(");
			CloseableIterator<Atom> it = this.getSources().iterator();
			try {
				if(it.hasNext()) {
					Atom a = it.next();
					s.append(a.getPredicate().getIdentifier());
					s.append(a.getTerms());
				}
			
			
				while(it.hasNext()) {
					s.append(" , ");
					Atom a = it.next();
					s.append(a.getPredicate().getIdentifier());
					s.append(a.getTerms());
				}
			} catch (IteratorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			s.append("( ");
		}
		s.append(")");
		return s.toString();
	}
}
