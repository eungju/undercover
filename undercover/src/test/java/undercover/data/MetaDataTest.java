package undercover.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class MetaDataTest {
	@Test public void loadAndSave() throws IOException {
		File file = File.createTempFile("undercover-", ".md");
		MetaData expected = new MetaData();
		MethodMeta methodMeta = new MethodMeta("bar()V", 0, Arrays.asList(new BlockMeta(new ArrayList<Integer>())));
		ClassMeta classMeta = new ClassMeta("Foo", "Foo.java", Arrays.asList(methodMeta));
		expected.addClass(classMeta);
		expected.save(file);
		MetaData actual = MetaData.load(file);
		assertEquals(expected, actual);
	}
}
