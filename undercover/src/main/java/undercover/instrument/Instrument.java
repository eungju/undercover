package undercover.instrument;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import undercover.data.ClassMeta;
import undercover.data.MetaData;
import undercover.instrument.filter.ExclusionSet;
import undercover.instrument.filter.GlobExclusion;
import undercover.instrument.filter.GlobFilter;

public class Instrument {
	static final String BLOCK_COVERAGE_FIELD_NAME = "$undercover$blockCoverage";
	private final ExclusionSet exclusionSet;
	private final MetaData metaData;
	private final ClassAnalyzer classAnalyzer;
	
	public Instrument(GlobFilter filter) {
		exclusionSet = ExclusionSet.withDefault();
		exclusionSet.add(new GlobExclusion(filter));
		metaData = new MetaData();
		classAnalyzer = new ClassAnalyzer(exclusionSet);
	}
	
	public MetaData getMetaData() {
		return metaData;
	}
	
	public byte[] instrument(byte[] bytecode) {
		ClassReader reader = new ClassReader(bytecode);
		ClassWriter writer = instrument(reader);
		return writer.toByteArray();
	}
	
	public byte[] instrument(InputStream bytecode) throws IOException {
		ClassReader reader = new ClassReader(bytecode);
		ClassWriter writer = instrument(reader);
		return writer.toByteArray();
	}
	
	public ClassWriter instrument(ClassReader classReader) {
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		if (!exclusionSet.exclude(classNode)) {
			ClassMeta classMeta = classAnalyzer.instrument(classNode);
			metaData.addClass(classMeta);
		}
		classNode.accept(classWriter);
		return classWriter;
	}
}
