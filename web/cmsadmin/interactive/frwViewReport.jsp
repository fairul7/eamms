<%
    if (request.getAttribute("exportedData") != null) {
        response.setContentType("application/vnd.ms-excel; charset=unicode");

        response.setHeader("Content-Disposition", "attachment; filename=formwizard.CSV" );
        out.print(request.getAttribute("exportedData"));
        return;
    }
%>
<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewReport"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="viewReportPage">
		<portlet name="viewReportPortlet" text="Form Report" width="100%" permanent="true">
    		<com.tms.collab.formwizard.ui.ViewReport name="viewReport"/>
		</portlet>
    </page>
</x:config>
<x:set name="viewReportPage.viewReportPortlet.viewReport" property="selected" value="${widgets['viewFormFieldsPage.viewFormFieldsPortlet.viewFormFields'].selectedFields}"/>
<x:set name="viewReportPage.viewReportPortlet.viewReport" property="formName" value="${widgets['viewFormFieldsPage.viewFormFieldsPortlet.viewFormFields'].formName}"/>

<c:if test="${!empty param.formId}">
    <x:set name="viewReportPage.viewReportPortlet.viewReport" property="formId" value="${param.formId}"/>
</c:if>


<c:if test="${!empty param.id}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewReport form = (ViewReport)wm.getWidget("viewReportPage.viewReportPortlet.viewReport");
%>
<c-rt:set var="formID" value="<%=form.getFormId()%>"/>
<c:redirect url="frwEditFormData.jsp?formId=${formID}&id=${param.id}" />
<%
}
%>
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



    <x:display name="viewReportPage.viewReportPortlet" ></x:display>

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



