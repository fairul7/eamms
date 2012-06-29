<%@ include file="/common/header.jsp" %>
<c:set var="cPanel" value="${widget}"/>

<c:set var="cPanel_indicator" value="+"/>
<c:set var="cPanel_display" value="none"/>
<c:if test="${not (cPanel.collapsed)}">
	<c:set var="cPanel_indicator" value="-"/>
	<c:set var="cPanel_display" value="block"/>
</c:if>

<script>
	function togglePanel_<c:out value="${cPanel.absoluteNameForJavaScript}"/>() {
		var section = document.getElementById("<c:out value="${cPanel.absoluteNameForJavaScript}"/>_section");
		var indicator = document.getElementById("<c:out value="${cPanel.absoluteNameForJavaScript}"/>_indicator");
		
		if (section.style.display == "none") {
			section.style.display = "block";
			indicator.innerHTML = "-";
		} else {
			section.style.display = "none";
			indicator.innerHTML = "+";
		}
	}
</script>

<div<c:if test="${cPanel.displayCollapseButton}"> class="collapsiblePanelBar" onclick="togglePanel_<c:out value="${cPanel.absoluteNameForJavaScript}"/>()"</c:if>>
	<table valign="top" width="100%" border="0" cellspacing="5" cellpadding="0">
	<tr valign="middle">
		<td height="22" bgcolor="#003366" class="contentTitleFont">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td><b><font color="#FFCF63" class="contentTitleFont">	&nbsp;  <c:out value="${cPanel.title}"/></font></b></td>
				<td align="right">
					<c:if test="${cPanel.displayCollapseButton}">
					<b>
						<font color="#FFCF63" class="contentTitleFont"> [ <span id="<c:out value="${cPanel.absoluteNameForJavaScript}"/>_indicator"><c:out value="${cPanel_indicator}"/></span> ] &nbsp; 
						</font>
					</b>
					</c:if>
				</td>
			</tr>	
			</table>
		</td>
	</tr>
	</table>
</div>

<div id="<c:out value="${cPanel.absoluteNameForJavaScript}"/>_section" style="display:<c:out value="${cPanel_display}"/>">