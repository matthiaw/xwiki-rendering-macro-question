package org.xwiki.rendering.question;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.xwiki.bridge.event.DocumentCreatedEvent;
import org.xwiki.bridge.event.DocumentUpdatedEvent;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.context.Execution;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.query.QueryManager;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.MacroBlock;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

@Component
@Named("QuestionEventListener")
public class QuestionEventListener implements EventListener {

	@Inject
	private Logger logger;

	@Inject
	@Named("context")
	private Provider<ComponentManager> componentManagerProvider;

	@Inject
	private QueryManager queryManager;

	@Override
	public List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();
		events.add(new DocumentUpdatedEvent());
		events.add(new DocumentCreatedEvent());
		return events;
	}

	@Inject
	private Execution execution;

	@Override
	public String getName() {
		return "question";
	}

	@Override
	public void onEvent(Event event, Object source, Object data) {

		XWikiDocument doc = (XWikiDocument) source;
		XWikiContext context = (XWikiContext) data;
		
		// is document created or changed?
		if ((event instanceof DocumentUpdatedEvent) || (event instanceof DocumentCreatedEvent)) {

			EntityReference entRef = doc.resolveClassReference(Constants.SPACE+".QuestionClass");

			List<MacroBlock> macros = new ArrayList<MacroBlock>();
			getQuestionMacros(macros, doc.getXDOM().getRoot());

//			if (doc.getXObjects(entRef) == null) {				
//				doc.getXDOM().getRoot().addChild(new WordBlock("{{warning}}No Question defined.{{/warning}}"));
//				try {
//					context.getWiki().saveDocument(doc, context);
//				} catch (XWikiException e) {
//					e.printStackTrace();
//				}
//			}
			
			/**
			 * Delete the Question-Object when no Question-Macro is used
			 */
			if (doc.getXObjects(entRef) != null) {
				boolean onefound = false;
				for (BaseObject baseObject : doc.getXObjects(entRef)) {
					if (baseObject != null) {
						String label = baseObject.getStringValue("label");

						boolean found = false;
						for (MacroBlock macro : macros) {
							if (label.equals(macro.getParameter("label"))) {
								found = true;
							}
						}
						if (!found) {
							doc.removeXObject(baseObject);
							onefound = true;
						}
					}
				}
				if (onefound) {
					try {
						context.getWiki().saveDocument(doc, context);
					} catch (XWikiException e) {
						e.printStackTrace();
					}
				}
			}

			/**
			 * Create Question-Object when Question-Macro is set
			 */
			for (MacroBlock macro : macros) {
				boolean create = false;
				if (doc.getXObjects(entRef) != null) {
					boolean found = false;
					for (BaseObject baseObject : doc.getXObjects(entRef)) {
						if (baseObject != null) {
							String label = baseObject.getStringValue("label");
							if (label.equals(macro.getParameter("label"))) {
								found = true;
							}
						}
					}
					if (!found) {
						create = true;
					}
				} else {
					create = true;
				}

				if (create) {
					String space = doc.getDocumentReference().getLastSpaceReference().getName();
					String name = doc.getDocumentReference().getName();

					try {
						int objectIndex = doc.createXObject(entRef, context);
						BaseObject obj = doc.getXObjects(entRef).get(objectIndex);
						obj.set("prettyid", space + "."+name + "."+macro.getParameter("label") + " (" + obj.getGuid() + ")", context);
						obj.set("label", macro.getParameter("label"), context);
						context.getWiki().saveDocument(doc, context);
					} catch (XWikiException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void getQuestionMacros(List<MacroBlock> list, Block block) {
		if (block instanceof MacroBlock) {
			MacroBlock mb = (MacroBlock) block;
			if (mb.getId().equals("question")) {
				list.add(mb);
			}
		}

		List<Block> children = block.getChildren();
		for (Block cBlock : children) {
			getQuestionMacros(list, cBlock);
		}

	}

}
