package undercover.metric;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MetaDataTest {
	@Test public void loadAndSave() throws IOException {
		File file = File.createTempFile("undercover-", ".md");
		MetaData expected = new MetaData();
		ClassMetric classMetric = new ClassMetric("Foo", "Foo.java");
		MethodMetric methodMetric = new MethodMetric("bar()V");
		BlockMetric blockMetric = new BlockMetric();
		methodMetric.addBlock(blockMetric);
		classMetric.addMethod(methodMetric);
		expected.addClass(classMetric);
		expected.save(file);
		MetaData actual = MetaData.load(file);
		assertEquals(expected, actual);
	}
}
