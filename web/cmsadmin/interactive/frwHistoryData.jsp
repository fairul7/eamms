<%@ page import="com.tms.collab.formwizard.ui.ViewHistoryData" %>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="viewHistoryDataPage">
		<portlet name="viewHistoryDataPortlet" text="Form History" width="100%" permanent="true">
    		<com.tms.collab.formwizard.ui.ViewHistoryData name="viewHistoryData"/>
		</portlet>
    </page>
</x:config>

<c:if test="${!empty param.formUid}">
    <x:set name="viewHistoryDataPage.viewHistoryDataPortlet.viewHistoryData" property="id" value="${param.formUid}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="viewHistoryDataPage.viewHistoryDataPortlet.viewHistoryData" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${forward.name == 'cancel'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewHistoryData form = (ViewHistoryData)wm.getWidget("viewHistoryDataPage.viewHistoryDataPortlet.viewHistoryData");
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
	ViewHistoryData form = (ViewHistoryData)wm.getWidget("viewHistoryDataPage.viewHistoryDataPortlet.viewHistoryData");
%>    
    myWin = window.open('frwPrintForm.jsp?formUid=<%= form.getId() %>&formId=<%=form.getFormId()%>&showHidden=0');
<%
}
%>
//-->
</script>
</c:if>

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideInteractiveFW.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>


             <x:display name="viewHistoryDataPage.viewHistoryDataPortlet" ></x:display>


                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
