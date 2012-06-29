<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ page pageEncoding="UTF-8" %>

<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bookmarkSelector">
         <com.tms.cms.core.ui.ContentListTable name="contentTable" customSelection="true" showPreview="true" sort="date" desc="true" />
    </page>
</x:config>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
<script language="javascript">
<!--
function select_url(url) {
        if (window.opener.document.forms['contentEdit.editContentPortlet.editContentPanel.containerForm'])
            targetBox = window.opener.document.forms['contentEdit.editContentPortlet.editContentPanel.containerForm'].elements['contentEdit.editContentPortlet.editContentPanel.containerForm.contentObjectForm.urlField'];
        else
            targetBox = window.opener.document.forms['contentAdd.addContentPanel.containerForm'].elements['contentAdd.addContentPanel.containerForm.contentObjectForm.urlField'];
            //targetBox = window.opener.document.forms['contentAdd.addContentPortlet.addContentPanel.containerForm'].elements['contentAdd.addContentPortlet.addContentPanel.containerForm.contentObjectForm.urlField'];

        // copy source to target
        <%
            String serverUrl = request.getServerName();
            if (request.getServerPort() != 80) {
                serverUrl += ":" + request.getServerPort();
            }
            serverUrl += request.getContextPath();
        %>
        targetBox.value = 'http://<%= serverUrl %>/cms/' + url;
        self.close();
}
//-->
</script>
</head>
<body class="contentBody">

<c:choose>
    <c:when test="${empty param.preview && !empty param.id}">
        <script>
        <!--
            select_url("content.jsp?id=<c:out value='${param.id}'/>");
        //-->
        </script>
    </c:when>
    <c:otherwise>

        <c:if test="${param.preview && !empty param.id}">
            <script>
            <!--
                window.open('<c:url value="/cmspreview/content.jsp"/>?id=<c:out value="${param.id}"/>', 'linkPreview');
            //-->
            </script>
        </c:if>

        <b>[<fmt:message key='general.label.content'/>]</b>
        <br><fmt:message key='general.label.linkToExistingContent'/>
        <x:display name="bookmarkSelector.contentTable" />

        <x:template name="selector" type="com.tms.cms.bookmark.ui.BookmarkSelector" body="custom">
        <form name="bookmarkSelector">

            <p>
            <hr size="1">
            <b>[<fmt:message key='general.label.formWizard'/>]</b>
            <br><fmt:message key='general.label.linkToAFormProcess'/>
            <select name="formSelect">
            <c:forEach var="proc" items="${selector.formList}">
                <option value="<c:out value='${proc.formId}'/>"><c:out value='${proc.formDisplayName}'/></option>
            </c:forEach>
            </select>
            <input type="button" class="button" onclick="select_url('form.jsp?formId=' + document.forms['bookmarkSelector'].elements['formSelect'].value)" value="<fmt:message key='general.label.ok'/>">

            <p>
            <hr size="1">
            <b>[<fmt:message key='general.label.forums'/>]</b>
            <br><fmt:message key='general.label.linkToAForum'/>
            <select name="forumSelect">
            <c:forEach var="forum" items="${selector.forumList}">
                <option value="<c:out value='${forum.id}'/>"><c:out value='${forum.name}'/></option>
            </c:forEach>
            </select>
            <input type="button" class="button" onclick="select_url('forumTopicList.jsp?forumId=' + document.forms['bookmarkSelector'].elements['forumSelect'].value)" value="<fmt:message key='general.label.ok'/>">
        </form>
        </x:template>


    </c:otherwise>
</c:choose>

</body>
</html>

