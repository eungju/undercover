package undercover.report;

import java.util.ArrayList;
import java.util.List;

import undercover.support.ObjectSupport;

public class JvmType extends ObjectSupport {
	static final char VOID = 'V';
	static final char BOOLEAN = 'Z';
	static final char CHAR = 'C';
	static final char BYTE = 'B';
	static final char SHORT = 'S';
	static final char INT = 'I';
	static final char LONG = 'J';
	static final char FLOAT = 'F';
	static final char DOUBLE = 'D';
	static final char OBJECT = 'L';
	static final char ARRAY = '[';
	
	private final char sort;
	private final String buf;
	private final int offset;
	private final int length;

	public JvmType(String buf) {
		this(buf, 0);
	}
	
	public JvmType(String buf, int offset) {
		this.buf = buf;
		this.offset = offset;
		sort = buf.charAt(offset);
		if (sort == 'L') {
			length = buf.indexOf(';', offset) - offset + 1;			
		} else if (sort == '[') {
			length = getDemension() + getElementType().length;
		} else if ("VZCBSIJFD".indexOf(sort) >= 0) {
			length = 1;
		} else  {
			throw new IllegalStateException("Unknown type " + buf.substring(offset, offset));				
		}
	}

	public String getSimpleName() {
		switch (sort) {
		case VOID:
			return "void";
		case BOOLEAN:
			return "boolean";
		case CHAR:
			return "char";
		case BYTE:
			return "byte";
		case SHORT:
			return "short";
		case INT:
			return "int";
		case LONG:
			return "long";
		case FLOAT:
			return "float";
		case DOUBLE:
			return "double";
		case OBJECT:
			String className = buf.substring(offset + 1, offset + length - 1);
			return className.substring(className.lastIndexOf('/') + 1, className.length());
		case ARRAY:
			int demension = getDemension();
			StringBuilder result = new StringBuilder(getElementType().getSimpleName());
			while (demension-- > 0) {
				result.append("[]");
			}
			return result.toString();
		}
		return null;
	}
	
	int getDemension() {
		int result = 1;
		while (buf.charAt(offset + result) == '[') {
			result++;
		}
		return result;
	}
	
	JvmType getElementType() {
		return new JvmType(buf, offset + getDemension());
	}

	public static JvmType getReturnType(String methodDescriptor) {
		return new JvmType(methodDescriptor.substring(methodDescriptor.lastIndexOf(')') + 1));		
	}

	public static List<JvmType> getArgumentTypes(String methodDescriptor) {
		List<JvmType> result = new ArrayList<JvmType>();
		int offset = 1;
		while (methodDescriptor.charAt(offset) != ')') {
			JvmType argumentType = new JvmType(methodDescriptor, offset);
			offset += argumentType.length;
			result.add(argumentType);
		}
		return result;
	}
}
