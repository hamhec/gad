package fr.lirmm.graphik.gad.api.path;

import java.util.LinkedList;

import fr.lirmm.graphik.gad.api.core.GADEdge;

public class GADPath {
	private LinkedList<GADEdge> path;
	
	/* --------------------------------
	 * Constructors
	 * -------------------------------- */
	public GADPath() {
		this(new LinkedList<GADEdge>());
	}
	
	public GADPath(GADEdge edge) {
		this();
		this.path.add(edge);
	}
	
	public GADPath(LinkedList<GADEdge> path) {
		this.path = path;
	}
	
	public GADPath(GADPath d) {
		this();
		this.addAll(d);
	}
	
	/* --------------------------------
	 * Public Methods
	 * -------------------------------- */
	public void add(GADEdge edge) {
		this.path.add(edge);
	}
	
	public void addAll(GADPath d) {
		LinkedList<GADEdge> path = d.getPath();
		for(GADEdge edge : path) {
			this.add(edge);
		}
	}
		
	public LinkedList<GADEdge> getPath() {
		return this.path;
	}
	
	/* --------------------------------
	 * Private Methods
	 * -------------------------------- */
	
}
