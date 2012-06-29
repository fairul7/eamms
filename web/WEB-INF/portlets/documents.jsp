<%@ page import="com.tms.cms.portlet.DocumentPortlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<x:config>
    <page name="documentPortlet">
        <com.tms.cms.portlet.DocumentPortlet name="portlet"/>
    </page>
</x:config>
<crt:set var="forward_content" value="<%= DocumentPortlet.FORWARD_CLICK %>"/>
<c:if test="${forward.name == forward_content}">
    <script>document.location="<c:url value="/ekms/content/"/>content.jsp?id=<c:out value="${param.id}"/>";</script>
</c:if>
<x:display name="documentPortlet.portlet"/>
