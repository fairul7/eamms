<%@ page import="kacang.runtime.taglib.DisplayTag"%>
<%@ include file="/common/header.jsp" %>

<c-rt:set var="debugDurationKey" value="<%= DisplayTag.REQUEST_KEY_DEBUG_DURATION %>"/>
<c-rt:set var="debugWidgetKey" value="<%= DisplayTag.REQUEST_KEY_DEBUG_WIDGET %>"/>
<c-rt:set var="debugTemplateKey" value="<%= DisplayTag.REQUEST_KEY_DEBUG_WIDGET_TEMPLATE %>"/>

<a title="<c:out value="${requestScope[debugWidgetKey].absoluteName}"/>
<c:out value="${requestScope[debugWidgetKey].class.name}"/>
<c:out value="${requestScope[debugTemplateKey]}"/>" style="color:red"><c:out value="${requestScope[debugDurationKey]}"/>ms</a>
