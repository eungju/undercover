package undercover.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import undercover.report.html.SourceLine;

public class SourceItem extends CompositeItem {
	public final String language;
	public final File file;
	public final List<SourceLine> lines;
	public final List<ClassItem> classes;
	
	public SourceItem(String name, File file) {
		super(name, name);
		this.file = file;
		language = FilenameUtils.getExtension(name);
		lines = new ArrayList<SourceLine>();
		if (file != null) {
			try {
				int lineNumber = 1;
				for (String each : (List<String>) FileUtils.readLines(file, "UTF-8")) {
					lines.add(new SourceLine(lineNumber++, each));
				}
			} catch (IOException e) {
				//TODO: warning
			}
		}
		classes = new ArrayList<ClassItem>();
	}

	public String getLink() {
		return "source-" + name.replaceAll("/", ".") + ".html";
	}
	
	public void addClass(ClassItem classItem) {
		classes.add(classItem);
	}

	protected Collection<Item> getItems() {
		return (Collection) classes;
	}
}
