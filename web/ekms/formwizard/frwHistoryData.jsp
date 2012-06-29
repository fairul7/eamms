<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewHistoryData"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsViewHistoryDataPage">
    		<com.tms.collab.formwizard.ui.ViewHistoryData name="ekmsViewHistoryData"/>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <x:set name="ekmsViewHistoryDataPage.ekmsViewHistoryData" property="id" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="ekmsViewHistoryDataPage.ekmsViewHistoryData" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${forward.name == 'cancel'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewHistoryData form = (ViewHistoryData)wm.getWidget("ekmsViewHistoryDataPage.ekmsViewHistoryData");
%>
    <c-rt:set var="formId" value="<%=form.getFormId()%>"/>
    <c:redirect url="frwViewHistoryReport.jsp?formId=${formId}" />
<%
}
%>
</c:if>

<c:if test="${forward.name == 'print'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewHistoryData form = (ViewHistoryData)wm.getWidget("ekmsViewHistoryDataPage.ekmsViewHistoryData");
%>
    myWin = window.open('frwPrintForm.jsp?formUid=<%= form.getId() %>&formId=<%=form.getFormId()%>&hidden=1');
<%
}
%>
//-->
</script>
</c:if>




<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='formWizard.label.submittedFormHistory'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsViewHistoryDataPage.ekmsViewHistoryData" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>