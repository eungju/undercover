package undercover.instrument;

public class HelloWorld {
	public static void main(String[] args) {
		System.out.println("Hello, World!");
	}
	
	private int a = 0;
	
	public void ifBranch() {
		if (a == 1) {
			a++;
		}
	}

	public void ifElseIfBranches() {
		if (a == 1) {
			a++;
		} else if (a == 2) {
			a--;
		}
	}
	
	public boolean shortCircuitBranch() {
		return a == 1;
	}
}
