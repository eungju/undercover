package undercover.report.html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;

import undercover.report.SourceItem;

public class SourceReport {
	private HtmlReport parent;
	public List<SourceLine> lines;
	public SourceItem sourceItem;
	
	public SourceReport(HtmlReport htmlReport, SourceItem sourceItem) {
		this.parent = htmlReport;
		this.sourceItem = sourceItem;
	}
	
	public void writeTo(File outputDir) throws IOException {
		StringTemplate st = parent.getTemplate("source_html");
		st.setAttribute("source", sourceItem);
		parent.writeTemplate(st, sourceItem.getLink());
	}
}
