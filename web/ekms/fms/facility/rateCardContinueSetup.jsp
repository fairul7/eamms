<%@include file="/common/header.jsp" %>

<x:config>
     <page name="rateCardContinueSetup">
        <com.tms.fms.facility.ui.RateCardContinueForm name="ratecardform"/>
     </page>
</x:config>

<c:if test="${forward.name == 'invalidRateCard'}" >
	<script>
		alert("<fmt:message key='fms.facility.msg.rateCardInvalid'/>");
		location = "rateCardList.jsp";
	</script>
</c:if>
<c:if test="${forward.name == 'continue'}" >
  <c:redirect url="rateCardList.jsp"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<script language="JavaScript">
	function pops(nama,jangkar,h,w){  
		self.name = nama;
		var nX = (screen.availWidth - w)/2;
		var nY = (screen.availHeight - h)/2;
		viewWin = window.open(jangkar , "viewWin", 'height='+ h + ', width='+ w + 
			', screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +
			', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
		viewWin.focus();  
	}

	function closeRemote() {	
		window.opener.location.reload();
		timer = setTimeout('window.close();', 10);		
	}

	<!--script>closeRemote();</script-->
</script>

<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='general.label.systemAdministration'/> > <fmt:message key='fms.label.rateCardSetup'/>  
        </td>
    	<td align="right" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
    		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="rateCardContinueSetup" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
