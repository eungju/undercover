package undercover.testbed;

public interface ISample {
	String[] VALUES = new String[] { "false", "true" };
	
	void notCovered();
	void simple();
}
