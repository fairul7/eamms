<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>


<c:if test="${!empty param.itemID}">
	<c:redirect url="editItem.jsp?id=${param.itemID}"/>
</c:if>
<script language="Javascript">
        function popupFormWindow_select() {
           window.opener.location="prepurchaseRequestForm.jsp";
           window.close();
        }
</script>

<c:if test="${forward.name eq 'insert'}">
	 <script language="Javascript">
	 	popupFormWindow_select();
    </script>
</c:if>

<x:config>
	<page name="popUp">
		<com.tms.sam.po.ui.PopUpItemSelect name="select"/>
	</page>
</x:config>

<head>
    <title><fmt:message key='general.label.popupFormWindow.pleaseSelect'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<table cellpadding="4" cellspacing="1" width="100%">
  <tr>
	   <td height="22" class="contentTitleFont" colspan="2">
			<fmt:message key="po.label.selectItem"/>
		</td>
 </tr>
  <tr>
    <td class="contentBgColor">
		<x:display name="popUp.select" />
	</td>
  </tr>
</table>
