<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<jsp:include page="mailListFormHeader.jsp" flush="true" />

    <tr>
      <td style="vertical-align: center;" colspan="2" class="contentTitleFont" height="22">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='cms.label.contentDetails'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
      <fmt:message key='general.label.content'/><br/>
      <small><fmt:message key='maillist.label.selectASection'/></small>
      </td>
      <td style="vertical-align: top;" class="classRow" class="classRow">
        <x:display name="${f.ffContentId.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.header'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffHeader.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.footer'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffFooter.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.startDate'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffStartDate.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.endDate'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffEndDate.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%> 
          <fmt:message key='maillist.label.scheduleDetails'/>
        <%--  /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
      <fmt:message key='general.label.active'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffActiveYes.absoluteName}" />
        <x:display name="${f.ffActiveNo.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
      <fmt:message key='maillist.label.repeatInterval'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffScheduleRepeatIntervalDaily.absoluteName}" />
        <x:display name="${f.ffScheduleRepeatIntervalWeekly.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.day'/><br>
      <small><fmt:message key='maillist.label.forWeeklyScheduleOnly'/></small></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffScheduleDay.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
      <fmt:message key='general.label.startTime'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffScheduleStartTime.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: center;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='maillist.label.unsubscribedEmails'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
      <fmt:message key='maillist.label.unsubscribedEmails'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffUnsubscribedEmails.absoluteName}" />
      </td>
    </tr>

<jsp:include page="mailListFormFooter.jsp" flush="true" />
