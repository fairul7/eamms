<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="sortTemplateText">
    <com.tms.quotation.ui.SortTemplateText name="sortBox" width="100%"/>
  </page>
</x:config>
<x:set name="sortTemplateText.sortBox" property="templateId" value="${param.templateId }" />

<%-- Handle events --%>
<c:choose>
  <c:when test="${forward.name eq 'sort' }">
    <script language="javascript">
      alert("<fmt:message key='com.tms.quotation.sortTemplate.alert'/>");
      document.location = "<c:url value='viewTemplate.jsp'></c:url>";
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
			<x:display name="sortTemplateText.sortBox" />
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
