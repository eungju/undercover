package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassItem extends CompositeItem {
	public final SourceFile sourceFile;
	public final String simpleName;
	public final List<MethodItem> methods;
	
	public ClassItem(String name, SourceFile sourceFile) {
		super(name, name.replaceAll("/", "."));
		this.sourceFile = sourceFile;
		this.simpleName = name.substring(name.lastIndexOf('/') + 1);
		this.methods = new ArrayList<MethodItem>();
	}
	
	public String getLink() {
		return "source-" + sourceFile.path.replaceAll("/", ".") + ".html";
	}

	public void addMethod(MethodItem methodItem) {
		methods.add(methodItem);
	}
	
	protected Collection<Item> getItems() {
		return (Collection) methods;
	}
	
	public int getMethodCount() {
		return methods.size();
	}
}
