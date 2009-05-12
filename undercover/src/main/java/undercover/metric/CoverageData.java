package undercover.metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

public class CoverageData {
	private Map<UUID, BlockCoverage> blocks = new HashMap<UUID, BlockCoverage>();
	
	public void touchBlock(UUID id) {
		BlockCoverage coverage = blocks.get(id);
		if (coverage == null) {
			coverage = new BlockCoverage(id);
			blocks.put(id, coverage);
		}
		coverage.touch();
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
			throw new IOException("Class not found", e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}

