<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.EditFormData,
                 com.tms.collab.formwizard.ui.DynamicEditFormDataField"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsEditFormDataPage">
	          <com.tms.collab.formwizard.ui.EditFormData name="ekmsEditFormData"/>
              <com.tms.collab.formwizard.ui.DynamicEditFormDataField name="ekmsDynamicEditFormDataField"/>
    </page>
</x:config>

<c:if test="${!empty param.id}">
    <x:set name="ekmsEditFormDataPage.ekmsDynamicEditFormDataField" property="id" value="${param.id}"/>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="ekmsEditFormDataPage.ekmsDynamicEditFormDataField" property="formId" value="${param.formId}"/>
    <x:set name="ekmsEditFormDataPage.ekmsEditFormData" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${forward.name == 'dataSaved' || forward.name == 'cancel'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	EditFormData form = (EditFormData)wm.getWidget("ekmsEditFormDataPage.ekmsEditFormData");
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
	DynamicEditFormDataField form = (DynamicEditFormDataField)wm.getWidget("ekmsEditFormDataPage.ekmsDynamicEditFormDataField");
%>
    myWin = window.open('frwPrintForm.jsp?formUid=<%= form.getId() %>&formId=<%=form.getFormId()%>');
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
      Edit Data
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <x:display name="ekmsEditFormDataPage.ekmsEditFormData" ></x:display>
            <x:display name="ekmsEditFormDataPage.ekmsDynamicEditFormDataField" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
