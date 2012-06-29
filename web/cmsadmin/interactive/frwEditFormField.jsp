<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm,
                 com.tms.collab.formwizard.ui.AddFormField,
                 com.tms.collab.formwizard.ui.EditFormField"%>
<%@ include file="/common/header.jsp" %>



<x:config>
    <page name="editFormFieldPage">
    	<portlet name="editFormFieldPortlet" text="New Field" width="100%" permanent="true">
          <com.tms.collab.formwizard.ui.AddFormField name="editFormField"/>
        </portlet>
    </page>
</x:config>

<x:set name="editFormFieldPage.editFormFieldPortlet.editFormField" property="fieldFormAbsoluteName" value="addG2FieldFormPage.addG2FieldForm" />

<c:if test="${!empty param.formId}">
	<x:set name="editFormFieldPage.editFormFieldPortlet.editFormField" property = "formId" value = "${param.formId}"/>
</c:if>


<c:if test="${forward.name == 'formConfigAdded'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if(wm!=null){
	AddFormField form = (AddFormField)wm.getWidget("editFormFieldPage.editFormFieldPortlet.editFormField");
%>
window.open('frwEditPreviewForm.jsp?formId=<%=form.getFormId()%>','preview','scrollbars=yes,resizable=yes,width=700,height=500')
document.location = "<c:url value="frwEditFormField.jsp" />?formId=<%=form.getFormId()%>";
<%
}
%>
//-->         
</script>
</c:if>

<c:if test="${forward.name == 'formConfigFinish'}">
    <script>
        document.location= "<c:url value="frwFormsEdit.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == 'fieldNotAdded'}">
    <script>
        <%
             WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
             if(wm!=null){
                 AddFormField form = (AddFormField)wm.getWidget("editFormFieldPage.editFormFieldPortlet.editFormField");
         %>
        alert('Field Not Added');
        document.location= "frwEditForm.jsp"?formId=<%=form.getFormId()%>"";
        <%
             }
        %>
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
              
     <x:display name="editFormFieldPage.editFormFieldPortlet" ></x:display>
     
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
