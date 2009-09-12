package undercover.report;

import java.util.ArrayList;
import java.util.List;

public class JvmType {
	private final String descriptor;

	JvmType(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getSimpleName() {
		char sort = descriptor.charAt(0);
		switch (sort) {
		case 'V':
			return "void";
		case 'Z':
			return "boolean";
		case 'C':
			return "char";
		case 'B':
			return "byte";
		case 'S':
			return "short";
		case 'I':
			return "int";
		case 'J':
			return "long";
		case 'F':
			return "float";
		case 'D':
			return "double";
		case 'L':
			String className = descriptor.substring(1, descriptor.length() - 1);
			return className.substring(className.lastIndexOf('/') + 1, className.length());
		case '[':
			int demension = getDemension();
			StringBuilder result = new StringBuilder(getElementType().getSimpleName());
			while (demension-- > 0) {
				result.append("[]");
			}
			return result.toString();
		}
		throw new IllegalStateException("Unknown type descriptor" + descriptor);
	}
	
	int getDemension() {
		int demension = 1;
		while (descriptor.charAt(demension) == '[') {
			demension++;
		}
		return demension;
	}
	
	JvmType getElementType() {
		return JvmType.getType(descriptor, getDemension());
	}

	public String toString() {
		return descriptor;
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
	
	public boolean equals(Object o) {
		JvmType other = (JvmType) o;
		return descriptor.equals(other.descriptor);
	}

	public static JvmType getReturnType(String methodDescriptor) {
		return new JvmType(methodDescriptor.substring(methodDescriptor.lastIndexOf(')') + 1));		
	}

	public static List<JvmType> getArgumentTypes(String methodDescriptor) {
		List<JvmType> result = new ArrayList<JvmType>();
		int offset = 1;
		while (methodDescriptor.charAt(offset) != ')') {
			JvmType argumentType = JvmType.getType(methodDescriptor, offset);
			result.add(argumentType);
			offset += argumentType.descriptor.length();
		}
		return result;
	}

	public static JvmType getType(String buf, int offset) {
		char sort = buf.charAt(offset);
		if (sort == 'L') {
			return new JvmType(buf.substring(offset, buf.indexOf(';', offset) + 1));			
		} else if (sort == '[') {
			int demension = 1;
			while (buf.charAt(offset + demension) == '[') {
				demension++;
			}
			return new JvmType(buf.substring(offset, offset + demension + getType(buf, offset + demension).descriptor.length()));
		} else  {
			return new JvmType(buf.substring(offset, offset + 1));
		}
	}
}
