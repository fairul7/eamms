<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="quota" value="${widget}"/>
<c:set var="usedPercentage" value="${quota.usedSpace1 / quota.quota1 * 100}"/>
<c:set var="leftPercentage" value="${100 - usedPercentage}"/>

	<table width="100%" cellspacing="0" cellpadding="0">
		<tr align="middle">
			
			<td  colspan="2">
			
				<c:choose >
					
					<c:when test="${quota.usedSpace1 > quota.quota1}">
						<fmt:message key='mf.label.quota.spaceExceed1'/><br/>
						<fmt:message key='mf.label.quota.spaceExceed2'/> <fmt:formatNumber value="${quota.usedSpace1/1024}" maxFractionDigits="2" /> <fmt:message key='mf.label.of'/> 
						<fmt:formatNumber value="${quota.quota1/1024}" maxFractionDigits="2" /> MB 
					</c:when>
					
					<c:otherwise>
						<fmt:formatNumber value="${quota.usedSpace1/1024}" maxFractionDigits="2" /> <fmt:message key='mf.label.of'/>  
						<fmt:formatNumber value="${quota.quota1/1024}" maxFractionDigits="2" /> MB 
						( <fmt:formatNumber value="${usedPercentage}" maxFractionDigits="2" minFractionDigits="1" />% )
					</c:otherwise>
				
				</c:choose>
			
			</td>
			
		</tr>

<c:if test="${quota.usedSpace1 < quota.quota1}">
		
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		
		<c:choose>
			<c:when test="${usedPercentage == 0}">
				<c:set var="color" value="${'#C0C0C0'}"/>
			</c:when>
			<c:otherwise>
				<c:set var="color" value="${'red'}"/>
			</c:otherwise>
		</c:choose>
		
		<!-- display the status bar -->
		<tr style="height:5">
			<td width="<fmt:formatNumber value="${usedPercentage}" maxFractionDigits="2" minFractionDigits="1" />%" bgcolor="<c:out value="${color}"/>">
			</td>
			<td width="<fmt:formatNumber value="${leftPercentage}" maxFractionDigits="2" minFractionDigits="1" />%" bgcolor="#C0C0C0">
			</td>
		</tr>

</c:if>

	</table>
	
<br/>