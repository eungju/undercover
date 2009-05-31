package undercover.support;

/**
 * Don't calculate twice. Remember it and use it.
 */
public abstract class LazyValue<T> {
	private T value;
	private boolean cached; 
	
	public T value() {
		if (cached) {
			return value;
		}
		value = calculate();
		cached = true;
		return value;
	}

	public void forget() {
		cached = false;
	}

	protected abstract T calculate();
}
