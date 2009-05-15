package undercover.instrument;

public abstract class HelloWorld {
	public static void main(String[] args) {
		System.out.println("Hello, World!");
	}
	
	private int a = 0;
	
	public abstract void abstractMethod();
	
	public void empty() {
	}
	
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
	
	public void tryCatchBranch() {
		try {
			a++;
		} catch (RuntimeException e) {
			a--;
		}
	}

	public void tryFinallyBranch() {
		try {
			a++;
		} finally {
			a++;
		}
	}

	public void tryCatchFinallyBranch() {
		try {
			a++;
		} catch (RuntimeException e) {
			a--;
		} finally {
			a++;
		}
	}

	public void tryCatchCatchBranch() {
		try {
			a++;
		} catch (RuntimeException e) {
			a--;
		} catch (Exception e) {
			a--;
		}
	}
}
