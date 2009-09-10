package undercover.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import undercover.support.IOUtils;
import undercover.support.ObjectSupport;

public class CoverageData extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -2867261294970889507L;

	private final Map<String, Coverage> coverages = new HashMap<String, Coverage>();

	public synchronized void register(String className, int[][] coverage) {
		coverages.put(className, new Coverage(className, coverage));
	}
	
	public synchronized Coverage getCoverage(String className) {
		return coverages.get(className);
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
	
	public static CoverageData load(File file) throws IOException {
		ObjectInputStream output = null;
		try {
			output = new ObjectInputStream(new FileInputStream(file));
			return (CoverageData) output.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}

