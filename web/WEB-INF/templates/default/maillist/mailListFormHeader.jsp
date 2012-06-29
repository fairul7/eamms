<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="f" value="${widget}" scope="request"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">

    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
            <c:if test="${f.mailList.mailListType eq 1}">
                <c:set var="mlType" value="Composed" ></c:set>
            </c:if>
            <c:if test="${f.mailList.mailListType eq 2}">
                <c:set var="mlType" value="Content" ></c:set>
            </c:if>
            <c:if test="${f.mailList.mailListType eq 3}">
                <c:set var="mlType" value="Scheduled" ></c:set>
            </c:if>
            <c:if test="${f.newForm}"><fmt:message key='maillist.label.new'/> <c:out value="${mlType}" /> <fmt:message key='maillist.label.mailingList'/></c:if>
            <c:if test="${!f.newForm}"><fmt:message key='maillist.label.editing'/> <c:out value="${mlType}" /> <fmt:message key='maillist.label.mailingList'/>: <c:out value="${f.mailList.name}" /></c:if>

            <c:if test="${f.formSaved}"><font color="green"><i>(Saved)</i></font></c:if>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.name'/>&nbsp; *</td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffName.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.description'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffDescription.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='maillist.label.templateDetails'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.subject'/>&nbsp; *</td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffSubject.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.htmlTemplate'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffHtmlYes.absoluteName}" />
        <x:display name="${f.ffHtmlNo.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.template'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffTemplateId.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='maillist.label.emailDetails'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.senderEmail'/>&nbsp; *</td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffSenderEmail.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.recipients'/> <br><fmt:message key='maillist.label.email'/><br> <small><fmt:message key='maillist.message.multipleEmails'/></small></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffRecipientsEmail.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.recipients'/> <br><fmt:message key='maillist.label.emailToGroupMembers'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffGroups.absoluteName}" />
      </td>
    </tr>