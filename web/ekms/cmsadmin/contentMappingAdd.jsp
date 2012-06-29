<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />

<x:config>
<page name="contentMappingAdd">
        <com.tms.cms.taxonomy.ui.TaxonomyMappingAdd name="tree"/>
</page>
</x:config>           


<c:if test="${!empty param.id}">
<x:set name="contentMappingAdd.tree" property="contentId" value="${param.id}"/>
</c:if>

<c:if test="${forward.name=='added'}">
<script>
    alert("<fmt:message key="message.mapping.added"/>");
    window.opener.location="contentMapping.jsp";
    window.close();
</script>
</c:if>

<c:if test="${forward.name=='cancel'}">
<script>
    window.close();
</script>
</c:if>

<html>
<head>
    <title><fmt:message key="general.label.taxonomy"/></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/ekms/images/ekp2005/default.css">
</head>
<body  class="forumBackground" >

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
        <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>
                <%--Content Path--%>
                <x:display name="cms.contentPath"/> <p>

                  <%--Content Details--%>
                  <x:display name="contentMappingAdd.tree"/>
                  <br>
                  <br>
                  <br>

                  <br>
                  <br>
                  <br>


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
