package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class FieldTypeTest {
	@Test public void primitiveVoid() {
		assertEquals("void", new FieldType("V").getSimpleName());
	}

	@Test public void primitiveBoolean() {
		assertEquals("boolean", new FieldType("Z").getSimpleName());
	}

	@Test public void primitiveChar() {
		assertEquals("char", new FieldType("C").getSimpleName());
	}

	@Test public void primitiveByte() {
		assertEquals("byte", new FieldType("B").getSimpleName());
	}

	@Test public void primitiveShort() {
		assertEquals("short", new FieldType("S").getSimpleName());
	}

	@Test public void primitiveInt() {
		assertEquals("int", new FieldType("I").getSimpleName());
	}

	@Test public void primitiveLong() {
		assertEquals("long", new FieldType("J").getSimpleName());
	}

	@Test public void primitiveFloat() {
		assertEquals("float", new FieldType("F").getSimpleName());
	}

	@Test public void primitiveDouble() {
		assertEquals("double", new FieldType("D").getSimpleName());
	}

	@Test public void object() {
		assertEquals("Object", new FieldType("Ljava/lang/Object;").getSimpleName());
	}

	@Test public void arrayOfPrimitive() {
		assertEquals("int[]", new FieldType("[I").getSimpleName());
	}

	@Test public void arrayOfObject() {
		assertEquals("Object[]", new FieldType("[Ljava/lang/Object;").getSimpleName());
	}

	@Test public void nestedArray() {
		assertEquals("int[][]", new FieldType("[[I").getSimpleName());
	}

	@Test public void methodReturnType() {
		assertEquals(new FieldType("I"), FieldType.getReturnType("()I"));
	}

	@Test public void methodArgumentTypes() {
		assertEquals(Arrays.asList(), FieldType.getArgumentTypes("()I"));
		assertEquals(Arrays.asList(new FieldType("S")), FieldType.getArgumentTypes("(S)I"));
		assertEquals(Arrays.asList(new FieldType("B"), new FieldType("S")), FieldType.getArgumentTypes("(BS)I"));
		assertEquals(Arrays.asList(new FieldType("Ljava/lang/Object;")), FieldType.getArgumentTypes("(Ljava/lang/Object;)I"));
		assertEquals(Arrays.asList(new FieldType("[I")), FieldType.getArgumentTypes("([I)I"));
		assertEquals(Arrays.asList(new FieldType("[[I")), FieldType.getArgumentTypes("([[I)I"));
	}
}
