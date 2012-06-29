<%@include file="/common/header.jsp" %>

<x:config>
     <page name="assignManpower">
		<com.tms.fms.engineering.ui.AssignManpowerForm name="form" width="100%"/>
     </page>
</x:config>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>
<c:if test="${not empty(param.action)}" >
	<c:set var="requestId" value="${widgets['assign.form'].requestId}"/>
    <script>
    	window.opener.location='requestAssignmentDetails.jsp?requestId=<c:out value="${requestId}" />';
        window.close();
    </script>
</c:if>
<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['assignManpower.form'].id}"/>
  </c:otherwise>
</c:choose>
<c:if test="${not empty(param.unitId)}" >
	<x:set name="assignManpower.form" property="unitId" value="${param.unitId}"/>
</c:if>

<c:choose>
  <c:when test="${not empty(param.act)}">
    <c:set var="act" value="${param.act}"/>
	<x:set name="assignManpower.form" property="act" value="${act}"/>
  </c:when>
</c:choose>

<c:choose>
<c:when test="${not empty(selectedKeys)}">
<c:forEach var="key" items="${selectedKeys}" varStatus="i">				
	<c:set var="selectedKeys" value="${selectedKeys}"/>
</c:forEach>
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>

<x:set name="assignManpower.form" property="id" value="${id}"/>


<%@include file="/ekms/includes/linkCSS.jsp" %>
  
<jsp:include page="includes/header.jsp" />
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
<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.label.manpowerAssignment'/>  
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
			<x:display name="assignManpower.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />
