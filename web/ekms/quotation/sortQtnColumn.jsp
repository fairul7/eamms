<%@ page import="com.tms.quotation.ui.SortQtnColumn" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="sortColumnPage">
    <com.tms.quotation.ui.SortQtnColumn name="sortQtnColumn" width="100%"/>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= SortQtnColumn.SORT_COLUMN_SUCCESS %>"/>
<x:set name="sortColumnPage.sortQtnColumn" property="tableId" value="${param.tableId }" />

<%-- Handle events --%>
<c:choose>
  <c:when test="${forward.name eq forward_succ}">
    <c:set var="tableId" value="${widgets['sortColumnPage.sortQtnColumn'].tableId }"/>
    <script language="javascript">
      alert("<fmt:message key='com.tms.quotation.sortColumn.alert'/>");
      document.location = "<c:url value='viewQtnColumn.jsp'><c:param name='tableId' value='${tableId}'/></c:url>";
    </script>
  </c:when>
</c:choose>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
<tbody>
<tr>
  <td style="vertical-align: top;">
    <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
    <tr>
      <td style="vertical-align: top;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr>
          <td style="vertical-align: top;">
			<x:display name="sortColumnPage.sortQtnColumn" />
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
