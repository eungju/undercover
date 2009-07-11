package undercover.support;

public class Proportion {
	public final int part;
	public final int whole;

	public Proportion(int part, int whole) {
		this.part = part;
		this.whole = whole;
	}
	
	public double getRatio() {
		return whole == 0 ? 1 : (double) part / whole;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + part;
		result = prime * result + whole;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Proportion other = (Proportion) obj;
		return part == other.part && whole == other.whole;
	}
}
