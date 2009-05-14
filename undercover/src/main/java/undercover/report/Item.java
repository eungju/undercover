package undercover.report;

public interface Item {
	String getName();
	String getDisplayName();
	int getComplexity();
	int getBlockCount();
	int getCoveredBlockCount();
	int getMethodCount();
	double getAverageMethodComplexity();
	double getCoverageRate();
}
