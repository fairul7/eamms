<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.collab.rss.ui.ChannelEditForm"%>

<x:permission permission="com.tms.collab.rss.managerRss" module="com.tms.collab.rss.model.RssHandler" url="rssNoPermission.jsp">
<!-- Declare Widgets -->
<x:config>
    <page name="channels">
        <com.tms.collab.rss.ui.ChannelEditForm name="EditForm" />
    </page>
</x:config>

<script>
	function hideButton(paraValue){
		var isChecked = document.forms['channels.EditForm'].elements['channels.EditForm.autocount'].checked;
		if(isChecked){
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=true;
			if (paraValue!=0) {
				document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value = paraValue;
			} else {
				alert("For Auto Count, Please insert value more than 1");
				document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value = '10';
			}
			document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].readOnly = false;
		} else {
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=false;
			document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value = '';
			document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].readOnly = true;
		}
	}
	
	function disabledButton(){
		var isChecked = document.forms['channels.EditForm'].elements['channels.EditForm.autocount'].checked;
		if(isChecked){
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=true;
		} else {
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=false;
		}
	}
	
	function disabledAutoCount(){
		//alert(document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value);
		if(document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value > 0 || 
			document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value != ''){
			document.forms['channels.EditForm'].elements['channels.EditForm.autocount'].checked=true;
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=true;
		} 
		if (document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value == '0'){
			document.forms['channels.EditForm'].elements['channels.EditForm.autocount'].checked=false;
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=false;
		}
		if (document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value == ''){
			document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value = '0';
			document.forms['channels.EditForm'].elements['channels.EditForm.autocount'].checked=false;
			document.forms['channels.EditForm'].elements['button*channels.EditForm.buttonPanel.AddItem'].disabled=false;
		}
	}
	
	function setZero(){
		if(document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value == ''){
			document.forms['channels.EditForm'].elements['channels.EditForm.countfor'].value = '0';
		}
	}
</script>

<c-rt:set var="forwardSuccess" value="<%=ChannelEditForm.FORWARD_SUCCESS %>" />
<c-rt:set var="forwardPopUpSuccess" value="<%=ChannelEditForm.FORWARD_POPUP_SUCCESS %>" />
<c-rt:set var="forwardCancel" value="<%=ChannelEditForm.FORWARD_CANCEL %>" />

<c:if test="${forward.name eq forwardSuccess}">
    <script>
    	document.location = "channelListing.jsp";
    </script>
</c:if>
<c:if test="${forward.name eq forwardCancel}">
    <script>
    	document.location = "channelListing.jsp";
    </script>
</c:if>

<c:set var="autocountfor" value="${widgets['channels.EditForm'].autocountfor.value}" />
<c:set var="autocount" value="${widgets['channels.EditForm'].autocount.checked}" />
<c:set var="moduleId" value="${widgets['channels.EditForm.module'].value}" />
<c:set var="categoryId" value="${widgets['channels.EditForm.category'].value}" />

<c:if test="${!empty param.channelId}">
	<x:set name="channels.EditForm" property="channelId" value="${param.channelId}" />
</c:if>

<c:if test="${forward.name eq forwardPopUpSuccess}">
    <c:choose>
    	<c:when test="${autocount eq true}">
    		<script>
				window.open("itemAutoListing.jsp?channelId=<c:out value="${widgets['channels.EditForm'].channelId}"/>&count=<c:out value="${autocountfor}"/>&autocount=<c:out value="${autocount}"/>&moduleId=<c:out value="${moduleId}"/>&categoryId=<c:out value="${categoryId}"/>", "Item", "status=1, height=400, width=400, resizable=1, location=1");
		    </script>
    	</c:when>
    	<c:otherwise>
    		<script>
				window.open("itemNonAutoListing.jsp?channelId=<c:out value="${widgets['channels.EditForm'].channelId}"/>&count=<c:out value="${autocountfor}"/>&autocount=<c:out value="${autocount}"/>&moduleId=<c:out value="${moduleId}"/>&categoryId=<c:out value="${categoryId}"/>", "Item", "status=1, height=400, width=400, resizable=1, location=1");
    		</script>
    	</c:otherwise>
    </c:choose>
    
</c:if>

<!-- Display Page -->
<%@include file="/ekms/includes/header.jsp" %>
<html>
<body onLoad="disabledButton()">
	<table cellpadding="4" cellspacing="1" width="100%">
		<tr>
			<td class="contentTitleFont" style="padding:5px;"><fmt:message key="rss.channel.rss"/> > <fmt:message key="rss.channel.editChannel"/></td>
		</tr>
		<tr>
			<td>
				<x:display name="channels.EditForm"/>
			</td>
		</tr>
    </table>        		
</body>    
</html>
</x:permission>
<%@include file="/ekms/includes/footer.jsp" %>






