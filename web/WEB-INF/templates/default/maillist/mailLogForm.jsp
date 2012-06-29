<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="f" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">

    <tr>
      <td style="vertical-align: top;" colspan="2">
        <hr>
        <span style="font-family:Arial; font-size:16px; font-weight:bold">
            <fmt:message key='maillist.label.viewingMailLog'/>: <c:out value="${f.mailLog.id}" />
        </span>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.startDate'/> </td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailLog.startDate}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.endDate'/> </td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailLog.endDate}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='maillist.label.message'/> </td>
      <td style="vertical-align: top;">
        <b><c:out value="${f.mailLog.message}" /></b>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='maillist.label.mailingListType'/> </td>
      <td style="vertical-align: top;">
        <c:if test="${f.mailLog.mailListType == 1}"><fmt:message key='maillist.label.composedMailingList'/></c:if>
            <c:if test="${f.mailLog.mailListType == 2}"><fmt:message key='maillist.label.contentMailingList'/></c:if>
            <c:if test="${f.mailLog.mailListType == 3}"><fmt:message key='maillist.label.scheduledMailingList'/></c:if>
            <c:if test="${f.mailLog.mailListType < 1 || f.mailLog.mailListType > 3}"><font color=red><fmt:message key='maillist.label.unknownMailingListType'/></font></c:if>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='maillist.label.html'/> </td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailLog.html}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='maillist.label.senderEmail'/> </td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailLog.senderEmail}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='maillist.label.recipients'/> </td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailLog.recipientsEmailForDisplay}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='maillist.label.subject'/> </td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailLog.subject}" />
      </td>
    </tr>


    <tr>
      <td style="vertical-align: top;" colspan="2">
        <hr>
        <span style="font-family:Arial; font-size:16px; font-weight:bold">
            <fmt:message key='maillist.label.contents'/>
        </span>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2"><pre>--- [ <fmt:message key='maillist.label.BEGIN'/>: <fmt:message key='maillist.label.mailingListPreview'/> ] ---</pre><c:if test="${f.mailLog.html}"><c:out value="${f.mailLog.content}" escapeXml="yes" /></c:if><c:if test="${!f.mailLog.html}"><pre><c:out value="${f.mailLog.content}" /></pre></c:if><pre>--- [ <fmt:message key='maillist.label.END'/>: <fmt:message key='maillist.label.mailingListPreview'/> ] ---</pre></td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2">
        <div align="center">
            [ <x:event name="${f.absoluteName}" type="Ok"><fmt:message key='general.label.ok'/></x:event> ]
        </div>
      </td>
    </tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>