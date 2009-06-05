package undercover.report;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ReportData {
	private final ProjectItem projectItem;
	private final SortedMap<String, ClassItem> classItems;
	private final SortedMap<String, SourceItem> sourceItems;
	
	public ReportData(ProjectItem projectItem, Map<String, ClassItem> classItems, Map<String, SourceItem> sourceItems) {
		this.projectItem = projectItem;
		this.classItems = new TreeMap<String, ClassItem>(ClassItem.NAME_ORDER_BY_SIMPLE_NAME);
		this.classItems.putAll(classItems);
		this.sourceItems = new TreeMap<String, SourceItem>(sourceItems);
	}
	
	public ProjectItem getProject() {
		return projectItem;
	}

	public Collection<ClassItem> getAllClasses() {
		return classItems.values();
	}
	
	public Collection<SourceItem> getAllSources() {
		return sourceItems.values();
	}
}
