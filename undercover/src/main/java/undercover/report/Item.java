package undercover.report;

public interface Item {
	String getDisplayName();
	BlockMetrics getBlockMetrics();
	MethodMetrics getMethodMetrics();
	ClassMetrics getClassMetrics();
	PackageMetrics getPackageMetrics();
}
