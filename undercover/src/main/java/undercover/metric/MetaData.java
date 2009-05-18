package undercover.metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

import undercover.support.ObjectSupport;

public class MetaData extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1378920643147735683L;

	private final List<ClassMeta> classes;

	public MetaData() {
		this(new ArrayList<ClassMeta>());
	}
	
	public MetaData(List<ClassMeta> classes) {
		this.classes = classes;
	}

	public int addClass(ClassMeta classMeta) {
		classes.add(classMeta);
		return classes.size() - 1;
	}

	public ClassMeta getClass(String name) {
		for (ClassMeta each : classes) {
			if (each.name().equals(name)) {
				return each;
			}
		}
		return null;
	}
	
	public Collection<ClassMeta> getAllClasses() {
		return classes;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ClassMeta each : classes) {
			builder.append(each.toString()).append(',');
		}
		return builder.toString();
	}
	
	public void save(File file) throws IOException {
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(file));
			output.writeObject(this);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	public static MetaData load(File file) throws IOException {
		ObjectInputStream output = null;
		try {
			output = new ObjectInputStream(new FileInputStream(file));
			return (MetaData) output.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}
