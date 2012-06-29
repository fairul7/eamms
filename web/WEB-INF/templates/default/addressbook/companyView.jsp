<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="company" value="${w.company}" />

<table width="100%" border="0" cellpadding="5" cellspacing="1">

    <%--company name--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='addressbook.label.companyName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.company}" />
    </td>
    </tr>

    <%--company email--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='addressbook.label.email'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.email}" />
    </td>
    </tr>

    <%--address--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.address'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${company.address}" /></pre>
    </td>
    </tr>

    <%--city--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.city'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.city}" />
    </td>
    </tr>

    <%--state--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.state'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.state}" />
    </td>
    </tr>

    <%--postcode--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.postCode'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.postcode}" />
    </td>
    </tr>

    <%--country--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.country'/></strong></td>
    <td class="contentBgColor">
        <c:choose>
            <c:when test="${company.country != '-1'}">
                <c:out value="${company.propertyMap.displayCountry}" />
            </c:when>
            <c:otherwise>
                &nbsp;
            </c:otherwise>
        </c:choose>
    </td>
    </tr>

    <%--phone--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.phone'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.phone}" />
    </td>
    </tr>

    <%--fax--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.fax'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${company.fax}" />
    </td>
    </tr>

  <%--comments--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.comments'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${company.comments}" /></pre>
    </td>
  </tr>

</table>

