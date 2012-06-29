<%@ page import="com.tms.cms.ad.ui.AdForm"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="f" value="${widget}"/>
<%
    AdForm a = (AdForm) request.getAttribute("widget");
%>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="0" border="0" style="text-align: left; width: 100%;">



    <tr>
      <td style="vertical-align: center;" colspan="2" class="contentTitleFont" height="22">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <c:if test="${f.newAd}">&nbsp;<fmt:message key='ad.label.newAd'/></c:if>
          <c:if test="${!f.newAd}">&nbsp;<fmt:message key='general.label.editing'/> &nbsp;<c:out value="${f.ad.url}" /></c:if>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.name'/>&nbsp; *
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffName.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.url'/>&nbsp; *
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffUrl.absoluteName}" />
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
        <fmt:message key='general.label.newWindow'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffNewWindowYes.absoluteName}" />
        <x:display name="${f.ffNewWindowNo.absoluteName}" />
      </td>
    </tr>

    <c:if test="${!empty f.ad.imageFile}">
    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.adImage'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <img src="<c:url value="/storage/${f.ad.imageFile}" />">
      </td>
    </tr>
    </c:if>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.imageFilename'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffImageFile.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.alternateText'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffAlternateText.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.useScript'/>
      </td>
      <td style="vertical-align: top;" class="classRow:>
        <x:display name="${f.ffUseScriptYes.absoluteName}" />
        <x:display name="${f.ffUseScriptNo.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.script'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffScript.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.startDate'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffStartDate.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.startDateEnabled'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffStartDateEnabledYes.absoluteName}" />
        <x:display name="${f.ffStartDateEnabledNo.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.endDate'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffEndDate.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
        <fmt:message key='general.label.endDateEnabled'/>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffEndDateEnabledYes.absoluteName}" />
        <x:display name="${f.ffEndDateEnabledNo.absoluteName}" />
      </td>
    </tr>

    

    <script language="JavaScript">
    <!--
        var adId='<c:out value="${f.adId}" />';
        function doReport(click) {
            window.open('ad_report.jsp?adId=' + adId + '&click=' + click, 'report', 'width=800,height=600,left=0,top=0,scrollbars=yes,resizable=yes');
        }
    //-->
    </script>

    <tr>
      <td style="vertical-align: top;">
        &nbsp;
      </td>
      <td style="vertical-align: top;">
        <input type="submit" class="button" value="<fmt:message key='general.label.save'/>">
        <input type="reset" class="button" value="<fmt:message key='general.label.reset'/>">
        <c:if test="${!f.newAd}">
            <input type="button" class="button" value="<fmt:message key='general.label.pageViewReport'/>" onclick="doReport('false')">
            <input type="button" class="button" value="<fmt:message key='general.label.clickThruReport'/>" onclick="doReport('true')">
        </c:if>
      </td>
    </tr>








</table>
<jsp:include page="../form_footer.jsp" flush="true"/>



