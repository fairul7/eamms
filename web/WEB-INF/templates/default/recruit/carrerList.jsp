<%@ include file="../recruit/fileHeader.jsp" %>

<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor" border="0">
<tr>
	<td width="100%"><c:out value="${form.employeeOpprMsg}" escapeXml="false"/></td>
</tr>
<tr>
<td> 
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground" border="0">
<tr>
	<td>
		
		<c:out value="${form.noOpprMsg}" />
		
		<table width="100%" cellpadding="3" cellspacing="1">
		
		<c:set var="i" value="${0}"/>
		<c:forEach items="${form.carrerCol}" var="coll">
		<c:if test="${i eq 0}" >
    		<tr>
    	</c:if>
        <c:if test="${i%3 eq 0 && i!=0}"><%-- 4 mean 3 column TD --%>
    		</tr>
    		 	<tr>	
    	</c:if>	
		    
			<td class="classRow" valign="top"><c:out value="${equalNoOfPosition}" />
        	<c:choose> 
				<c:when test="${!coll.equalNoOfPosition && coll.vacancyCode!=null}" >
					<a href="careerView.jsp?vacancyCode=<c:out value='${coll.vacancyCode}' />"><c:out value='${coll.carrerName}'  escapeXml="false" /> | Viewed <i>(<c:out value='${coll.totalViewed}' />)</i>
					<br /><c:out value='${coll.noOfPosition}' /> <fmt:message key="recruit.general.label.positions"/></a>
				</c:when>
				<c:otherwise>
					<c:out value='${coll.carrerName}'  escapeXml="false" />		
				</c:otherwise>
			</c:choose>
        	</td>
        
   		<c:set var="i" value="${i+1}"/>
   		<c:set var="total" value="${i}"/>	
		</c:forEach>
		
		<c:if test="${i eq total}">
        	</tr>
        </c:if>	
		
		</table>
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

