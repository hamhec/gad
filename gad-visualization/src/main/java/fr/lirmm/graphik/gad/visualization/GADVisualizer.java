package fr.lirmm.graphik.gad.visualization;

import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import fr.lirmm.graphik.gad.api.core.GADEdge;
import fr.lirmm.graphik.gad.api.core.GraphOfAtomDependency;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public class GADVisualizer {
	
	public GraphOfAtomDependency gad;
	public Graph graph;
	
	public GADVisualizer(GraphOfAtomDependency gad) {
		this.gad = gad;
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        
        this.graph = new SingleGraph("vis");
        
        graph.addAttribute("ui.stylesheet", "url('file:///home/hamhec/workspace/java/gad/gad-visualization/src/main/java/fr/lirmm/graphik/gad/visualization/styles.css')");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
	}
	
	public void display() {
		this.construct();
		this.style();
		this.graph.display();
	}
	
	public void construct() {
		Iterator<String> itLeaves = this.gad.getLeaves();
		while(itLeaves.hasNext()) {
			crowlBack(itLeaves.next());
		}
	}
	
	public void style() {
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
		
		for(Edge edge : graph.getEachEdge()) {
			edge.addAttribute("layout.weight", 1.5f);
		}
	}
	
	private void crowlBack(String atomString) {
		Node e = null;
		Node n = this.generateAtomNode(atomString);
		Iterator<GADEdge> itEdges = this.gad.getInEdges(atomString).iterator();
		while(itEdges.hasNext()) {
			GADEdge edge = itEdges.next();
			if(edge.getRuleApplicationTuple() == null) { // its a Fact
				n.addAttribute("isFact");
			} else {
				e = this.generateRuleApplicationNode(edge.getRuleApplicationTuple().toString());
				this.addEdge(e, n, true);
				
				CloseableIterator<Atom> itSources = edge.getSources().iterator();
				try {
					while(itSources.hasNext()) {
						Node s = this.generateAtomNode(itSources.next().toString());
						this.addEdge(s, e, false);
						crowlBack(s.getId());
					}
				} catch (IteratorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	private Node generateAtomNode(String atomString) {
		Node n = this.graph.getNode(atomString);
		if(n == null) { 
			n = this.graph.addNode(atomString);
			n.addAttribute("isAtom");
		}
		return n;
	}
	
	private Node generateRuleApplicationNode(String ruleApplicationString) {
		Node n = this.graph.getNode(ruleApplicationString);
		if(n == null) { 
			n = this.graph.addNode(ruleApplicationString);
		}
		return n;
	}
	
	private Edge addEdge(Node source, Node target, boolean bool) {
		Edge e = this.graph.getEdge(source.getId() + target.getId());
		if(e == null) e = this.graph.addEdge(source.getId() + target.getId(), source.getId(), target.getId(), bool);
		return e;
	}
}
