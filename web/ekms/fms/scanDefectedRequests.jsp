<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_scanDefectedTransportRequest">
    <com.tms.fms.engineering.ui.ScanDefectedTRForm name="submit" width="100%"/>
  </page>
</x:config>

<c-rt:if test="${forward.name == 'refresh'}">
	<script type="text/javascript">
		alert("Missing Transport Request detected");
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'refreshFR'}">
	<script type="text/javascript">
		alert("A defected Facility Request detected");
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'cleared'}">
	<script type="text/javascript">
		alert("Data Cleared");
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'zero'}">
	<script type="text/javascript">
		alert("This request is OK");
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'refreshSync'}">
	<script type="text/javascript">
		alert("Transport Request fixed");
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'refreshFRSync'}">
	<script type="text/javascript">
		alert("Facility Request fixed");
	</script>
</c-rt:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">Scan Defected Transport Request from Facility</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_scanDefectedTransportRequest.submit"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>