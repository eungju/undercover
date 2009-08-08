package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.support.xml.Element;

public abstract class ClassListPage extends HtmlPage {
	protected Element classList(Collection<ClassItem> classes) {
		return table().attr("class", "item-children").append(
				colgroup().append(
						col().attr("width", "*"),
						col().attr("width", "80")
						),
				thead().append(
						tr().append(
								th().append(text("Class")),
								th().append(text("Coverage"))
								)
						),
				classListBody(classes)
				);
	}

	protected Element classListBody(Collection<ClassItem> classes) {
		Element result = tbody();
		for (ClassItem each : classes) {
			result.append(
					tr().append(
							td().append(a().attr("href", "source-" + each.getSource().getLinkName() + ".html").attr("target", "classPane").append(text(each.getSimpleName()))),
							td().attr("class", "coverage").append(coveragePercent(each))
							)
					);
		}
		return result;
	}
}
