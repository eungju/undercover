package undercover.support;

public class Proportion {
	public final int part;
	public final int whole;

	public Proportion(int part, int whole) {
		this.part = part;
		this.whole = whole;
	}
	
	public double ratio() {
		return (double) part / whole;
	}
}
