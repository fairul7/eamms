<!--- editQtnTemplateText.jsp --->
<%@ page import="kacang.ui.WidgetManager, com.tms.quotation.ui.AddQtnTemplateText" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="editQtnTemplateTextPage">
    <%--<portlet name="editQtnTemplateTextPortlet" text="Edit QtnTemplateText" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.AddQtnTemplateText name="editQtnTemplateText" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= AddQtnTemplateText.QTNTEMPLATETEXT_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= AddQtnTemplateText.QTNTEMPLATETEXT_ADD_FAIL %>"/>
<c-rt:set var="forward_exists" value="<%= AddQtnTemplateText.QTNTEMPLATETEXT_ADD_EXISTS %>"/>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="templateId" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <%--<c:set var="templateId" value="${widgets['editQtnTemplateTextPage.editQtnTemplateTextPortlet.editQtnTemplateText'].templateId}"/>--%>
    <c:set var="templateId" value="${widgets['editQtnTemplateTextPage.editQtnTemplateText'].templateId}"/>
  </c:otherwise>
</c:choose>
<%--<x:set name="editQtnTemplateTextPage.editQtnTemplateTextPortlet.editQtnTemplateText" property="templateId" value="${templateId}"/>
<x:set name="editQtnTemplateTextPage.editQtnTemplateTextPortlet.editQtnTemplateText" property="type" value="${type}"/>--%>
<x:set name="editQtnTemplateTextPage.editQtnTemplateText" property="templateId" value="${templateId}"/>
<x:set name="editQtnTemplateTextPage.editQtnTemplateText" property="type" value="${type}"/>

<x:set name="editQtnTemplateTextPage.editQtnTemplateText" property="whoModified" value="${sessionScope.currentUser.username}"/>
<!--- permissions --->
<x:set name="editQtnTemplateTextPage.editQtnTemplateText" property="canAdd" value="1"/>
<x:set name="editQtnTemplateTextPage.editQtnTemplateText" property="canEdit" value="1"/>
<x:set name="editQtnTemplateTextPage.editQtnTemplateText" property="canDelete" value="1"/>

<c:choose>
  <c:when test="${forward.name == forward_succ}">
    <script>alert("<fmt:message key='com.tms.quotation.templateText.alert.updated'/>"); document.location = "<c:url value="viewQtnTemplateText.jsp"/>";</script>
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
            <%--<x:display name="editQtnTemplateTextPage.editQtnTemplateTextPortlet"/>--%>
            <x:display name="editQtnTemplateTextPage.editQtnTemplateText"/>
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
