<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.EditFormData,
                 com.tms.collab.formwizard.ui.DynamicEditFormDataField"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="editFormDataPage">
	     <portlet name="editFormDataPortlet" text="Edit Data" width="100%" permanent="true">
	          <com.tms.collab.formwizard.ui.EditFormData name="editFormData"/>
              <com.tms.collab.formwizard.ui.DynamicEditFormDataField name="dynamicEditFormDataField"/>
	    </portlet>
    </page>
</x:config>

<c:if test="${!empty param.id}">
    <x:set name="editFormDataPage.editFormDataPortlet.dynamicEditFormDataField" property="id" value="${param.id}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="editFormDataPage.editFormDataPortlet.editFormData" property="formId" value="${param.formId}"/>
    <x:set name="editFormDataPage.editFormDataPortlet.dynamicEditFormDataField" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${forward.name == 'dataSaved' || forward.name == 'cancel'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditFormData form = (EditFormData)wm.getWidget("editFormDataPage.editFormDataPortlet.editFormData");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="frwViewReport.jsp?formId=${formId}" />
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
	DynamicEditFormDataField form = (DynamicEditFormDataField)wm.getWidget("editFormDataPage.editFormDataPortlet.dynamicEditFormDataField");
%>
    myWin = window.open('frwPrintForm.jsp?formUid=<%= form.getId() %>&formId=<%=form.getFormId()%>&showHidden=1');
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



    <x:display name="editFormDataPage.editFormDataPortlet" ></x:display>

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



