package fr.lirmm.graphik.gad.api.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.lirmm.graphik.gad.api.path.CompactGADPath;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.Chase;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomSetFactory;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.forward_chaining.SccChase;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public class GADRuleApplicationHandlerTest {
	
private static GraphOfAtomDependency gad;
	
	
	@BeforeClass
	public static void setUp() throws AtomSetException, IteratorException, ChaseException {
		gad = new GraphOfAtomDependency();

		/*o(a)
		 * r0(a) <-- [q0(a)] <-- [p0(a)]
		 * 			q1_0(a)  <-- [p1(a)]
		 * 			q1_1(a)  <-- [p1(a)]
		 * r2(a) <-- [q2(a)] <-- [p2_0(a), p2_1(a)] | [p0(a)]
		 */
		
		// generate Facts
		CloseableIterator<Atom> itFacts = DlgpParser.parseAtomSet("p0(a), p1(a), p2_0(a), p2_1(a), o(a).");
		AtomSet facts = DefaultAtomSetFactory.instance().create();
		while(itFacts.hasNext()) {
			facts.add(itFacts.next());
		}
		
		// generate Rules
		RuleSet rules = new LinkedListRuleSet();
		rules.add(DlgpParser.parseRule("q0(X) :- p0(X)."));
		rules.add(DlgpParser.parseRule("q2(X) :- p0(X)."));
		rules.add(DlgpParser.parseRule("r0(X) :- q0(X)."));
		rules.add(DlgpParser.parseRule("q1_0(X), q1_1(X) :- p1(X)."));
		rules.add(DlgpParser.parseRule("q2(X) :- p2_0(X), p2_1(X)."));
		rules.add(DlgpParser.parseRule("r2(X) :- q2(X)."));
		
		gad.initialise(facts);
		
		Chase chase = new SccChase<AtomSet>(rules.iterator(), facts,
				new GADRuleApplicationHandler(gad).getRuleApplier());
		chase.execute();
		
		System.out.println("Begin Tests");
		System.out.println(gad.toString());
		
	}
	
	@AfterClass
	public static void trearDown() {
		gad = null;
	}
	
	@Test
	public void getLeaves_Given5LeavesGAD_ShouldReturn5Leaves() {
		Iterator<String> leaves = gad.getLeaves();
		int nbrLeaves = 0;
		while(leaves.hasNext()) {
			String leaf = leaves.next();
			nbrLeaves++;
		}
		assertTrue("Failure - There should be 5 leaves.", nbrLeaves == 5);
	}
	
	
	@Test
	public void getCompactPathFor_GivenStartingFact_ShouldReturnOnePathWithNoBranchingAtoms() throws IteratorException, AtomSetException {
		CompactGADPath path = gad.getCompactPathsFor("p0[1](a)").iterator().next();
		assertFalse("Failure - Path for a Fact should not have branching Atoms.", path.getBranchingAtoms().iterator().hasNext());
	}
	@Test
	public void getCompactPathFor_GivenStartingFact_ShouldReturnOnePathWithOneGADEdgeWithNoRuleApplication() throws IteratorException, AtomSetException {
		CompactGADPath path = gad.getCompactPathsFor("p0[1](a)").iterator().next();
		assertTrue("Failure - Path for a Fact should have one GADEdge with null RuleApplicationTuple.", path.getNonBranchingPath().getPath().getFirst().getRuleApplicationTuple() == null);
	}
	
	@Test
	public void getCompactPathFor_Given1stLevelFact_ShouldReturnOnePathWithSourcesGADEdgePlus1() throws IteratorException, AtomSetException {
		CompactGADPath path = gad.getCompactPathsFor("q1_0[1](a)").iterator().next();
		LinkedList<GADEdge> p = path.getNonBranchingPath().getPath();
		assertTrue("Failure - Path for a 1st lvl Fact should have Two GADEdges.", p.size() == 2);
	}
	
	@Test
	public void getCompactPathFor_Given2stLevelFact_ShouldReturnOnePathWithSourcesGADEdgePlus1() throws IteratorException, AtomSetException {
		CompactGADPath path = gad.getCompactPathsFor("r0[1](a)").iterator().next();
		LinkedList<GADEdge> p = path.getNonBranchingPath().getPath();
		assertTrue("Failure - Paths have been lost - Path for a 1st lvl Fact should have Three GADEdges.", p.size() == 3);
	}
	
	@Test
	public void getCompactPathFor_GivenFactWithTwoDirectPaths_ShouldReturnTwoPaths() throws IteratorException, AtomSetException {
		List<CompactGADPath> path = gad.getCompactPathsFor("q2[1](a)");
		assertTrue("Failure - Paths have been lost - Path for a 1st lvl Fact should have Two GADEdges.", path.size() == 2);
	}
	
	@Test
	public void getCompactPathFor_GivenFactWithTwoIndirectPaths_ShouldReturnOnePaths() throws IteratorException, AtomSetException {
		List<CompactGADPath> path = gad.getCompactPathsFor("r2[1](a)");
		assertTrue("Failure - Path for a 1st lvl Fact should have Three GADEdges.", path.size() == 1);
	}
}
