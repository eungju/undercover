package undercover.instrument;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import undercover.metric.MetaData;

public class Instrument {
	private final MetaData metaData;
	
	public Instrument() {
		metaData = new MetaData();
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
		classReader.accept(new InstrumentClassVisitor(classWriter, metaData), 0);
		return classWriter;
	}
}
