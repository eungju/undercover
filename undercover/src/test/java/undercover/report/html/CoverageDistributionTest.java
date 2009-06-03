package undercover.report.html;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoverageDistributionTest {
	@Test public void zeroToNinePercent() {
		assertEquals(0, CoverageDistribution.coverageInterval(0));
		assertEquals(0, CoverageDistribution.coverageInterval(0.09));
	}

	@Test public void tenToNineteenPercent() {
		assertEquals(1, CoverageDistribution.coverageInterval(0.1));
		assertEquals(1, CoverageDistribution.coverageInterval(0.19));
	}

	@Test public void ninetyToHundredPercent() {
		assertEquals(9, CoverageDistribution.coverageInterval(0.9));
		assertEquals(9, CoverageDistribution.coverageInterval(1));
	}
}
