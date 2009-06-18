package undercover.ant;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class InstrumentTaskTest {
	@Test public void instrument() throws IOException {
		InstrumentTask dut = new InstrumentTask();
		File destDir = File.createTempFile("undercover", "");
		dut.setDestDir(destDir);
		//dut.execute();
	}
}
