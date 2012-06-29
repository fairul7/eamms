<%@ page import="com.tms.cms.ad.ui.AdLocationForm"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="f" value="${widget}"/>
<%
    AdLocationForm a = (AdLocationForm) request.getAttribute("widget");
%>
<jsp:include page="../form_header.jsp" flush="true"/>

<%-- 
<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
--%>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">




    <tr>
      <td class="contentTitleFont" style="vertical-align: top;" colspan="2">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
        <c:if test="${f.newAdLocation}"><fmt:message key='ad.label.newAdLocation'/></c:if>
        <c:if test="${!f.newAdLocation}"><fmt:message key='general.label.editing'/> <c:out value="${f.adLocation.name}" /></c:if>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;">
        <fmt:message key='general.label.name'/>
        &nbsp; *
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${f.ffName.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;">
        <fmt:message key='general.label.active'/>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${f.ffActiveYes.absoluteName}" />
        <x:display name="${f.ffActiveNo.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;">
        <fmt:message key='general.label.type'/>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${f.ffType.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;">
        <fmt:message key='general.label.description'/>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${f.ffDescription.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td class="classRowLabel" align="right" style="vertical-align: top;">
        <fmt:message key='general.label.ads'/>
      </td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${f.ffAds.absoluteName}" />
      </td>
    </tr>

	<script language="JavaScript">
    <!--
        var adLocationName='<c:out value="${f.adLocation.name}" />';
        function doPreview() {
            window.open('ad_preview.jsp?adLocationName=' + adLocationName + '&preview=true', 'preview', 'width=800,height=600,left=0,top=0,scrollbars=yes,resizable=yes');
        }
    //-->
    </script>

    <tr>
      <td class="classRow" style="vertical-align: top;">
        &nbsp;
      </td>
      <td class="classRow" style="vertical-align: top;">
        <input type="submit" class="button" value="<fmt:message key='general.label.save'/>">
        <input type="reset" class="button" value="<fmt:message key='general.label.reset'/>">
        <c:if test="${!f.newAdLocation}">
            <input type="button" class="button" value="<fmt:message key='general.label.preview'/>" onclick="doPreview()">
        </c:if>
      </td>
    </tr>


	<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>
	



<%-- 
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
--%>
