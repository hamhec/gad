package fr.lirmm.graphik.gad.api.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.lirmm.graphik.gad.api.path.CompactGADPath;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public class GraphOfAtomDependency {
	private HashMap<String, LinkedList<GADEdge>> map;
	private HashSet<String> leaves;
	
	/* --------------------------------
	 * Constructors
	 * -------------------------------- */

	public GraphOfAtomDependency() {
		this.map = new HashMap<String, LinkedList<GADEdge>>();
		this.leaves = new HashSet<String>();
	}

	public GraphOfAtomDependency(AtomSet atomset) throws IteratorException {
		this();
		this.initialise(atomset);
	}
	
	/* --------------------------------
	 * Public Methods
	 * -------------------------------- */
	public void initialise(AtomSet atomset) throws IteratorException {
		this.map.clear();
		CloseableIterator<Atom> it = atomset.iterator();
		while (it.hasNext()) {
			Atom atom = it.next();
			this.addEdge(null, atom);
		}
	}
	
	public void addLeaf(String atom) {
		this.leaves.add(atom);
	}
	public void updateLeaves(GADEdge edge) throws IteratorException {
		String targetString = edge.getTarget().toString();
		LinkedList<GADEdge> edges = this.map.get(targetString);
		if(null == edges) { // the target has never been tracked before
			this.addLeaf(targetString);
		}
		if(edge.getSources() != null) { // target is not a fact
			CloseableIterator<Atom> itSources = edge.getSources().iterator();
			while(itSources.hasNext()) {
				this.removeLeaf(itSources.next().toString());
			}
		}
	}
	
	public void removeLeaf(String atom) {
		this.leaves.remove(atom);
	}
	public Iterator<String> getLeaves() {
		return this.leaves.iterator();
	}
	
	
	public void addEdge(GADEdge edge) {
		try {
			this.updateLeaves(edge);
		} catch (IteratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String targetString = edge.getTarget().toString();
		
		LinkedList<GADEdge> edges = this.map.get(targetString);
		if (null == edges) { // The target atom has never been tracked before
			edges = new LinkedList<GADEdge>();
			edges.add(edge);
			this.map.put(targetString, edges);
			
			
		} else { // The target atom has been 'seen' before
			boolean exists = false;
			for(GADEdge e : edges) {
				if(edge.equals(e)) {
					exists = true;
					break;
				}
			}
			// TODO: Check for cycles
			if(!exists) {
				if(!edge.isFact()) // if it is for a fact, then do not add it as it created a cycle
					edges.add(edge);
			}
		}
	}
	
	public void addEdge(RuleApplicationTuple ruleApplication, Atom target) {
		GADEdge edge = new GADEdge(ruleApplication, target);
		this.addEdge(edge);
	}
	
	public void addEdges(RuleApplicationTuple ruleApplication) {
		CloseableIterator<Atom> atomIt = ruleApplication.getResultingAtoms().iterator();
		try {
			while(atomIt.hasNext()) {
				GADEdge edge = new GADEdge(ruleApplication, atomIt.next());
				this.addEdge(edge);
			}
		} catch (IteratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LinkedList<GADEdge> getInEdges(String atomString) {
		return this.map.get(atomString);
	}
	
	// TODO: getVertices does not rely on the atom.toString(), might be problematic.
	public Set<Atom> getVertices() {
		Set<Atom> set = new HashSet<Atom>();
		for(LinkedList<GADEdge> list : this.map.values()) {
			set.add(list.getFirst().getTarget());
		}
		return set;
	}
	
	public boolean isFact(String atomString) { // Checks whether the atom is a starting fact (not derived)
		List<GADEdge> edges = getInEdges(atomString);
		boolean isFact = false;
		for (GADEdge edge : edges) {
			if(edge.isFact()) {
				isFact = true;
				break;
			}
		}
		return isFact;
	}
	public Atom getAtom(String atomString) {
		LinkedList<GADEdge> edges = this.getInEdges(atomString);
		if(null == edges) return null;
		else return edges.getFirst().getTarget();
	}
	
	public List<CompactGADPath> getCompactPathsFor(String atomString) throws IteratorException, AtomSetException {
		Atom atom = this.getAtom(atomString);
		if(null == atom) return null;
		return this.getCompactPathsFor(atom);
	}
	public List<CompactGADPath> getCompactPathsFor(Atom atom) throws IteratorException, AtomSetException {
		List<CompactGADPath> paths = new LinkedList<CompactGADPath>();
		String atomString = atom.toString();
		Iterator<GADEdge> itInEdges = this.getInEdges(atomString).iterator();
		
		if(this.isFact(atomString)) { // No path for a Fact
			GADEdge edge = itInEdges.next();
			CompactGADPath path = new CompactGADPath(edge);
			paths.add(path);
			return paths;
		}
		
		
		while(itInEdges.hasNext()) { // A path for the atom
			GADEdge edge = itInEdges.next();
			CompactGADPath path = new CompactGADPath(edge);
			
			CloseableIterator<Atom> itSources = edge.getSources().iterator();
			while(itSources.hasNext()) {
				Atom source = itSources.next();
				this.followPath(source, path);
			}
			paths.add(path);
		}
		return paths;
	}
	
	// follows a path untill it reaches facts or branching atoms
	private void followPath(Atom atom, CompactGADPath path) throws AtomSetException, IteratorException {
		LinkedList<GADEdge> edges = this.getInEdges(atom.toString());	
		if(edges.size() > 1) { // a branching atom
			path.addBranchingAtom(atom);
		} else { // Not branching so continue following it till facts or branching
			GADEdge edge = edges.getFirst();
			path.addNonBranchingAtom(edge);
			// continue till we reach a fact or a branching atom			
			
			if(!edge.isFact()) { // this is not a fact
				AtomSet sources = edge.getSources();
				CloseableIterator<Atom> itSources = sources.iterator();
				while(itSources.hasNext()) {
					followPath(itSources.next(), path);
				}
			}
			
		}
	}
	
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		Iterator<LinkedList<GADEdge>> itEdgesLists = this.map.values().iterator();
		while(itEdgesLists.hasNext()) {
			Iterator<GADEdge> itEdges = itEdgesLists.next().iterator();
			while(itEdges.hasNext()) {
				s.append(itEdges.next()).append('\n');
			}
		}
		return s.toString();
	}
}
