<!--- viewTemplate.jsp --->
<%@ page import="com.tms.quotation.ui.ViewTemplate" %>

<%@ include file="/common/header.jsp" %>
<%-- <x:permission permission="" module="" /> --%>
<x:config>
  <page name="viewTemplatePage">
    <%--<portlet name="viewTemplatePortlet" text="Template" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.ViewTemplate name="viewTemplate" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>

<c-rt:set var="forward_add" value="<%= ViewTemplate.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ViewTemplate.FORWARD_DELETE %>"/>

<!--- permissions --->
<x:permission permission="com.tms.quotation.templates" module="com.tms.quotation.model.QuotationModule">
  <x:set name="viewTemplatePage.viewTemplate" property="canAdd" value="1"/>
  <x:set name="viewTemplatePage.viewTemplate" property="canEdit" value="1"/>
  <x:set name="viewTemplatePage.viewTemplate" property="canDelete" value="1"/>
  <x:set name="viewTemplatePage.viewTemplate" property="canActivate" value="1"/>
</x:permission>

<c:if test="${forward.name == forward_add}">
  <c:redirect url="addTemplate.jsp"/>
</c:if>
<c:if test="${!empty param.templateId}">
  <c:redirect url="editTemplate.jsp?id=${param.templateId}"/>
</c:if>

<%--<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />--%>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
<tbody>
<tr>
  <%--<td style="vertical-align: top; width: 250px;">
    <jsp:include page="/cmsadmin/includes/sideContentModules.jsp" flush="true" />
  </td>--%>
  <td style="vertical-align: top;">
    <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
      <tr>
        <td style="vertical-align: top;">
          <table width="100%" border="0" cellspacing="0" cellpadding="2" class="darkgreen">
            <tr>
              <td class="contentTitleFont" style="vertical-align: top;">
              <font class="font_title"><fmt:message key='com.tms.quotation.table.template'/></font><br>
<%--    <hr size="1" color="cccccc"> --%>
              </td>
           </tr>
           <tr style="background-color:#E9F5FF;">
             <td style="vertical-align: top;">
            <%--<x:display name="viewTemplatePage.viewTemplatePortlet"/>--%>
              <x:display name="viewTemplatePage.viewTemplate"/>
             </td>
           </tr>
        </table>
      </td>
    </tr>
    </tbody>
    </table>
  </td>
</tr>
</tbody>
</table>

<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
