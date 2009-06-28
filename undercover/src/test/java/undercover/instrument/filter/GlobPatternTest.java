package undercover.instrument.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class GlobPatternTest {
	@Test public void exactMatch() {
		GlobPattern dut = new GlobPattern("p/c");
		assertTrue(dut.match("p/c"));
	}
	
	@Test public void starMatchesZeroOrMoreCharacters() {
		GlobPattern dut = new GlobPattern("pkg/*");
		assertTrue(dut.match("pkg/class"));
		assertFalse(dut.match("pkg/pkg/class"));
	}
	
	@Test public void questionMatchesOneCharacter() {
		GlobPattern dut = new GlobPattern("pkg?");
		assertTrue(dut.match("pkgs"));
		assertFalse(dut.match("pkg/"));
	}

	@Test public void doubleStarsMatchesZeroOrMoreDirectoriesAndFiles() {
		GlobPattern dut = new GlobPattern("pkg/**");
		assertTrue(dut.match("pkg/class"));
		assertTrue(dut.match("pkg/pkg/class"));
	}
	
	@Test public void doubleStarsFollowedBySeparatorMatchesZeroOrMoreDirectories() {
		GlobPattern dut = new GlobPattern("pkg/**/class");
		assertTrue(dut.match("pkg/class"));
	}

	@Test public void pathContainsDollar() {
		GlobPattern dut = new GlobPattern("pkg/class$1");
		assertFalse(dut.match("pkg/class"));
		assertTrue(dut.match("pkg/class$1"));
	}
}
