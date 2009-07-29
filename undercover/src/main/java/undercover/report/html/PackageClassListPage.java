package undercover.report.html;

import undercover.report.PackageItem;
import undercover.support.xml.Element;

public class PackageClassListPage extends ClassListPage {
	private PackageItem packageItem;

	public PackageClassListPage(PackageItem packageItem) {
		this.packageItem = packageItem;
	}
	
	public Element build() {
		return html().append(
				defaultHead("Classes of "),
				body().append(classList(packageItem.classes)
						)
				);
	}
}
