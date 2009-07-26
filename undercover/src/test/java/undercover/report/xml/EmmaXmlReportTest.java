package undercover.report.xml;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceFile;
import undercover.report.SourceItem;
import undercover.support.Proportion;

public class EmmaXmlReportTest {
	private EmmaXmlReport dut;
	private EmmaXmlWriter writer;

	@Before public void beforeEach() {
		writer = mock(EmmaXmlWriter.class);
		dut = new EmmaXmlReport(null);
		dut.writer = writer;

		when(writer.document()).thenReturn(writer);
		when(writer.endDocument()).thenReturn(writer);
		when(writer.report()).thenReturn(writer);
		when(writer.endReport()).thenReturn(writer);
		when(writer.stats()).thenReturn(writer);
		when(writer.statsPackages(anyInt())).thenReturn(writer);
		when(writer.statsClasses(anyInt())).thenReturn(writer);
		when(writer.statsMethods(anyInt())).thenReturn(writer);
		when(writer.statsSourceFiles(anyInt())).thenReturn(writer);
		when(writer.statsSourceLines(anyInt())).thenReturn(writer);
		when(writer.data()).thenReturn(writer);
		when(writer.all()).thenReturn(writer);
		when(writer.endAll()).thenReturn(writer);
		when(writer.package_(anyString())).thenReturn(writer);
		when(writer.sourceFile(anyString())).thenReturn(writer);
		when(writer.class_(anyString())).thenReturn(writer);
		when(writer.method(anyString())).thenReturn(writer);
		when(writer.coverage(anyString(), any(Proportion.class))).thenReturn(writer);
	}

	@Test public void writeReport() {
		ReportData item = new ReportData("cool product", Collections.<PackageItem>emptyList(), Collections.<ClassItem>emptyList(), Collections.<SourceItem>emptyList());
		dut.writeReport(item);
		
		verify(writer).document();
		verify(writer).report();
		verify(writer).data();
		verify(writer).all();
		verify(writer).coverage("class", new Proportion(0, 0));
		verify(writer).coverage("method", new Proportion(0, 0));
		verify(writer).coverage("block", new Proportion(0, 0));
		verify(writer).endAll();
		verify(writer).endData();
		verify(writer).endReport();
		verify(writer).endDocument();
	}

	@Test public void writePackage() {
		PackageItem item = new PackageItem("p");
		dut.writePackage(item);
		
		verify(writer).package_(item.getDisplayName());
		verify(writer).coverage("class", new Proportion(0, 0));
		verify(writer).coverage("method", new Proportion(0, 0));
		verify(writer).coverage("block", new Proportion(0, 0));
		verify(writer).endPackage();
	}

	@Test public void writeSourceFile() {
		SourceItem item = new SourceItem(new SourceFile("p/c.java"));
		dut.writeSource(item);
		
		verify(writer).sourceFile(item.getSimpleName());
		verify(writer).coverage("class", new Proportion(0, 0));
		verify(writer).coverage("method", new Proportion(0, 0));
		verify(writer).coverage("block", new Proportion(0, 0));
		verify(writer).endSourceFile();
	}

	@Test public void writeClass() {
		ClassItem item = new ClassItem("p/c");
		dut.writeClass(item);
		
		verify(writer).class_(item.getSimpleName());
		verify(writer).coverage("class", new Proportion(0, 1));
		verify(writer).coverage("method", new Proportion(0, 0));
		verify(writer).coverage("block", new Proportion(0, 0));
		verify(writer).endClass();
	}
	
	@Test public void writeMethod() {
		MethodItem item = new MethodItem("m()V", 1, 2, 0);
		dut.writeMethod(item);
		
		verify(writer).method("m() : void");
		verify(writer).coverage("method", new Proportion(0, 1));
		verify(writer).coverage("block", new Proportion(0, 2));
		verify(writer).endMethod();
	}
}
