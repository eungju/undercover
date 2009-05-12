package undercover.metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import undercover.support.ObjectSupport;

public class MetaData extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1378920643147735683L;

	private List<ClassMetric> classes;

	public MetaData() {
		this(new ArrayList<ClassMetric>());
	}
	
	public MetaData(List<ClassMetric> classes) {
		this.classes = classes;
	}

	public int addClass(ClassMetric classMetric) {
		classes.add(classMetric);
		return classes.size() - 1;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ClassMetric each : classes) {
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
