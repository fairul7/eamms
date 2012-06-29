<%@ include file="/common/header.jsp" %>
<iframe id="scoringSummaryFrame" name="scoringSummaryFrame" src="<c:url value="/ekms/sysadmin/scoringSummary.jsp?scoringSummaryMonth=${param.scoringSummaryMonth}&scoringSummaryYear=${param.scoringSummaryYear}"/>" border="0" width="0" height="0" onload="loadScoringSummary()"></iframe>
<div id="scoringSummaryDiv"><img src="<c:url value="/ekms/images/ekp2005/loading.gif"/>" border="0" valign="middle"> <fmt:message key="general.label.loadingPleaseWait"/></div>
<script>
<!--
	function loadScoringSummary() {
		var frame = window.frames["scoringSummaryFrame"];
		var sdiv = document.getElementById("scoringSummaryDiv");
		sdiv.innerHTML = frame.document.body.innerHTML;
	}
//-->	
</script>