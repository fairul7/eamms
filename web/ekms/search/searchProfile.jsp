<%@ page import="com.tms.ekms.search.ui.SearchProfileTable"%>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<x:config>
    <page name="searchProfile">
        <com.tms.ekms.search.ui.SearchProfileTable name="table"/>
    </page>
</x:config>

<c:if test="${!(empty param.profileId)}">
    <script>
        document.location="searchProfileView.jsp?profileId=<c:out value="${param.profileId}"/>";
    </script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<body>
  <table cellpadding="3" cellspacing="0" width="100%">
      <tr><td class="contentTitleFont"><span class="contentTitleFont"><fmt:message key="searchprofile.label.title"/></span></td></tr>
      <tr><td class="contentBody" align="center"><x:display name="searchProfile.table" /></td></tr>
    </table>
  </body>
<%@ include file="/ekms/includes/footer.jsp" %>