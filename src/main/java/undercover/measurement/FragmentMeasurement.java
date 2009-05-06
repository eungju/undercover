package undercover.measurement;

/**
 * Project, Package, Class, Method, Block, Fragment
 */
public class FragmentMeasurement {
	public final Location location;
	public int executions;
	public int conditionals;
	
	public FragmentMeasurement(Location location) {
		this.location = location;
		this.executions = 0;
		this.conditionals = 0;
	}
	
	public void executed() {
		executions++;
	}
	
	public void foundConditional() {
		int x = 3;
		if (x > 2 && x < 10) {
			System.out.println("X");
		}
		conditionals++;
	}
}
