<!--- viewQtnColumn.jsp --->
<%@ page import="com.tms.quotation.ui.ViewQtnColumn" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="viewQtnColumnPage">
    <%--<portlet name="viewQtnColumnPortlet" text="QtnColumn" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.ViewQtnColumn name="viewQtnColumn" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>

<c-rt:set var="forward_add" value="<%= ViewQtnColumn.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ViewQtnColumn.FORWARD_DELETE %>"/>
<c-rt:set var="forward_sort" value="<%= ViewQtnColumn.FORWARD_SORT %>"/>

<!--- permissions --->
<x:set name="viewQtnColumnPage.viewQtnColumn" property="canAdd" value="1"/>
<x:set name="viewQtnColumnPage.viewQtnColumn" property="canEdit" value="1"/>
<x:set name="viewQtnColumnPage.viewQtnColumn" property="canDelete" value="1"/>

<c:if test="${!empty forward }" >
  <c:set var="tableId" value="${widgets['viewQtnColumnPage.viewQtnColumn'].tableId }"/>
  <c:if test="${forward.name == forward_add}">
    <c:redirect url="addQtnColumn.jsp">
      <c:param name="tableId" value="${tableId}"/>
    </c:redirect>
  </c:if>
  <c:if test="${forward.name eq forward_sort }">
    <c:redirect url="sortQtnColumn.jsp">
      <c:param name="tableId" value="${tableId}"/>
    </c:redirect>
  </c:if>
</c:if>
<c:if test="${!empty param.columnId}">
  <c:redirect url="editQtnColumn.jsp?columnId=${param.columnId}"/>
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
        <font class="font_title"><fmt:message key='com.tms.quotation.table.column'/></font><br>
<%--    <hr size="1" color="cccccc">        --%>
      </td>
    </tr>        
    <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr style="background-color:#E9F5FF;">
          <td style="vertical-align: top;">
            <%--<x:display name="viewQtnColumnPage.viewQtnColumnPortlet"/>--%>
            <x:display name="viewQtnColumnPage.viewQtnColumn"/>
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
