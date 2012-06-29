<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="contact" value="${w.contact}" />
<c:set var="company" value="${w.company}" />

<table width="100%" border="0" cellpadding="5" cellspacing="1">

  <%--title--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='addressbook.label.title'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.title}" />
    </td>
  </tr>

  <%--first name--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.firstName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.firstName}" />
    </td>
  </tr>

  <%--middle name--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.middleName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.middleName}" />
    </td>
  </tr>

  <%--last name--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.lastName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.lastName}" />
    </td>
  </tr>

  <%--nick name--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.nickName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.nickName}" />
    </td>
  </tr>

  <%--email--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.email'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.email}" />
    </td>
  </tr>

  <%--mobile--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.mobile'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.mobile}" />
    </td>
  </tr>

  <%--designation--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.designation'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.designation}" />
    </td>
  </tr>

  <%--company--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.company'/></strong></td>
    <td class="contentBgColor">
        <c:choose>
            <c:when test="${empty company}">
                <c:out value="${contact.company}" />
            </c:when>
            <c:otherwise>
                <c:out value="${company.company}" />
            </c:otherwise>
        </c:choose>
    </td>
  </tr>

  <%--address--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.address'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${contact.address}" /></pre>
    </td>
  </tr>

  <%--city--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.city'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.city}" />
    </td>
  </tr>

  <%--state--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.state'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.state}" />
    </td>
  </tr>

  <%--postcode--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.postCode'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.postcode}" />
    </td>
  </tr>

  <%--country--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.country'/></strong></td>
    <td class="contentBgColor">
        <c:choose>
            <c:when test="${contact.country != '-1'}">
                <c:out value="${contact.propertyMap.displayCountry}" />
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
        <c:out value="${contact.phone}" />
    </td>
  </tr>

  <%--extension--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.ext'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.extension}" />
    </td>
  </tr>

  <%--fax--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.fax'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${contact.fax}" />
    </td>
  </tr>

  <%--comments--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.comments'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${contact.comments}" /></pre>
    </td>
  </tr>

  <c:if test="${!empty company}">

        <%--company heading--%>
        <tr>
        <td class="contentStrapColor" colspan="2" valign="top" nowrap><strong><fmt:message key='addressbook.label.companyDetails'/></strong></td>
        </tr>

        <%--company name--%>
        <tr>
        <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='addressbook.label.companyName'/></strong></td>
        <td class="contentBgColor">
            <c:out value="${company.company}" />
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

  </c:if>

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

