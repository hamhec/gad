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
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.IteratorException;

public class GraphOfAtomDependencyTest {
	private static GraphOfAtomDependency gad;
	
	
	@BeforeClass
	public static void setUp() throws AtomSetException {
		gad = new GraphOfAtomDependency();

		/*
		 * r0(a) <-- [q0(a)] <-- [p0(a)]
		 * 			q1_0(a)  <-- [p1(a)]
		 * 			q1_1(a)  <-- [p1(a)]
		 * r2(a) <-- [q2(a)] <-- [p2_0(a), p2_1(a)] | [p0(a)]
		 */
		
		// generate1Source1Target
		Atom source1 = DlgpParser.parseAtom("p0(a).");
		gad.addEdge(new GADEdge(null, source1));
		GADGenerator.generate1Source1Target(gad, source1, "q0");
			
		Atom q0 = DlgpParser.parseAtom("q0(a).");
		GADGenerator.generate1Source1Target(gad, q0, "r0");
		
		
		// generate1Source2Target
		Atom source2 = DlgpParser.parseAtom("p1(a).");
		gad.addEdge(new GADEdge(null, source2));
		GADGenerator.generate1Source2Target(gad, source2, "q1_0", "q1_1");
		
		// generate2Source1Target
		Atom source3 = DlgpParser.parseAtom("p2_0(a).");
		Atom source4 = DlgpParser.parseAtom("p2_1(a).");
		gad.addEdge(new GADEdge(null, source3));
		gad.addEdge(new GADEdge(null, source4));
		GADGenerator.generate2Source1Target(gad, source3, source4, "q2");
		
		// generate chain
		Atom q2 = DlgpParser.parseAtom("q2(a).");
		GADGenerator.generate1Source1Target(gad, q2, "r2");
		
		// generate simple fact
		Atom simpleAtom = DlgpParser.parseAtom("o(a).");
		gad.addEdge(new GADEdge(null, simpleAtom));
		
		// generate a second path for q2
		GADGenerator.generate1Source1Target(gad, source1, "q2");
		
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
			leaves.next();
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
		assertTrue("Failure - Path for a 1st lvl Fact should have Three GADEdges.", p.size() == 3);
	}
	
	@Test
	public void getCompactPathFor_GivenFactWithTwoDirectPaths_ShouldReturnTwoPaths() throws IteratorException, AtomSetException {
		List<CompactGADPath> path = gad.getCompactPathsFor("q2[1](a)");
		assertTrue("Failure - Path for a 1st lvl Fact should have Three GADEdges.", path.size() == 2);
	}
	
	@Test
	public void getCompactPathFor_GivenFactWithTwoIndirectPaths_ShouldReturnOnePaths() throws IteratorException, AtomSetException {
		List<CompactGADPath> path = gad.getCompactPathsFor("r2[1](a)");
		assertTrue("Failure - Path for a 1st lvl Fact should have Three GADEdges.", path.size() == 1);
	}
}
