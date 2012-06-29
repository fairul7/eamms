<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<script language="javascript" src="/common/tree/tree.js"></script>
<script language="javascript" src="/common/WCH.js"></script>
<link type="text/css" rel="stylesheet" href="/ekms/images/fms2008/default.css">
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<!--<!--[if lte IE 6]--><div style="display:none">This function actually allow the CSS styling in IE</div>
<style>
	div.rowinOffline {background-color:#E5E5E5; border-width:thin; border-color:#CCCCCC;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;}
	div.rowoutOffline {background-color:#FFFFFF; border-width:thin;color:#AEAEAE;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;}
	div.rowinOnline {background-color:#DEF2FF; border-width:thin; border-color:#CCCCCC;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;}
	div.rowoutOnline {background-color:#FFFFFF; border-width:thin;color:#00ADE6;font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;}
</style>

<div>
<img src="/ekms/images/ekp2005/hd_messenger.gif">
</div>
<table width="150px">
	<tr><td colspan="4" align="left" height="2"><img src="/ekms/images/blank.gif" width="10" height="2"></td></tr>
	<tr>
		<td height="20" width="2" align="left"><img src="/ekms/images/blank.gif" width="2" height="5"></td>
		<td height="20" valign="top" align = "left" width="5"><img src="/ekms/images/ekp2005/ic_online.gif"></td>
		<td height="20" valign="top" align = "left" nowrap>
			<a class="messengerLeftBar" href="#" onclick="treeToggle('online'); return false"><span id="click_online"><font class="TextBlueOnline"><fmt:message key="com.tms.messenger.panelSideBar.onlineUsers"/></font></span></a>        
			<span id="online" style="display: block">
				<table width="100%">
					<tr>
						<td id = "onlineUser" height="20" valign="top">  
							<c:forEach var="user" items="${widget.onlineUsers}">
								<div class="onlineuser" style="cursor:pointer;" id="<c:out value="${user.id}"/>" onclick="createAccor('<c:out value="chtwnd_${user.name}"/>','<c:out value="${widget.currentUser.id}"/>','','','<c:out value="${widget.currentUser.username}"/>','<c:out value="${user.id}"/>','false','true')" width="50px" style="padding:0px;" class="rowoutOnline" onmouseover="this.className='rowinOnline';Tip('<c:out value="${user.name}"/>')" onmouseout="this.className='rowoutOnline';"><img src="/ekms/images/ekp2005/dot_blue.gif"><c:out value="${user.username}"/></div>
							</c:forEach>
						</td>
					</tr>
				</table>
			</span>
		</td>
	</tr>
	<tr><td colspan="4" align="left" height="2"><img src="/ekms/images/blank.gif" width="10" height="2"></td></tr>
	<tr>
		<td height="20" width="2" align = "left"><img src="/ekms/images/blank.gif" width="2" height="5"></td>
		<td height="20" valign="top" width="5"><img src="/ekms/images/ekp2005/ic_offline.gif"></td>
		<td height="20" valign="top" align = "left" nowrap>
		<a class="messengerLeftBar" href="#" onclick="treeToggle('offline'); return false"><span id="click_offline"><font class="TextGreyOnline"><fmt:message key="com.tms.messenger.panelSideBar.offlineUsers"/></font></span></a>
			<span id="offline" style="display: none"> 			
				<table width="100%">
					<tr>
						<td id = "offlineUser" height="20" valign="top"> 
							<c:forEach var="user" items="${widget.offlineUsers}">
								<div class="offlineuser" style="cursor:pointer;" id="<c:out value="${user.id}"/>" onclick="createAccor('<c:out value="chtwnd_${user.name}"/>','<c:out value="${widget.currentUser.id}"/>','','','<c:out value="${widget.currentUser.username}"/>','<c:out value="${user.id}"/>','false','true')" width="50px" style="padding:0px;" class="rowoutOffline" onmouseover="this.className='rowinOffline';Tip('<c:out value="${user.name}"/>')" onmouseout="this.className='rowoutOffline';"><img src="/ekms/images/ekp2005/dot_cross.gif"><c:out value="${user.username}"/></div>
							</c:forEach>									
						</td>
					</tr>
				</table>		
            </span>
        </td>
	</tr>
	<tr><td colspan="4" align="left" height="2"><img src="/ekms/images/blank.gif" width="10" height="2"></td></tr>
	<tr>
		<td height="20" width="2" align = "left"><img src="/ekms/images/blank.gif" width="2" height="5"></td>
		<td height="20" valign="top" width="5"><img src="/ekms/images/ekp2005/ic_group.gif"></td>
		<td height="20" valign="top" align = "left">
		<a class="messengerLeftBar" href="#" onclick="treeToggle('groupChat'); return false"><span id="click_groupChat"><font class="TextGreyOnline"><fmt:message key="com.tms.messenger.panelSideBar.groupChat"/></font></span></a>
 			<script type="text/javascript" src="/common/scripts/wz_tooltip.js"></script>
			<c:set var="counter" value="${1}"/>
			<span id="groupChat" style="display: block"> 			
				<table cellspacing=2 cellpadding=1 width="100%">
					<tr>
						<td id = "groupChatConversation" height="20" valign="top">							
							<c:forEach var="group" items="${widget.groupUsers}">
								<a href ="#" class = "groupClass" id="<c:out value="${group.chatId}"/>" onclick="createAccor('<c:out value="Group No.${counter}"/>','<c:out value="${widget.currentUser.id}"/>','','<c:out value="${group.chatId}"/>','<c:out value="${widget.currentUser.username}"/>','','false','true')" onmouseover="Tip('<c:forEach var="groupUsersName" items="${group.info}"><c:out value="${groupUsersName.user.username}"/>,</c:forEach> ')"><fmt:message key="com.tms.messenger.panelSideBar.groupNo"/><c:out value="${counter}"/></a><br/>
								<c:set var="counter" value="${counter+1}"/>
							</c:forEach>	
						</td>
					</tr>
				</table>		
            </span>
        </td>
	</tr>				
</table>

<script>
var counter ='<c:out value="${counter}"/>';
</script>