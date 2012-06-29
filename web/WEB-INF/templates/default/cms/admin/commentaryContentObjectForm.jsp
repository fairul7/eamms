<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" scope="request" value="${widget}"/>
<c:set var="co" scope="request" value="${form.contentObject}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 150px;"><fmt:message key='general.label.nameTitle'/><br>
      </td>
      <td style="vertical-align: top;"><x:display name="${form.childMap.nameField.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2">
        <hr>
        <span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='general.label.content'/></span>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.summary'/><br>
      </td>
      <td style="vertical-align: top;"><x:display name="${form.childMap.summaryBox.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2">
        <hr>
        <span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='general.label.metaInformation'/></span>
      </td>
    </tr>

<c:choose>
<c:when test="${!empty co && !empty co.id}">
    <tr>
      <td style="vertical-align: top; width: 150px;"><fmt:message key='general.label.id'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.id}"/>
      </td>
    </tr>
</c:when>
<c:otherwise>
    <tr>
      <td style="vertical-align: top; width: 150px;"><fmt:message key='general.label.id'/><br>
      </td>
      <td style="vertical-align: top;"><x:display name="${form.childMap.idField.absoluteName}"/>
      </td>
    </tr>
</c:otherwise>
</c:choose>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.description'/><br>
      </td>
      <td style="vertical-align: top;"><x:display name="${form.childMap.descriptionBox.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2">
        <hr>
        <span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key='general.label.publication'/></span>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.publishingSchedule'/><br>
      </td>
      <td style="vertical-align: top;">
            <table cellpadding="2">
            <tr>
                <td colspan="2">
                    <x:display name="${form.childMap.scheduleCheckBox.absoluteName}"/> <fmt:message key='general.label.schedulePublishing'/>
                </td>
            </tr>
            <tr>
                <td><fmt:message key='general.label.startDate'/></td>
                <td><x:display name="${form.childMap.startDate.absoluteName}"/></td>
            </tr>
            <tr>
                <td><fmt:message key='general.label.startTime'/></td>
                <td><x:display name="${form.childMap.startTime.absoluteName}"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <x:display name="${form.childMap.scheduleEndCheckBox.absoluteName}"/> <fmt:message key='general.label.schedulePublishingEnd'/>
                </td>
            </tr>
            <tr>
                <td><fmt:message key='general.label.endDate'/></td>
                <td><x:display name="${form.childMap.endDate.absoluteName}"/></td>
            </tr>
            <tr>
                <td><fmt:message key='general.label.endTime'/></td>
                <td><x:display name="${form.childMap.endTime.absoluteName}"/></td>
            </tr>
            </table>
            <p>
      </td>
    </tr>

  </tbody>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>
