package undercover.support;

import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

public class UndercoverMockery extends JUnit4Mockery {
	public UndercoverMockery() {
		super();
		setImposteriser(ClassImposteriser.INSTANCE);
	}
}
