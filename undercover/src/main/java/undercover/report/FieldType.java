package undercover.report;

import java.util.ArrayList;
import java.util.List;

public class FieldType {
	private final String descriptor;

	FieldType(String descriptor) {
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
		throw new IllegalStateException("Unknown type descriptor " + descriptor);
	}
	
	int getDemension() {
		int demension = 1;
		while (descriptor.charAt(demension) == '[') {
			demension++;
		}
		return demension;
	}
	
	FieldType getElementType() {
		return FieldType.getType(descriptor, getDemension());
	}

	public String toString() {
		return descriptor;
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
	
	public boolean equals(Object o) {
		FieldType other = (FieldType) o;
		return descriptor.equals(other.descriptor);
	}

	public static FieldType getReturnType(String methodDescriptor) {
		return new FieldType(methodDescriptor.substring(methodDescriptor.lastIndexOf(')') + 1));		
	}

	public static List<FieldType> getArgumentTypes(String methodDescriptor) {
		List<FieldType> result = new ArrayList<FieldType>();
		int offset = 1;
		while (methodDescriptor.charAt(offset) != ')') {
			FieldType argumentType = FieldType.getType(methodDescriptor, offset);
			result.add(argumentType);
			offset += argumentType.descriptor.length();
		}
		return result;
	}

	public static FieldType getType(String buf, int offset) {
		char sort = buf.charAt(offset);
		if (sort == 'L') {
			return new FieldType(buf.substring(offset, buf.indexOf(';', offset) + 1));			
		} else if (sort == '[') {
			int demension = 1;
			while (buf.charAt(offset + demension) == '[') {
				demension++;
			}
			return new FieldType(buf.substring(offset, offset + demension + getType(buf, offset + demension).descriptor.length()));
		} else  {
			return new FieldType(buf.substring(offset, offset + 1));
		}
	}
}
