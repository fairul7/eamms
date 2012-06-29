<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="imageSelector">
       <tabbedpanel name="tab1" width="100%">
         <c:if test="${!param.new}">
         <panel name="tree" text="<fmt:message key='general.label.related'/>">
           <com.tms.cms.image.ui.ImageSelectorList name="imageList" />
         </panel>
         </c:if>
         <panel name="list" text="<fmt:message key='general.label.other'/>">
           <com.tms.cms.image.ui.ImageSelectorTree name="imageTree" />
         </panel>
       </tabbedpanel>
    </page>
</x:config>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body>
   <x:display name="imageSelector"/>
</body>
</html>