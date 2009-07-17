package undercover.report.xml;

import java.io.PrintWriter;

import undercover.support.HtmlUtils;
import undercover.support.Proportion;

public class EmmaXmlWriter {
	private PrintWriter out;
	private int indentDepth;
	
	public EmmaXmlWriter(PrintWriter out) {
		this.out = out;
		indentDepth = 0;
	}
	
	public void close() {
		out.close();
	}

	private void indent() {
		for (int i = 0; i < indentDepth; i++) {
			out.print('\t');
		}
	}

	public EmmaXmlWriter coverage(String type, Proportion coverage) {
		indent();
		out.format("<coverage type=\"%s, %%\" value=\"%.0f%% (%d/%d)\" />",
				type, coverage.getRatio() * 100, coverage.part, coverage.whole).println();
		return this;
	}

	public EmmaXmlWriter method(String name) {
		indent();
		out.append("<method name=\"").append(HtmlUtils.escape(name)).append("\">").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endMethod() {
		indentDepth--;
		indent();
		out.append("</method>").println();
		return this;
	}

	public EmmaXmlWriter class_(String name) {
		indent();
		out.append("<class name=\"").append(HtmlUtils.escape(name)).append("\">").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endClass() {
		indentDepth--;
		indent();
		out.append("</class>").println();
		return this;
	}

	public EmmaXmlWriter sourceFile(String name) {
		indent();
		out.append("<srcfile name=\"").append(HtmlUtils.escape(name)).append("\">").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endSourceFile() {
		indentDepth--;
		indent();
		out.append("</srcfile>").println();
		return this;
	}

	public EmmaXmlWriter package_(String name) {
		indent();
		out.append("<package name=\"").append(HtmlUtils.escape(name)).append("\">").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endPackage() {
		indentDepth--;
		indent();
		out.append("</package>").println();
		return this;
	}

	public EmmaXmlWriter all() {
		indent();
		out.append("<all name=\"all classes\">").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endAll() {
		indentDepth--;
		indent();
		out.append("</all>").println();
		return this;
	}

	public EmmaXmlWriter data() {
		indent();
		out.append("<data>").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endData() {
		indentDepth--;
		indent();
		out.append("</data>").println();
		return this;
	}

	public EmmaXmlWriter stats() {
		indent();
		out.append("<stats>").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endStats() {
		indentDepth--;
		indent();
		out.append("</stats>").println();
		return this;
	}
	
	public EmmaXmlWriter statsPackages(int value) {
		indent();
		out.format("<packages value=\"%d\" />", value).println();
		return this;
	}

	public EmmaXmlWriter statsClasses(int value) {
		indent();
		out.format("<classes value=\"%d\" />", value).println();
		return this;
	}

	public EmmaXmlWriter statsMethods(int value) {
		indent();
		out.format("<methods value=\"%d\" />", value).println();
		return this;

	}

	public EmmaXmlWriter statsSourceFiles(int value) {
		indent();
		out.format("<srcfiles value=\"%d\" />", value).println();
		return this;
	}

	public EmmaXmlWriter statsSourceLines(int value) {
		indent();
		out.format("<srclines value=\"%d\" />", value).println();
		return this;
	}

	public EmmaXmlWriter report() {
		indent();
		out.append("<report>").println();
		indentDepth++;
		return this;
	}

	public EmmaXmlWriter endReport() {
		indentDepth--;
		indent();
		out.append("</report>").println();
		return this;
	}

	public EmmaXmlWriter document(String encoding) {
		out.format("<?xml version=\"1.0\" encoding=\"%s\" ?>", encoding).println();
		return this;
	}

	public EmmaXmlWriter endDocument() {
		return this;
	}
}
