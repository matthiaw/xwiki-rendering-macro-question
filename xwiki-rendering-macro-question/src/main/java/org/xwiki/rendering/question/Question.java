package org.xwiki.rendering.question;

import org.xwiki.component.wiki.WikiComponent;

import com.xpn.xwiki.doc.XWikiDocument;

/**
 * Load them with
 * {{velocity}}
$xwiki.ssfx.use("js/xwiki/table/table.css")
$xwiki.jsfx.use("js/xwiki/table/tablefilterNsort.js", true)
{{/velocity}}

{{groovy}}
   import org.xwiki.rendering.question.Question;
   println("(% class=\"grid sortable filterable doOddEven\" id=\"tableid\" %)")
   println("(% class=\"sortHeader\" %)|=Question|=Document")
   for (Question q: services.component.getComponentManager().getInstanceList(Question.class)) {
      println("|"+q.getLabel()+"|[["+q.getDocument()+"]]")
   }
{{/groovy}}
 * 
 * @version $Id$
 */

public interface Question extends WikiComponent {

	public static final String CLASS = "Question";

	public String getPrettyId();

	public String getGuid();

	public String getLabel();

	public XWikiDocument getDocument();
	
}
