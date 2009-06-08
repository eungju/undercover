package undercover.report;

public interface Item {
	String getDisplayName();
	String getLinkName();
	BlockMetrics getBlockMetrics();
	MethodMetrics getMethodMetrics();
	ClassMetrics getClassMetrics();
	PackageMetrics getPackageMetrics();
}
