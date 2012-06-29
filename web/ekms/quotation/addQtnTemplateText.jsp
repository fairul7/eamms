<!--- addQtnTemplateText.jsp --->
<%@ page import="kacang.ui.WidgetManager, com.tms.quotation.ui.AddQtnTemplateText" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="addQtnTemplateTextPage">
    <%--<portlet name="addQtnTemplateTextPortlet" text="Add QtnTemplateText" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.AddQtnTemplateText name="addQtnTemplateText" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= AddQtnTemplateText.QTNTEMPLATETEXT_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= AddQtnTemplateText.QTNTEMPLATETEXT_ADD_FAIL %>"/>
<c-rt:set var="forward_exists" value="<%= AddQtnTemplateText.QTNTEMPLATETEXT_ADD_EXISTS %>"/>

<x:set name="addQtnTemplateTextPage.addQtnTemplateText" property="whoModified" value="${sessionScope.currentUser.username}"/>
<!--- permissions --->
<x:set name="addQtnTemplateTextPage.addQtnTemplateText" property="canAdd" value="1"/>
<x:set name="addQtnTemplateTextPage.addQtnTemplateText" property="canEdit" value="1"/>
<x:set name="addQtnTemplateTextPage.addQtnTemplateText" property="canDelete" value="1"/>

<c:choose>
  <c:when test="${forward.name == forward_succ}">
    <script>alert("<fmt:message key='com.tms.quotation.templateText.alert.added'/>"); document.location = "<c:url value="viewQtnTemplateText.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == forward_exists}">
    <script>alert("<fmt:message key='com.tms.quotation.templateText.alert.exists'/>");</script>
  </c:when>
  <c:when test="${forward.name == forward_fail}">
    <script>alert("<fmt:message key='com.tms.quotation.templateText.alert.error'/>");</script>
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
            <%--<x:display name="addQtnTemplateTextPage.addQtnTemplateTextPortlet"/>--%>
            <x:display name="addQtnTemplateTextPage.addQtnTemplateText"/>
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
