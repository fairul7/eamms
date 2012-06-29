<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>
<c:set var="vehicleReq" value="${widget.request}"/>


<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
<table width ="100%" class="contentBgColor">
<tr width ="100%">
    <td align="center" width ="100%">
        <b></b>
    </td>
</tr>
<tr>
    <td align="right" >    	
       
         
    </td>
</tr>


<tr>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="60%" class="borderTable">
            <tr align="center"  class="tableHeader" height="20">
                <th width="5%">No</th>
                <th width="20%">Category</th>
                <th width="5%">Qty</th>
                <th width="20%">Driver Required</th>  
                <th width="10%">&nbsp;</th>      
                
            	</td> 
            </tr>    
            
            
            <% int i=1;%>      
            <c:set var="x" value="0"></c:set>   
			<c:forEach items="${vehicleReq}" var="req" >
            
             <tr align="center" class="tableRow">
                <td valign="top" class="classRow">
                	<c:out value="<%=i%>"/>               
                </td>
                
                <td valign="top" class="classRow">
                	<c:out value="${req.name}" />               
                </td>   
                
                <td valign="top" class="classRow">
                	<c:out value="${req.quantity}" />               
                </td>   
                
                <td valign="top" class="classRow">
                	<c:out value="${req.driver}" />               
                </td>    
                
                <td valign="top" class="classRow">      	
		 				<x:display name="${form.deleteButton[x].absoluteName}" />					
				</td> 	
				
	              <% i++;%>
	              <c:set var="x" value="${x+1}"></c:set>
	            </c:forEach>
                
                			 
			</tr>     
			
			
           
           <tr align="center" class="tableRow" >
            	<td valign="top" class="classRow">  
            		<c:out value="<%=i%>"/>          
            	</td>
            	<td valign="top" class="classRow">           	
	          		 	
				 			<x:display name="${form.category.absoluteName}" />
							<br>				
	        	</td>
	        	
	        	<td valign="top" class="classRow">  
				 			<x:display name="${form.qty.absoluteName}" />
							<br>				
            	</td>
            	
            	<td valign="top" class="classRow">   	
				 			<x:display name="${form.driver.absoluteName}" />
							<br>				 
            	</td>
            	
            	<td valign="top" class="classRow">  	
            		<x:display name="${form.addButton.absoluteName}" />			
            	</td>        	
            	
            </tr>
            
    
    </table>

    </td>
</tr>
<tr>
    <td>
    </td>
</tr>
</table>