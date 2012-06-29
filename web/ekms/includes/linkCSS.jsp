<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<head>
	<title><fmt:message key="general.label.ekp"/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="/ekms/images/fms2008/default.css" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">    
</head>