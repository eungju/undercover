package undercover.instrument;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import undercover.instrument.synthetic.Exclusion;
import undercover.instrument.synthetic.ExclusionSet;
import undercover.metric.ClassMeta;
import undercover.metric.MetaData;

public class Instrument {
	static final String COVERAGE_FIELD_NAME = "$undercover$coverage";
	static final String PRE_CLINIT_METHOD_NAME = "$undercover$preClinit";
	private final Exclusion exclusion;
	private final MetaData metaData;
	private final ClassAnalyzer classAnalyzer;
	
	public Instrument() {
		exclusion = new ExclusionSet();
		metaData = new MetaData();
		classAnalyzer = new ClassAnalyzer(exclusion);
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
		if (!exclusion.exclude(classNode)) {
			ClassMeta classMeta = classAnalyzer.instrument(classNode);
			metaData.addClass(classMeta);
		}
		classNode.accept(classWriter);
		return classWriter;
	}
}
