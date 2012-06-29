<%@ include file="/common/header.jsp" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<script language="javascript" src="/common/tree/tree.js"></script>
<script language="javascript" src="/common/WCH.js"></script>
<script type="text/javascript" src="/common/scripts/yahoo.js"></script>
<script type="text/javascript" src="/common/scripts/event.js" ></script>
<script type="text/javascript" src="/common/scripts/dom.js" ></script>
<script type="text/javascript" src="/common/scripts/dragdrop.js" ></script>
<script type="text/javascript" src="/common/scripts/container_core.js"></script>
<script type="text/javascript" src="/common/scripts/container.js"></script>
<script type="text/javascript" src="/common/scripts/ajax.js"></script>
<script type="text/javascript" src="/common/scripts/ajax-dynamic-content.js"></script>
<script type="text/javascript" src="/ekms/messenger/js/messengerMain.js"></script>
<script type='text/javascript' src="<c:out value='${pageContext.request.contextPath}/dwr/engine.js'/>"></script>
<script type='text/javascript' src="<c:out value='${pageContext.request.contextPath}/dwr/util.js'/>"></script>
<script type='text/javascript' src="<c:out value='${pageContext.request.contextPath}/dwr/interface/MessageDWRModule.js'/>"></script>

<link type="text/css" rel="stylesheet" href="/ekms/messenger/css/fonts.css">
<link type="text/css" rel="stylesheet" href="/ekms/messenger/css/container.css">	
<style type="text/css">
	textarea {border:1px solid #00ADE6;}
</style>
<script>
var chatHeader = '<fmt:message key="com.tms.messenger.panelPop.chatSession"/>';
var chatFooter = '<fmt:message key="com.tms.messenger.panelPop.footer"/>';

</script>

<div id="messengerPanel">
	<div class="hd">
		<div class="tl"></div><span id="chatSession"></span><div class="tr"></div>
	</div>
	<div class = "bd">
		<div id = "messageCollapsar"></div>
	</div>
	<div class="ft" id ="conver"></div>
</div>