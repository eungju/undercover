package undercover.metric;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class MetaDataTest {
	@Test public void loadAndSave() throws IOException {
		File file = File.createTempFile("undercover-", ".md");
		MetaData expected = new MetaData();
		ClassMeta classMeta = new ClassMeta("Foo", "Foo.java");
		MethodMeta methodMeta = new MethodMeta("bar()V");
		BlockMeta blockMeta = new BlockMeta(new ArrayList<Integer>());
		methodMeta.addBlock(blockMeta);
		classMeta.addMethod(methodMeta);
		expected.addClass(classMeta);
		expected.save(file);
		MetaData actual = MetaData.load(file);
		assertEquals(expected, actual);
	}
}
