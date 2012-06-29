<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.collab.rss.ui.ChannelAddForm"%>

<x:permission permission="com.tms.collab.rss.managerRss" module="com.tms.collab.rss.model.RssHandler" url="rssNoPermission.jsp">
<!-- Declare Widgets -->
<x:config>
    <page name="channels">
        <com.tms.collab.rss.ui.ChannelAddForm name="AddForm" />
    </page>
</x:config>

<c:set var="moduleid" value="${widgets['channels.AddForm.module'].value}" />

<script>
    var title;
	function setAutoChecked(){
		//alert(document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value);
		if(document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value > 0 || 
			document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value != ''){
			document.forms['channels.AddForm'].elements['channels.AddForm.autocount'].checked=true;
		} 
		if (document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value == '0'){
			document.forms['channels.AddForm'].elements['channels.AddForm.autocount'].checked=false;
		}
		if (document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value == ''){
			document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value = '0';
			document.forms['channels.AddForm'].elements['channels.AddForm.autocount'].checked=false;
		}
	}
	
	function disableCountFor(){
		//alert(document.forms['channels.AddForm'].elements['channels.AddForm.autocount'].checked);
		if (document.forms['channels.AddForm'].elements['channels.AddForm.autocount'].checked == true){
			document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].readOnly = false;
			if (document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value == '0' ){
				alert("For Auto Count, Please insert value more than 1");
				document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value = '10'
			}
		} else {
			document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].readOnly = true;
			document.forms['channels.AddForm'].elements['channels.AddForm.countfor'].value = '0'
		}
	}
</script>



<c:if test="${!empty param.ChannelId}">
	<c:redirect url="channelAdd.jsp?id=${param.ChannelId}"/>
</c:if>
<c-rt:set var="forwardSuccess" value="<%=ChannelAddForm.FORWARD_SUCCESS %>" />
<c-rt:set var="forwardCancel" value="<%=ChannelAddForm.FORWARD_CANCEL %>" />
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

<!-- Display Page -->
<%@include file="/ekms/includes/header.jsp" %>
	<table cellpadding="4" cellspacing="1" width="100%">
		<tr>
			<td class="contentTitleFont" style="padding:5px;"><fmt:message key="rss.channel.rss"/> > <fmt:message key="rss.channel.addChannel"/></td>
		</tr>
		<tr>
			<td>
				<x:display name="channels.AddForm"/>
			</td>
		</tr>
    </table>   
</x:permission>
<%@include file="/ekms/includes/footer.jsp" %>
