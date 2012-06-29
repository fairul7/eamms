<!--- viewQtnTable.jsp --->
<%@ page import="com.tms.quotation.ui.ViewQtnTable" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="viewQtnTablePage">
    <%--<portlet name="viewQtnTablePortlet" text="QtnTable" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.ViewQtnTable name="viewQtnTable" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>

<c-rt:set var="forward_add" value="<%= ViewQtnTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ViewQtnTable.FORWARD_DELETE %>"/>

<!--- permissions --->
<x:permission permission="com.tms.quotation.templates" module="com.tms.quotation.model.QuotationModule">
  <x:set name="viewQtnTablePage.viewQtnTable" property="canAdd" value="1"/>
  <x:set name="viewQtnTablePage.viewQtnTable" property="canEdit" value="1"/>
  <x:set name="viewQtnTablePage.viewQtnTable" property="canDelete" value="1"/>
  <x:set name="viewQtnTablePage.viewQtnTable" property="canActivate" value="1"/>
</x:permission>
<x:set name="viewQtnTablePage.viewQtnTable" property="colEditUrl" value="viewQtnColumn.jsp"/>

<c:if test="${forward.name == forward_add}">
  <c:redirect url="addQtnTable.jsp"/>
</c:if>
<c:if test="${!empty param.tableId}">
  <c:redirect url="editQtnTable.jsp">
    <c:param name="tableId" value="${param.tableId}"/>
  </c:redirect>
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
      <td class="contentTitleFont" style="vertical-align: top;">
        <font class="font_title"><fmt:message key='com.tms.quotation.table.qtnTable'/></font><br>
<%--    <hr size="1" color="cccccc"> --%>
      </td>
    </tr>
      <tr>
      <td style="vertical-align: top;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr style="background-color:#E9F5FF;">
          <td style="vertical-align: top;">
            <%--<x:display name="viewQtnTablePage.viewQtnTablePortlet"/>--%>
            <x:display name="viewQtnTablePage.viewQtnTable"/>
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
