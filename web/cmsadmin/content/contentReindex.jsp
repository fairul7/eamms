<%@ page import="com.tms.cms.core.model.ContentManager,
                 kacang.Application,
                 kacang.ui.WidgetManager,
				 com.tms.cms.core.model.ContentPublisher"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%
    ContentManager cm = (ContentManager)Application.getInstance().getModule(ContentManager.class);
    cm.reindexTree(ContentManager.CONTENT_TREE_ROOT_ID, WidgetManager.getWidgetManager(request).getUser());

	ContentPublisher cp = (ContentPublisher) Application.getInstance().getModule(ContentPublisher.class);
	cp.reindexTree(ContentManager.CONTENT_TREE_ROOT_ID, WidgetManager.getWidgetManager(request).getUser());
%>

<fmt:message key='general.message.contentReindexOK'/>