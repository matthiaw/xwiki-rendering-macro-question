package org.xwiki.rendering.question;

import java.lang.reflect.Type;

import org.xwiki.component.wiki.WikiComponentScope;
import org.xwiki.model.reference.DocumentReference;

import com.xpn.xwiki.doc.XWikiDocument;

public class DefaultQuestion implements Question {

	private String roleHint;
	private XWikiDocument xwikiDocument;
	private DocumentReference documentReference;
	private DocumentReference authorReference;

	public XWikiDocument getDocument() {
		return xwikiDocument;
	}

	public void setDocument(XWikiDocument xwikiDocument) {
		this.xwikiDocument = xwikiDocument;
	}

	public DocumentReference getDocumentReference() {
		return documentReference;
	}

	public DocumentReference getAuthorReference() {
		return authorReference;
	}

	public void setRoleHint(String roleHint) {
		this.roleHint = roleHint;
	}

	public void setDocumentReference(DocumentReference documentReference) {
		this.documentReference = documentReference;
	}

	public void setAuthorReference(DocumentReference authorReference) {
		this.authorReference = authorReference;
	}

	public String getRoleHint() {
		return roleHint;
	}

	public WikiComponentScope getScope() {
		return WikiComponentScope.WIKI;
	}

	private String label;

	public DefaultQuestion() {
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String guid;

	public void setGuid(String id) {
		this.guid = id;
	}

	private String prettyId;

	@Override
	public String getPrettyId() {
		return prettyId;
	}

	public void setPrettyId(String prettyId) {
		this.prettyId = prettyId;
	}

	@Override
	public String getGuid() {
		return guid;
	}

	@Override
	public Type getRoleType() {
		return Question.class;
	}

}
