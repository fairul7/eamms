<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="children" value="${widget.children}"/>

<c:forEach var="co" items="${children}">
    <fmt:message key="cms.label.icon_${co.className}"/>
    <c:choose>
      <c:when test="${co.className=='com.tms.cms.page.Page'}">
        <span class="contentChildName"><c:out value="${co.name}"/></span>  
      </c:when>
      <c:otherwise>
        <a href="content.jsp?id=<c:out value="${co.id}"/>" class="contentChildName" <c:if test="${co.className=='com.tms.cms.bookmark.Bookmark'}"> target="<c:out value='${co.contentObject.target}'/>"</c:if>><c:out value="${co.name}"/></a>
      </c:otherwise>
    </c:choose>
    <br>
    <c:if test="${! empty co.author}"><span class="contentChildAuthor"><c:out value="${co.author}"/></span> - </c:if>
    <span class="contentChildDate"><fmt:formatDate pattern="${globalDateLong}" value="${co.date}"/></span>
    <%--<span class="contentChildAuthor"> | View: xx </span>--%>
    <p>
    <span class="contentChildSummary">
    <c:out value="${co.summary}" escapeXml="false" />
    </span>
    </p>
</c:forEach>

<%-- Paging --%>
        <c:if test="${widget.pageCount > 1}">
        <p class="contentPaging" align="right">
         <fmt:message key='general.label.page'/> :
            <c:forEach var="pg" begin="1" end="${widget.pageCount}" varStatus="stat">
               
                
                <c:if test="${stat.index > 1    }  "> | </c:if>
                  
                  <c:if test="${(widget.page - stat.index )<=6  && (stat.index-widget.page )<= 6  }">
                	<c:choose>
                    <c:when test="${pg == widget.page}"><span class="contentPageLink"><b><c:out value='${pg}'/></b></span></c:when>
                    <c:otherwise>
                    	<c:if test="${(widget.page - stat.index )==6    }">
                  	     
                  	    <a class="contentPageLink" href="content.jsp?id=<c:out value='${widget.id}'/>&page=<c:out value='${pg}'/>"><b><fmt:message key='general.label.previous'/></b></a>
                        
                        </c:if>
                        
                        <c:if test="${(stat.index-widget.page )== 6   }">
                  	      ...
                         <fmt:message key='general.label.of'/> <c:out value="${widget.pageCount}"/>
                        <a class="contentPageLink" href="content.jsp?id=<c:out value='${widget.id}'/>&page=<c:out value='${pg}'/>"><b><fmt:message key='general.label.next'/></b></a>
                       
                        
                        </c:if>    
                        
                        <c:if test="${(widget.page - stat.index ) !=6  && (stat.index-widget.page ) != 6  }">
                  	     <a class="contentPageLink" href="content.jsp?id=<c:out value='${widget.id}'/>&page=<c:out value='${pg}'/>"><c:out value='${pg}'/></a>
                        </c:if>
                    </c:otherwise>
                	</c:choose>
                </c:if>
            </c:forEach>
          
        </p>
        </c:if>



<script>
    	
    	function feedToURL(){
    	
    		var origURL = ""+window.document.location;
    		if( origURL.indexOf("&page=") >0 )
    		{
    			origURL= origURL.substring(0, origURL.indexOf("&page=") );
    			
    		}
    		
    		
    		if(document.getElementById("gotopagetext").value  !='' ){
    			origURL +="&page="+document.getElementById("gotopagetext").value;
    		    var totalPage= parseInt('<c:out value="${widget.pageCount}"/>');
    		    if(  parseInt( document.getElementById("gotopagetext").value) > totalPage )
    			{
    			 alert("<fmt:message key='general.label.warningpagecountexceeded'/>");
    			}
    			else
    			window.location=""+origURL;
    		
    		}
    	
    	}
    	
    	
    	
    	function filterInput(filterType, evt, allowDecimal, allowCustom){
    var keyCode, Char, inputField, filter = '';
    var alpha = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var num   = '0123456789';
    // Get the Key Code of the Key pressed if possible else - allow
    if(window.event){
        keyCode = window.event.keyCode;
        evt = window.event;
    }else if (evt)keyCode = evt.which;
    else return true;
    // Setup the allowed Character Set
    if(filterType == 0) filter = alpha;
    else if(filterType == 1) filter = num;
    else if(filterType == 2) filter = alpha + num;
    if(allowCustom)filter += allowCustom;
    if(filter == '')return true;
    // Get the Element that triggered the Event
    inputField = evt.srcElement ? evt.srcElement : evt.target || evt.currentTarget;
    // If the Key Pressed is a CTRL key like Esc, Enter etc - allow
    if((keyCode==null) || (keyCode==0) || (keyCode==8) || (keyCode==9) || (keyCode==13) || (keyCode==27) )return true;
    // Get the Pressed Character
    Char = String.fromCharCode(keyCode);
    // If the Character is a number - allow
    if((filter.indexOf(Char) > -1)) return true;
    // Else if Decimal Point is allowed and the Character is '.' - allow
    else if(filterType == 1 && allowDecimal && (Char == '.') && inputField.value.indexOf('.') == -1)return true;
    else return false;
}
    	
</script>

<tr>
	 <td valign=top align=left colspan=3><p class="contentPaging" align="right">
	   <c:if test="${widget.pageCount > 1}">
	        <input 
		style="background-color:#FFFFFF; border-width:1pt; border-style:solid; border-color:#CCCCCC; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt; font-weight:normal"
       type="text" name="gotopagetext" 
       id="gotopagetext"
       maxlength="5"
       size="5"
       onKeyPress="return filterInput(1, event)" 
    >
    
    
    <input 
    class="button"
    type="button"
    name="pageButton"
    value="Go"
    onClick="feedToURL();"
    >
    
    </c:if>
    </p>
	</td>
</tr>
