<%@ include file="/common/header.jsp" %>
<x:config>
  <page name="setupQtnCol">
	<com.tms.quotation.ui.QtnColumnForm name="form" width="100%" />
  </page>
</x:config>

<html>
<head>
    <link rel="stylesheet" href="/ekms/images/ekp2005/default.css">
</head>
<body>
<%-- 
<%@ include file="/ekms/includes/header.jsp" %>
--%>

<jsp:include page="includes/header.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
<tbody>
<c:if test="${!empty param.tableId }">
<tr><td><c:out value="${param.tableId }"/></td></tr>
<tr><td><c:out value="${param.numOfCols }"/></td></tr>
</c:if>
<tr>
  <td style="vertical-align: top;">
    <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
      <tr>
        <td style="vertical-align: top;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
          <tr>
            <td style="vertical-align: top;">
              <x:display name="setupQtnCol.form"/>
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

</body>
</html>

<%-- 
<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
--%>