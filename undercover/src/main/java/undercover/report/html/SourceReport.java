package undercover.report.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.io.FileUtils;

public class SourceReport {
	private HtmlReport parent;
	private File file;
	public String path;
	public String name;
	public List<SourceLine> lines;
	
	public SourceReport(HtmlReport htmlReport, File file) {
		this.parent = htmlReport;
		this.file = file;
		path = sourceHtmlPath(htmlReport.relativeSourcePath(file));
		name = file.getName();
	}
	
	public void writeTo(File outputDir) throws IOException {
		StringTemplate st = parent.getTemplate("source_html");

		st.setAttribute("source", this);
		lines = new ArrayList<SourceLine>();
		int lineNumber = 1;
		for (String each : (List<String>) FileUtils.readLines(file, parent.sourceEncoding)) {
			lines.add(new SourceLine(lineNumber++, each));
		}
		
		parent.writeTemplate(st, path);
	}

	String sourceHtmlPath(String relativePath) {
		return relativePath.replaceAll("\\/|\\\\", ".") + ".html";
	}
}
