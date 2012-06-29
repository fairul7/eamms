<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" scope="request" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top; width: 150px;"><fmt:message key='general.label.title'/>&nbsp; *<br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <c:if test="${!empty form.id}">
            <input type="hidden" name="<c:out value="${form.childMap.title.absoluteName}"/>" value="<c:out value="${form.id}"/>">
            <c:out value="${form.id}" />
        </c:if>
        <x:display name="${form.childMap.title.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='general.label.content'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.contentBox.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='general.label.location'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.location.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="contentTitleFont" style="vertical-align: top;" colspan="2">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
        <fmt:message key='siteadmin.label.channelSize'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.maximumChannelSize'/>&nbsp; *<br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.maxSize.absoluteName}"/> <fmt:message key='siteadmin.label.k'/>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.linkDepth'/>&nbsp; *<br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.depth.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.includeImages'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.allowImages.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.followOffsiteLinks'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.offsiteLinks.absoluteName}"/>
      </td>
    </tr>

    <tr>
      <td class="contentTitleFont" style="vertical-align: top;" colspan="2">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
        <fmt:message key='siteadmin.label.channelRefresh'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;"><fmt:message key='siteadmin.label.refreshThisChannel'/><br>
      </td>
      <td class="classRow" style="vertical-align: top;">
            <table cellspacing="5" cellpadding="0" border="0">
            <tr>
              <td>
              <x:display name="${form.childMap.refreshAlways.absoluteName}"/>
              </td>
              <td class="formlabel"><label for="refresh_always"><fmt:message key='siteadmin.label.onEverySync'/></label></td>
            </tr>
            <tr>
              <td>
              <x:display name="${form.childMap.refreshHourly.absoluteName}"/>
              </td>
              <td class="formlabel"><label for="refresh_hourly"><fmt:message key='siteadmin.label.every'/>

              <x:display name="${form.childMap.refreshHourlyDuration.absoluteName}"/>
               <fmt:message key='siteadmin.label.hours'/></label></td>
            </tr>
            <tr>
              <td>
              <x:display name="${form.childMap.refreshDaily.absoluteName}"/>
              </td>
              <td class="formlabel"><label for="refresh_daily"><fmt:message key='siteadmin.label.onceDailyAt'/>
            <x:display name="${form.childMap.refreshDailyTime.absoluteName}"/>
              </label></td>
            </tr>
            <tr>
              <td>
              <x:display name="${form.childMap.refreshOnce.absoluteName}"/>
              </td>
              <td class="formlabel"><label for="refresh_once"><fmt:message key='siteadmin.label.onlyOnce'/></label></td>
            </tr>
            </table>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;">&nbsp;<br>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${form.childMap.submitButton.absoluteName}"/>
        <c:if test="${!empty form.id}">
            <script>
            <!--
                function activateChannel() {
                    window.open(document.forms['<c:out value="${form.rootForm.absoluteName}"/>'].elements['<c:out value="${form.childMap.location.absoluteName}"/>'].value, 'view_window');
                }
                function emulateChannel() {
                    <c:url var="channelId" value="/cmsmobile/index.jsp?id=${form.id}"/>
                    window.open("<c:out value='${channelId}'/>", 'emulator_window', "height=450,width=350,left=50,top=50,screenx=50,screeny=50,scrollbars=yes");
                }
            //-->
            </script>
            <input type="button" class="button" value="<fmt:message key='siteadmin.label.activateChannel'/>" onclick="activateChannel()" />
            <input type="button" class="button" value="<fmt:message key='siteadmin.label.emulateChannel'/>" onclick="emulateChannel()" />
        </c:if>
      </td>
    </tr>

  </tbody>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>
