<%@ page import="java.net.URLEncoder"%>
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
		<tr>
	    	<td class="contentTitleFont"><fmt:message key='po.label.attachments'/></td>
	    </tr>
	    <tr>
	        <td class="classRowLabel" valign="top" align="left">
		        <blockquote>
				<br>
				   	<c:set var="map" value="${sessionScope.attachmentMap}" />
				       	<c:if test="${empty map}">
				       		<fmt:message key='po.label.selectFiles'/>
				         <br><br>
				      	</c:if>
				   	 	<c:if test="${!empty map}">
				      		<fmt:message key='po.label.currentlyAttached'/>
				           	<br><br>
				            <c:forEach var="a" items="${map}">
				                <c:set var="key" value="${a.key}" />
				                <%
				                    String key = (String) pageContext.getAttribute("key");
				                    key =  URLEncoder.encode(key, "UTF-8");
				                    pageContext.setAttribute("encodedKey", key);
				                %>
				                <li><c:out value="${a.key}" /> [<x:event name="${w.absoluteName}"
				    	            type="remove" url=""
					                param="key=${encodedKey}">Delete</x:event>]</li>
					        </c:forEach>
							<br>
						</c:if>
				 </blockquote>
	         </td>
        </tr>	
        <tr>
	       	<td class="contentTitleFont">
	       		<img src="<c:url value="/ekms/images/blank.gif" />" width="5" height="10">
	       	</td>
	   </tr>
	   <tr>
	       <td class="contentTitleFont">
	       		<img src="<c:url value="/ekms/images/blank.gif" />" width="5" height="10">
	       </td>
	   </tr>
	   <tr>
	   		<td>
       			<table width="100%" cellpadding="3" cellspacing="1">
                	<jsp:include page="../form_header.jsp" flush="true"/>
	                	<tr>
                  			<td class="classRowLabel" width="20%" valign="top" align="right"><b>
                  				<fmt:message key='po.label.attachment'/> 1
                  			</b></td>
                 			<td class="classRowLabel" width="80%" valign="top">
                    			<x:display name="${w.attach1.absoluteName}" />
                  			</td>
                		</tr>
		                <tr>
		                  <td class="classRowLabel" width="20%" valign="top" align="right"><b>
		                  <fmt:message key='po.label.attachment'/> 2</b></td>
		                  <td class="classRowLabel" width="80%" valign="top">
		                    <x:display name="${w.attach2.absoluteName}" />
		                  </td>
		                </tr>
		                <tr>
		                  <td class="classRowLabel" width="20%" valign="top" align="right"><b>
		                    <fmt:message key='po.label.attachment'/> 3</b></td>
		                  <td class="classRowLabel" width="80%" valign="top">
		                    <x:display name="${w.attach3.absoluteName}" />
		                  </td>
		                </tr>
		                <tr>
		                  <td class="classRowLabel" width="20%" valign="top" align="right"><b>
		                    &nbsp; </b></td>
		                  <td class="classRowLabel" width="80%" valign="top">
		                    <x:display name="${w.upload.absoluteName}" />
		                    <x:display name="${w.done.absoluteName}" />
		                  </td>
		                </tr>
		                <tr> </tr>
                	<jsp:include page="../form_footer.jsp" flush="true"/>
                </table>
        	</td>
		</tr>
		</table>
	</td>
</tr>
</table>
