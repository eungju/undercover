package undercover.instrument;

public abstract class HelloWorld {
	public static void main(String[] args) {
		System.out.println("Hello, World!");
	}
	
	public abstract void abstractMethod();
	
	public void empty() {
	}
	
	public boolean b1() {
		return true;
	}

	public boolean b2() {
		return false;
	}

	public boolean b3() {
		return true;
	}

	public boolean b4() {
		return false;
	}

	public boolean b5() {
		return true;
	}

	public void simple() {
		b1();
	}
	
	public void sequential() {
		b1();
		b2();
	}
	
	public void ifBranch() {
		if (b1()) {
			b2();
		}
	}

	public void ifElseIfBranches() {
		if (b1()) {
			b2();
		} else if (b3()) {
			b4();
		}
	}
	
	public boolean shortCircuitBranch() {
		return b1() && b2();
	}
	
	public void tryCatchBranch() {
		try {
			b1();
		} catch (RuntimeException e) {
			b2();
		}
	}

	public void tryFinallyBranch() {
		try {
			b1();
		} finally {
			b2();
		}
	}

	public void tryCatchFinallyBranch() {
		try {
			b1();
		} catch (RuntimeException e) {
			b2();
		} finally {
			b3();
		}
	}

	public void tryCatchCatchBranch() {
		try {
			b1();
		} catch (RuntimeException e) {
			b2();
		} catch (Exception e) {
			b3();
		}
	}
	
	public void throwingMethod() {
		throw new UnsupportedOperationException();
	}
	
	public void forLoop(int end) {
		for (int i = 0; i < end; i++) {
			b1();
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
