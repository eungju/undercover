package undercover.support;

public class Proportion extends ObjectSupport {
	public final int part;
	public final int whole;

	public Proportion(int part, int whole) {
		this.part = part;
		this.whole = whole;
	}
	
	public double getRatio() {
		return whole == 0 ? 1 : (double) part / whole;
	}
}
