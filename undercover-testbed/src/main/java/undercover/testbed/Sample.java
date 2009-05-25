package undercover.testbed;

public class Sample implements ISample {
	public void notCovered() {
	}
	
	public void simple() {
	}

	public void ifBranch(boolean a) {
		if (a) {
			System.out.println(VALUES[1]);
		} else {
			System.out.println(VALUES[0]);
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
	
	public void throwingMethod(boolean a) {
		if (a) {
			throw new IllegalStateException();
		}
	}
	
	public void tryFinally(boolean a) {
		try {
			System.out.println("1");
			throwingMethod(a);
			System.out.println("2");
		} finally {
			System.out.println("finally");
		}
	}

	public void tableSwitchBranches(int i) {
		switch (i) {
		case 1:
			System.out.println("1");
			break;
		case 2:
			System.out.println("2");
		}
	}

	public void lookupSwitchBranches(int i) {
		switch (i) {
		case 1:
			System.out.println("1");
			break;
		case Integer.MAX_VALUE:
			System.out.println("max");
		}
	}
}
