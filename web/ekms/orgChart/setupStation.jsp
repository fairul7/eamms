<%@ page import="com.tms.hr.orgChart.ui.StationForm"%>
<%@include file="/common/header.jsp" %>
<%@include file="includes/accessControl.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="orgChart">
		<com.tms.hr.orgChart.ui.StationForm name="addStation"/>
        <com.tms.hr.orgChart.ui.StationList name="listStation"/>
    </page>
</x:config>

<c-rt:set var="forward_saved" value="<%=StationForm.FORWARD_SAVED%>"/>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="listHierachy.jsp"/>
</c:if>

<c:if test="${forward.name==forward_saved}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.station.alert.saved"/>");
    </script>
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<table cellpadding="0" cellspacing="0" width="100%" class="contentBgColor">
    <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
      &nbsp;
            <fmt:message key='orgChart.station.label.addStationTitle'/>
            </font>
        </b>
    </td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
    <tr>
        <td>
            <x:display name="orgChart.addStation"/>
        </td>
    </tr>
    <tr>
    <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
      &nbsp;
            <fmt:message key='orgChart.station.label.listStationTitle'/>
            </font>
        </b>
    </td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
    <tr>
        <td>
            <x:display name="orgChart.listStation"/>
        </td>
    </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>




