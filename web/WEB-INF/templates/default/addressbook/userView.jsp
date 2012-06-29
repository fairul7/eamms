<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="user" value="${w.user}" />
<c:set var="mugshotpath" value="${w.mugshotpath}"/>

<table width="100%" border="0" cellpadding="5" cellspacing="1">

 <c:if test="${!empty mugshotpath}">
 <tr>
     <td class="contentBgColor"></td>
     <td class="contentBgColor"><IMG SRC=<c:out value="${mugshotpath}"/> height=120 width=90 border=0></td>
 </tr>
 </c:if>

  <%--username--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='addressbook.label.username'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.username}" />
    </td>
  </tr>

  <%--first name--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.firstName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.firstName}" />
    </td>
  </tr>

  <%--last name--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.lastName'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.lastName}" />
    </td>
  </tr>

  <%--email--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.email'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.email1}" />
    </td>
  </tr>

  <%--mobile--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.mobile'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.telMobile}" />
    </td>
  </tr>

  <%--address--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.address'/></strong></td>
    <td class="contentBgColor">
        <pre class="contentBgColor"><c:out value="${user.propertyMap.address}" /></pre>
    </td>
  </tr>

  <%--city--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.city'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.city}" />
    </td>
  </tr>

  <%--state--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.state'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.state}" />
    </td>
  </tr>

  <%--postcode--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.postCode'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.postcode}" />
    </td>
  </tr>

  <%--country--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.country'/></strong></td>
    <td class="contentBgColor">
        <c:choose>
            <c:when test="${user.propertyMap.country != '-1'}">
                <c:out value="${user.propertyMap.displayCountry}" />
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
        <c:out value="${user.propertyMap.telOffice}" />
    </td>
  </tr>

  <%--fax--%>
  <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap><strong><fmt:message key='addressbook.label.fax'/></strong></td>
    <td class="contentBgColor">
        <c:out value="${user.propertyMap.fax}" />
    </td>
  </tr>

</table>

