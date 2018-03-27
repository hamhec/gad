package fr.lirmm.graphik.gad.api.path;

import fr.lirmm.graphik.gad.api.core.GADEdge;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.core.atomset.graph.DefaultInMemoryGraphStore;

public class CompactGADPath {
	private GADEdge target;
	private AtomSet branchingAtoms;
	private GADPath nonBranchingPath;
	private boolean isDefeasible;
	
	public CompactGADPath(GADEdge edge) {
		this.target = edge;
		this.branchingAtoms = new DefaultInMemoryGraphStore();
		this.nonBranchingPath = new GADPath();
		this.nonBranchingPath.add(edge);
		this.isDefeasible = false;
	}
	
	public void addBranchingAtom(Atom a) throws AtomSetException {
		this.branchingAtoms.add(a);
	}
	
	public void addNonBranchingAtom(GADEdge edge) {
		this.nonBranchingPath.add(edge);
	}
	
	public AtomSet getBranchingAtoms() {
		return this.branchingAtoms;
	}
	
	public GADPath getNonBranchingPath() {
		return this.nonBranchingPath;
	}
	
	public GADEdge getTarget() {
		return this.target;
	}
	
	public Atom getTargetAtom() {
		return this.target.getTarget();
	}
	
	public String toString() {
		String str = "Branching: ";
	/*	for(Atom a : this.branchingAtoms) {
			str += " " + a.toString() + " ";
		}
		str += ", NonBrancing ";
		for(Atom a : this.nonBranchingAtoms) {
			str += " " + a.toString() + " ";
		} */
		str += " ; defeasible: " + this.isDefeasible;
		return str;
	}
	
	public void setIsDefeasible(boolean bool) {
		this.isDefeasible = bool;
	}
	
	public boolean isDefeasible() {
		return this.isDefeasible;
	}
}
