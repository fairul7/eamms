<%@include file="/common/header.jsp"%>
<%@ page import="com.tms.collab.rss.ui.ItemListNonAutoForm"%>

<x:permission permission="com.tms.collab.rss.managerRss" module="com.tms.collab.rss.model.RssHandler" url="rssNoPermission.jsp">
<x:config>
	<page name="setting">
		<com.tms.collab.rss.ui.ItemListNonAutoForm name="ListItem"/>
	</page>
</x:config>

<script>
 	function isSave() {
		if(confirm("Are you sure want to save the record?")) {
			return true;
		} else{
			return false;
		}
		return false
 	}
</script>
<c-rt:set var="forwardSuccess" value="<%=ItemListNonAutoForm.FORWARD_SUCCESS %>" />
<c-rt:set var="forwardClose" value="<%=ItemListNonAutoForm.FORWARD_CLOSE %>" />

<c:if test="${!empty param.moduleId}">
	<x:set name="setting.ListItem" property="moduleId" value="${param.moduleId}" />
</c:if>
<c:if test="${forward.name eq forwardSuccess}">
    <script>
    	window.close();
    </script>
</c:if>
<c:if test="${forward.name eq forwardClose}">
    <script>
    	window.close();
    </script>
</c:if>

<script>
	function checkAll(count){
		var isChecked = document.forms['setting.ListItem'].elements['setting.ListItem.CheckAllItem'].checked;
		if (isChecked == true){
			for (i=0 ; i < count ;i++){
				document.forms['setting.ListItem'].elements['setting.ListItem.itemPanel.checkItem'+i].checked = true;
			}
		} else {
			for (i=0 ; i < count ;i++){
				document.forms['setting.ListItem'].elements['setting.ListItem.itemPanel.checkItem'+i].checked = false;
			}
		}
	}
</script>
<link rel="stylesheet" href="/ekms/images/ekp2005/default.css">
<table  cellpadding="4" cellspacing="1" width="100%">
	<tr>
	    <td class="contentTitleFont" style="padding:5px;"><fmt:message key='rss.channel.rss'/> > <fmt:message key='rss.channel.addItem'/></td>
	</tr>
	<tr>
    	<td class="contentBgColor">
    		<x:display name="setting.ListItem" />
    	</td>
	</tr>
</table>
</x:permission>
<%@include file="/ekms/includes/footer.jsp" %>


