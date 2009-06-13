package undercover.report;

/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * A Java type. This class can be used to make it easier to manipulate type and
 * method descriptors.
 * 
 * @author Eric Bruneton
 * @author Chris Nokleberg
 */
public class Type {

    /**
     * The sort of the <tt>void</tt> type. See {@link #getSort getSort}.
     */
    public static final int VOID = 0;

    /**
     * The sort of the <tt>boolean</tt> type. See {@link #getSort getSort}.
     */
    public static final int BOOLEAN = 1;

    /**
     * The sort of the <tt>char</tt> type. See {@link #getSort getSort}.
     */
    public static final int CHAR = 2;

    /**
     * The sort of the <tt>byte</tt> type. See {@link #getSort getSort}.
     */
    public static final int BYTE = 3;

    /**
     * The sort of the <tt>short</tt> type. See {@link #getSort getSort}.
     */
    public static final int SHORT = 4;

    /**
     * The sort of the <tt>int</tt> type. See {@link #getSort getSort}.
     */
    public static final int INT = 5;

    /**
     * The sort of the <tt>float</tt> type. See {@link #getSort getSort}.
     */
    public static final int FLOAT = 6;

    /**
     * The sort of the <tt>long</tt> type. See {@link #getSort getSort}.
     */
    public static final int LONG = 7;

    /**
     * The sort of the <tt>double</tt> type. See {@link #getSort getSort}.
     */
    public static final int DOUBLE = 8;

    /**
     * The sort of array reference types. See {@link #getSort getSort}.
     */
    public static final int ARRAY = 9;

    /**
     * The sort of object reference type. See {@link #getSort getSort}.
     */
    public static final int OBJECT = 10;

    /**
     * The <tt>void</tt> type.
     */
    public static final Type VOID_TYPE = new Type(VOID);

    /**
     * The <tt>boolean</tt> type.
     */
    public static final Type BOOLEAN_TYPE = new Type(BOOLEAN);

    /**
     * The <tt>char</tt> type.
     */
    public static final Type CHAR_TYPE = new Type(CHAR);

    /**
     * The <tt>byte</tt> type.
     */
    public static final Type BYTE_TYPE = new Type(BYTE);

    /**
     * The <tt>short</tt> type.
     */
    public static final Type SHORT_TYPE = new Type(SHORT);

    /**
     * The <tt>int</tt> type.
     */
    public static final Type INT_TYPE = new Type(INT);

    /**
     * The <tt>float</tt> type.
     */
    public static final Type FLOAT_TYPE = new Type(FLOAT);

    /**
     * The <tt>long</tt> type.
     */
    public static final Type LONG_TYPE = new Type(LONG);

    /**
     * The <tt>double</tt> type.
     */
    public static final Type DOUBLE_TYPE = new Type(DOUBLE);

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /**
     * The sort of this Java type.
     */
    private final int sort;

    /**
     * A buffer containing the internal name of this Java type. This field is
     * only used for reference types.
     */
    private final char[] buf;

    /**
     * The offset of the internal name of this Java type in {@link #buf buf}.
     * This field is only used for reference types.
     */
    private final int off;

    /**
     * The length of the internal name of this Java type. This field is only
     * used for reference types.
     */
    private final int len;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Constructs a primitive type.
     * 
     * @param sort the sort of the primitive type to be constructed.
     */
    private Type(final int sort) {
        this(sort, null, 0, 1);
    }

    /**
     * Constructs a reference type.
     * 
     * @param sort the sort of the reference type to be constructed.
     * @param buf a buffer containing the descriptor of the previous type.
     * @param off the offset of this descriptor in the previous buffer.
     * @param len the length of this descriptor.
     */
    private Type(final int sort, final char[] buf, final int off, final int len)
    {
        this.sort = sort;
        this.buf = buf;
        this.off = off;
        this.len = len;
    }

    /**
     * Returns the Java type corresponding to the given type descriptor.
     * 
     * @param typeDescriptor a type descriptor.
     * @return the Java type corresponding to the given type descriptor.
     */
    public static Type getType(final String typeDescriptor) {
        return getType(typeDescriptor.toCharArray(), 0);
    }

    /**
     * Returns the Java type corresponding to the given internal name.
     * 
     * @param internalName an internal name.
     * @return the Java type corresponding to the given internal name.
     */
    public static Type getObjectType(final String internalName) {
        char[] buf = internalName.toCharArray();
        return new Type(buf[0] == '[' ? ARRAY : OBJECT, buf, 0, buf.length);
    }

    /**
     * Returns the Java types corresponding to the argument types of the given
     * method descriptor.
     * 
     * @param methodDescriptor a method descriptor.
     * @return the Java types corresponding to the argument types of the given
     *         method descriptor.
     */
    public static Type[] getArgumentTypes(final String methodDescriptor) {
        char[] buf = methodDescriptor.toCharArray();
        int off = 1;
        int size = 0;
        while (true) {
            char car = buf[off++];
            if (car == ')') {
                break;
            } else if (car == 'L') {
                while (buf[off++] != ';') {
                }
                ++size;
            } else if (car != '[') {
                ++size;
            }
        }
        Type[] args = new Type[size];
        off = 1;
        size = 0;
        while (buf[off] != ')') {
            args[size] = getType(buf, off);
            off += args[size].len + (args[size].sort == OBJECT ? 2 : 0);
            size += 1;
        }
        return args;
    }

    /**
     * Returns the Java type corresponding to the return type of the given
     * method descriptor.
     * 
     * @param methodDescriptor a method descriptor.
     * @return the Java type corresponding to the return type of the given
     *         method descriptor.
     */
    public static Type getReturnType(final String methodDescriptor) {
        char[] buf = methodDescriptor.toCharArray();
        return getType(buf, methodDescriptor.indexOf(')') + 1);
    }

    /**
     * Returns the Java type corresponding to the given type descriptor.
     * 
     * @param buf a buffer containing a type descriptor.
     * @param off the offset of this descriptor in the previous buffer.
     * @return the Java type corresponding to the given type descriptor.
     */
    private static Type getType(final char[] buf, final int off) {
        int len;
        switch (buf[off]) {
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'B':
                return BYTE_TYPE;
            case 'S':
                return SHORT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'D':
                return DOUBLE_TYPE;
            case '[':
                len = 1;
                while (buf[off + len] == '[') {
                    ++len;
                }
                if (buf[off + len] == 'L') {
                    ++len;
                    while (buf[off + len] != ';') {
                        ++len;
                    }
                }
                return new Type(ARRAY, buf, off, len + 1);
                // case 'L':
            default:
                len = 1;
                while (buf[off + len] != ';') {
                    ++len;
                }
                return new Type(OBJECT, buf, off + 1, len - 1);
        }
    }

    // ------------------------------------------------------------------------
    // Accessors
    // ------------------------------------------------------------------------

    /**
     * Returns the sort of this Java type.
     * 
     * @return {@link #VOID VOID}, {@link #BOOLEAN BOOLEAN},
     *         {@link #CHAR CHAR}, {@link #BYTE BYTE}, {@link #SHORT SHORT},
     *         {@link #INT INT}, {@link #FLOAT FLOAT}, {@link #LONG LONG},
     *         {@link #DOUBLE DOUBLE}, {@link #ARRAY ARRAY} or
     *         {@link #OBJECT OBJECT}.
     */
    public int getSort() {
        return sort;
    }

    /**
     * Returns the number of dimensions of this array type. This method should
     * only be used for an array type.
     * 
     * @return the number of dimensions of this array type.
     */
    public int getDimensions() {
        int i = 1;
        while (buf[off + i] == '[') {
            ++i;
        }
        return i;
    }

    /**
     * Returns the type of the elements of this array type. This method should
     * only be used for an array type.
     * 
     * @return Returns the type of the elements of this array type.
     */
    public Type getElementType() {
        return getType(buf, off + getDimensions());
    }

    /**
     * Returns the name of the class corresponding to this type.
     * 
     * @return the fully qualified name of the class corresponding to this type.
     */
    public String getClassName() {
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
            case FLOAT:
                return "float";
            case LONG:
                return "long";
            case DOUBLE:
                return "double";
            case ARRAY:
                StringBuffer b = new StringBuffer(getElementType().getClassName());
                for (int i = getDimensions(); i > 0; --i) {
                    b.append("[]");
                }
                return b.toString();
                // case OBJECT:
            default:
                return new String(buf, off, len).replace('/', '.');
        }
    }

    /**
     * Returns the simple name of the class corresponding to this type.
     * 
     * @return the simple name of the class corresponding to this type.
     */
    public String getSimpleClassName() {
        switch (sort) {
        case ARRAY:
            StringBuffer b = new StringBuffer(getElementType().getSimpleClassName());
            for (int i = getDimensions(); i > 0; --i) {
                b.append("[]");
            }
            return b.toString();
        case OBJECT:
        	String name = new String(buf, off, len);
        	int index = name.lastIndexOf('/');
        	return index == -1 ? name : name.substring(index + 1);
        default:
        	return getClassName();
        }
    }

    /**
     * Returns the internal name of the class corresponding to this object or
     * array type. The internal name of a class is its fully qualified name (as
     * returned by Class.getName(), where '.' are replaced by '/'. This method
     * should only be used for an object or array type.
     * 
     * @return the internal name of the class corresponding to this object type.
     */
    public String getInternalName() {
        return new String(buf, off, len);
    }

    // ------------------------------------------------------------------------
    // Equals, hashCode and toString
    // ------------------------------------------------------------------------

    /**
     * Tests if the given object is equal to this type.
     * 
     * @param o the object to be compared to this type.
     * @return <tt>true</tt> if the given object is equal to this type.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        Type t = (Type) o;
        if (sort != t.sort) {
            return false;
        }
        if (sort == OBJECT || sort == ARRAY) {
            if (len != t.len) {
                return false;
            }
            for (int i = off, j = t.off, end = i + len; i < end; i++, j++) {
                if (buf[i] != t.buf[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a hash code value for this type.
     * 
     * @return a hash code value for this type.
     */
    public int hashCode() {
        int hc = 13 * sort;
        if (sort == OBJECT || sort == ARRAY) {
            for (int i = off, end = i + len; i < end; i++) {
                hc = 17 * (hc + buf[i]);
            }
        }
        return hc;
    }

    /**
     * Returns a string representation of this type.
     * 
     * @return the descriptor of this type.
     */
    public String toString() {
        return getInternalName();
    }
}
