package undercover.instrument;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import undercover.metric.MetricCollector;

public class Instrument {
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
		MetricCollector collector = new MetricCollector();
		ClassWriter classWriter = new ClassWriter(classReader, 0);
		classReader.accept(new InstrumentClass(classWriter, collector), 0);
		System.out.println(collector.toString());
		return classWriter;
	}
}
