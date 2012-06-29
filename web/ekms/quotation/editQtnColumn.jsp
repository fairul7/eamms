<!--- editQtnColumn.jsp --->
<%@ page import="kacang.ui.WidgetManager, com.tms.quotation.ui.AddQtnColumn" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="editQtnColumnPage">
    <%--<portlet name="editQtnColumnPortlet" text="Edit QtnColumn" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.AddQtnColumn name="editQtnColumn" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= AddQtnColumn.QTNCOLUMN_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= AddQtnColumn.QTNCOLUMN_ADD_FAIL %>"/>
<c-rt:set var="forward_exists" value="<%= AddQtnColumn.QTNCOLUMN_ADD_EXISTS %>"/>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.columnId)}">
    <c:set var="columnId" value="${param.columnId}"/>
  </c:when>
  <c:otherwise>
    <%--<c:set var="columnId" value="${widgets['editQtnColumnPage.editQtnColumnPortlet.editQtnColumn'].columnId}"/>--%>
    <c:set var="columnId" value="${widgets['editQtnColumnPage.editQtnColumn'].columnId}"/>
  </c:otherwise>
</c:choose>
<%--<x:set name="editQtnColumnPage.editQtnColumnPortlet.editQtnColumn" property="columnId" value="${columnId}"/>
<x:set name="editQtnColumnPage.editQtnColumnPortlet.editQtnColumn" property="type" value="${type}"/>--%>
<x:set name="editQtnColumnPage.editQtnColumn" property="columnId" value="${columnId}"/>
<x:set name="editQtnColumnPage.editQtnColumn" property="type" value="${type}"/>

<x:set name="editQtnColumnPage.editQtnColumn" property="whoModified" value="${sessionScope.currentUser.username}"/>
<!--- permissions --->
<x:set name="editQtnColumnPage.editQtnColumn" property="canAdd" value="1"/>
<x:set name="editQtnColumnPage.editQtnColumn" property="canEdit" value="1"/>
<x:set name="editQtnColumnPage.editQtnColumn" property="canDelete" value="1"/>

<c:choose>
  <c:when test="${forward.name == forward_succ}">
  <c:set var="tableId" value="${widgets['editQtnColumnPage.editQtnColumn'].tableId }"/>
    <script>
      alert("<fmt:message key='com.tms.quotation.column.alert.updated'/>");
      document.location = "<c:url value='viewQtnColumn.jsp'><c:param name='tableId' value='${tableId}'/></c:url>";
    </script>
  </c:when>
  <c:when test="${forward.name == forward_exists}">
    <script>alert("<fmt:message key='com.tms.quotation.column.alert.exists'/>");</script>
  </c:when>
  <c:when test="${forward.name == forward_fail}">
    <script>alert("<fmt:message key='com.tms.quotation.column.alert.error'/>");</script>
  </c:when>
</c:choose>

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
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr>
          <td style="vertical-align: top;">
            <%--<x:display name="editQtnColumnPage.editQtnColumnPortlet"/>--%>
            <x:display name="editQtnColumnPage.editQtnColumn"/>
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
