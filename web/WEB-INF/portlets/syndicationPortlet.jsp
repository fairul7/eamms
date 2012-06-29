<%@ include file="/common/header.jsp" %>
<iframe id="syndicationPortletFrame" name="syndicationPortletFrame" src="<c:url value="/ekms/syndication/syndicationPortlet.jsp"/>" border="0" width="0" height="0" onload="loadSyndicationPortlet()"></iframe>
<div id="syndicationPortletDiv"><img src="<c:url value="/ekms/images/ekp2005/loading.gif"/>" border="0" valign="middle"> <fmt:message key="general.label.loadingPleaseWait"/></div>
<script>
<!--
	function loadSyndicationPortlet() {
		var frame = window.frames["syndicationPortletFrame"];
		var sdiv = document.getElementById("syndicationPortletDiv");
		sdiv.innerHTML = frame.document.body.innerHTML;
	}
//-->	
</script>
