package undercover.metric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import undercover.support.ObjectSupport;

public class CoverageData extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -2867261294970889507L;

	private final Map<UUID, BlockCoverage> blocks = new HashMap<UUID, BlockCoverage>();
	
	public void touchBlock(UUID id) {
		BlockCoverage coverage = blocks.get(id);
		if (coverage == null) {
			coverage = new BlockCoverage(id);
			blocks.put(id, coverage);
		}
		coverage.touch();
	}

	public void getBlock(UUID id) {
		blocks.get(id);
	}
	
	public Collection<BlockCoverage> getAllBlocks() {
		return blocks.values();
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
