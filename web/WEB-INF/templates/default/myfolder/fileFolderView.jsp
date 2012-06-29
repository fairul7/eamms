<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<c:set var="f" value="${w.fileFolder}"/>

<c:choose>

	<c:when test="${f != null}">
	
			<table width="100%" border="0" cellpadding="5" cellspacing="1">
			
			    <%--name--%>
			    <tr>
				    <td width="10%"><fmt:message key='mf.label.fileName'/></td>
				    <td width="70%">
						<c:out value="${f.fileName}" escapeXml="${false}"/>
				    </td>
			    </tr>
				
				<%--file type--%>
				<tr>
				    <td ><fmt:message key='mf.label.fileType'/></td>
				    <td>
						<c:out value="${f.fileType}"/>
				    </td>
			    </tr>
				
			    <%--description--%>
			    <tr>
				    <td ><fmt:message key='mf.label.desc'/></td>
				    <td>
				    	<c:choose>
				    		<c:when test="${f.fileDescription == ''}">
				    			<c:out value="${'--- No description ---'}"/>
				    		</c:when>
				    		<c:otherwise>
				    			<c:out value="${f.fileDescription}"/>
				    		</c:otherwise>
				    	</c:choose>
				    </td>
			    </tr>
			
			<c:if test="${f.fileType != 'Folder'}">
<!--				<tr>
					<td ></td>
					<td>
						<x:event name="${w.absoluteName}" type="download" param="id=${param.id}">
							Download
						</x:event>
					</td>
				</tr> -->
				<tr>
					<td ></td>
					<td>
						<a href="<c:url value="/myfolder/downloadFile" />?id=<c:out value='${f.id}'/>">
							Download
						</a>
					</td>
				</tr> 
			</c:if>
			
			
			<c:if test="${f.fileType == 'Folder'}">
				<tr>
				    <td valign="top"><fmt:message key='mf.label.folderAccess'/></td>
				    <td >
				        <c:choose>
				        	<c:when test="${f.fileAccess == '1'}">
				        		<fmt:message key='mf.label.privateAccess'/>
				        	</c:when>
				        	<c:when test="${f.fileAccess == '2'}">
				        		<fmt:message key='mf.label.publicAccess'/>
				        	</c:when>
							<c:otherwise>
				        		<fmt:message key='mf.label.selectedAccess'/><br/>
				        		<ul>
				        		<c:forEach var="item" items="${w.selectedUsers}">
				        			<li><c:out value="${item}"/></li>
				        		</c:forEach>
				        		</ul>
							</c:otherwise>
				        </c:choose> 
				    </td>
			    </tr>
			</c:if>
			</table>
			
			<br/>
			
			<c:if test="${w.canEdit}">
				<input type="button" class="buttonClass" value="<fmt:message key="mf.label.edit"/>" onclick="edit()">
				<input type="button" class="buttonClass" value="<fmt:message key="mf.label.cancel"/>" onclick="goBack()">
			</c:if>
			
	</c:when>
	
	<c:otherwise>
			<h2><fmt:message key='mf.message.unauthorize1'/></h2>
			<hr size="1">
			<fmt:message key='mf.message.unauthorize2'/>
	</c:otherwise>

</c:choose>





