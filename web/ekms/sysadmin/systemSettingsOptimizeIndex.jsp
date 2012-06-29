<%@ page import="kacang.services.indexing.IndexingService,
                 kacang.Application,
                 com.tms.cms.core.model.ContentManager,
                 kacang.services.indexing.Index,
                 com.tms.cms.core.model.ContentPublisher"%>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.SystemSettings" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<%
    if (Boolean.valueOf(request.getParameter("optimizeIndex")).booleanValue()) {
        IndexingService indexer = (IndexingService)Application.getInstance().getService(IndexingService.class);
        Index pindex = indexer.getIndexWithName(ContentPublisher.INDEX_PUBLISHED);
        indexer.optimizeIndex(pindex);
        Index cindex = indexer.getIndexWithName(ContentManager.INDEX_CONTENT);
        indexer.optimizeIndex(cindex);
        response.sendRedirect(response.encodeRedirectURL("systemSettings.jsp"));
    }
%>
