package undercover.instrument;

public class Sample {
	public int simple(int a) {
		return a;
	}
	
	public int methodCall(int a) {
		return Math.max(a, a);
	}
	
	public int ifStatement(int a) {
		if (a == 0) {
			return 0;
		}
		return a;
	}
}
