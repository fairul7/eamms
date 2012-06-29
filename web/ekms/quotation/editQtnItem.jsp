<%@ page import="com.tms.quotation.ui.QtnItemForm" %>

<%@ include file="/common/header.jsp" %>
<x:config>
  <page name="itemEntry">
    <com.tms.quotation.ui.QtnItemForm name="itemForm" width="100%" />
  </page>
</x:config>

<c:if test="${!empty param.quotationId}">
  <x:set name="itemEntry.itemForm" property="quotationId" value="${param.quotationId}"/>
</c:if>
<c:if test="${!empty param.tableId }">
  <x:set name="itemEntry.itemForm" property="tableId" value="${param.tableId}" />
</c:if>
<c:if test="${!empty param.itemId }">
  <x:set name="itemEntry.itemForm" property="itemId" value="${param.itemId }" />
</c:if>
<x:set name="itemEntry.itemForm" property="type" value="Edit" />

<%-- Handle events --%>
<c-rt:set var="forward_succ" value="<%= QtnItemForm.QTNITEM_ADD_SUCCESS %>" />
<c-rt:set var="forward_fail" value="<%= QtnItemForm.QTNITEM_ADD_FAIL %>" />
<c-rt:set var="forward_exit" value="<%= QtnItemForm.QTNITEM_ADD_EXIT %>" />

<c:if test="${!empty forward }">
  <c:set var="quotnId" value="${widgets['itemEntry.itemForm'].quotationId }"/>
  <c:set var="tableId" value="${widgets['itemEntry.itemForm'].tableId }"/>
</c:if>
<c:choose>
  <c:when test="${forward.name eq forward_exit}">
    <script>
      alert("<fmt:message key='com.tms.quotation.item.alert.updated'/>");
      document.location="<c:url value='previewQuotation.jsp'><c:param name='quotationId' value='${quotnId}'/></c:url>";
    </script>
  </c:when>
  <c:when test="${forward.name eq forward_succ }">
    <script>
      alert("<fmt:message key='com.tms.quotation.item.alert.updated'/>");
      document.location="<c:url value='qtnItemForm.jsp'><c:param name='quotationId' value='${quotnId}'/><c:param name='tableId' value='${tableId}'/></c:url>";
    </script>
  </c:when>
  <c:when test="${forward.name eq forward_fail}">
      <script>
      alert("<fmt:message key='com.tms.quotation.item.alert.error'/>");
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
    <tr><td><c:out value="${idTable }"/></td></tr>
    <tr>
      <td style="vertical-align: top;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
<%--
        <c:if test="${!empty testMsg}" >
        <tr><td><c:out value="${testMsg}"/></td></tr>
        </c:if>
--%>
        <tr>
          <td style="vertical-align: top;">
            <x:display name="itemEntry.itemForm"/>
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
<%--
<c:if test="${forward.name eq QtnItem.add }" >
<table border="1">
<tr>
    <c:set var="rowData" value="${widgets['itemEntry.itemForm'].rowString}" />
	<c:out value="${rowData}" escapeXml="false" />
</tr>
</c:if>
--%>
<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
