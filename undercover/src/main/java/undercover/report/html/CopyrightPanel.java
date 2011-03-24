package undercover.report.html;

import undercover.support.xml.Element;
import undercover.support.xml.Text;

import static undercover.report.html.HtmlElements.*;

public class CopyrightPanel implements HtmlFragment {
	public Element build() {
		return div().attr("class", "copyright").append(
				p().append(
						a().attr("href", "http://code.google.com/p/undercover/").attr("target", "_top").append("Undercover"),
						new Text(" | Copyright © 2009 Eungju PARK. All rights reserved. Licensed under the "),
						a().attr("href", "http://www.apache.org/licenses/LICENSE-2.0").attr("target", "_top").append("Apache License, Version 2.0")
						)
				);
	}
}
