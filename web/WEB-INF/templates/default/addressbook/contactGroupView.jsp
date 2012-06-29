<%@ page import="com.tms.collab.directory.ui.ContactView,
                 com.tms.collab.messaging.model.MessagingModule"%>
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="contact" value="${w.contact}" />
<c:set var="company" value="${w.company}" />

<table width="100%" border="0" cellpadding="5" cellspacing="1">

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.contactGroup'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.firstName}" />
    </td>
  </tr>

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.comments'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${contact.comments}" /></pre>
    </td>
  </tr>

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.contactGroupContacts'/></strong></td>
    <td class="contentBgColor">
        <c:forEach items="${w.contactGroupList}" var="ct">
            <li><x:event name="${w.absoluteName}" type="<%= ContactView.EVENT_VIEW %>" param="id=${ct.id}" ><c:out value="${ct.displayName}"/></x:event>
                <c:if test="${!empty ct.email}">(<c:out value="${ct.email}"/>)</c:if></li>
        </c:forEach>
    </td>
  </tr>

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.contactGroupIntranetUsers'/></strong></td>
    <td class="contentBgColor">
        <c:forEach items="${w.intranetUsersList}" var="ct">
            <li><x:event name="${w.absoluteName}" type="<%= ContactView.EVENT_VIEW_INTRANET_USER %>" param="id=${ct.id}" ><c:out value="${ct.name}"/></x:event> (<c:out value="${ct.username}"/>@<%= MessagingModule.INTRANET_EMAIL_DOMAIN %>)</li>
        </c:forEach>
    </td>
  </tr>

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.contactGroupEmails'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${contact.contactGroupEmails}" /></pre>
    </td>
  </tr>

  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%" colspan="2">&nbsp;</td>
  </tr>

  <c:if test="${!empty contact.auditDateCreated}">
        <tr>
        <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.created'/></strong></td>
        <td class="contentBgColor">
            <c:out value="${contact.auditUserCreatedObject.name}" />
            (<fmt:formatDate pattern="${globalDateLong}" value="${contact.auditDateCreated}" />)
        </td>
        </tr>
  </c:if>

  <c:if test="${!empty contact.auditDateModified}">
        <tr>
        <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.lastModified'/></strong></td>
        <td class="contentBgColor">
            <c:out value="${contact.auditUserModifiedObject.name}" />
            (<fmt:formatDate pattern="${globalDateLong}" value="${contact.auditDateModified}" />)
        </td>
        </tr>
  </c:if>

</table>

