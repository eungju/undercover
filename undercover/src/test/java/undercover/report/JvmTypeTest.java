package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class JvmTypeTest {
	@Test public void primitiveVoid() {
		assertEquals("void", new JvmType("V").getSimpleName());
	}

	@Test public void primitiveBoolean() {
		assertEquals("boolean", new JvmType("Z").getSimpleName());
	}

	@Test public void primitiveChar() {
		assertEquals("char", new JvmType("C").getSimpleName());
	}

	@Test public void primitiveByte() {
		assertEquals("byte", new JvmType("B").getSimpleName());
	}

	@Test public void primitiveShort() {
		assertEquals("short", new JvmType("S").getSimpleName());
	}

	@Test public void primitiveInt() {
		assertEquals("int", new JvmType("I").getSimpleName());
	}

	@Test public void primitiveLong() {
		assertEquals("long", new JvmType("J").getSimpleName());
	}

	@Test public void primitiveFloat() {
		assertEquals("float", new JvmType("F").getSimpleName());
	}

	@Test public void primitiveDouble() {
		assertEquals("double", new JvmType("D").getSimpleName());
	}

	@Test public void object() {
		assertEquals("Object", new JvmType("Ljava/lang/Object;").getSimpleName());
	}

	@Test public void arrayOfPrimitive() {
		assertEquals("int[]", new JvmType("[I").getSimpleName());
	}

	@Test public void arrayOfObject() {
		assertEquals("Object[]", new JvmType("[Ljava/lang/Object;").getSimpleName());
	}

	@Test public void nestedArray() {
		assertEquals("int[][]", new JvmType("[[I").getSimpleName());
	}

	@Test public void methodReturnType() {
		assertEquals(new JvmType("I"), JvmType.getReturnType("()I"));
	}

	@Test public void methodArgumentTypes() {
		assertEquals(Arrays.asList(), JvmType.getArgumentTypes("()I"));
		assertEquals(Arrays.asList(new JvmType("S")), JvmType.getArgumentTypes("(S)I"));
		assertEquals(Arrays.asList(new JvmType("B"), new JvmType("S")), JvmType.getArgumentTypes("(BS)I"));
		assertEquals(Arrays.asList(new JvmType("Ljava/lang/Object;")), JvmType.getArgumentTypes("(Ljava/lang/Object;)I"));
		assertEquals(Arrays.asList(new JvmType("[I")), JvmType.getArgumentTypes("([I)I"));
		assertEquals(Arrays.asList(new JvmType("[[I")), JvmType.getArgumentTypes("([[I)I"));
	}
}
