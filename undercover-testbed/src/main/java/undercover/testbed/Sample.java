package undercover.testbed;

public class Sample {
	public void notCovered() {
	}
	
	public void simple() {
	}

	public void ifBranch(boolean a) {
		if (a) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}

	public void ifBranch(boolean a, boolean b) {
		if (a || b) {
			System.out.println("one of them is true");
		}
	}
	
	public void inlineIfBranch(boolean a) {
		if (a) { System.out.println("true"); } else { System.out.println("false"); }
	}
}
