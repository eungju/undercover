package undercover.report;

public interface Item {
	String getName();
	String getDisplayName();
	int getComplexity();
	int getBlockCount();
	int getMethodCount();
	double getAverageMethodComplexity();

	boolean isExecutable();
	int getCoveredBlockCount();
	double getCoverageRate();
}
