<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="articles" value="${widget.articles}"/>
<TABLE style="TEXT-ALIGN: center" cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
           <TR>
                <TD vAlign=top>                
				  <TABLE class=siteBodyHeader cellSpacing="1" cellPadding="1" width="100%" border=0>
                    <TBODY>
                    <TR>
                     <TD vAlign=center align=left><FONT 
                        class=contentName>&nbsp;Advance Search</FONT></TD>
                      </TR></TBODY>
				</TABLE>
			  </TD></TR>
              <TR>
                <TD style="VERTICAL-ALIGN: top" class="lightgrey"> 	        			
					<form name="advSearchForm" method="post" action="advanceSearchResults.jsp">    
        			<TABLE style="TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="100%" border=0>

				        <TBODY>
				        	<tr>
				        		<TD valign="top" align="left" class=lightgrey><font class="tsText">Search :</font>
								<input type="text" name="keyword"  class="font_text" value="">
								</td>
						   	</tr>		              
				
					        <tr>
					            <TD valign="top" align="left" class=lightgrey >	
					            <font class="tsText">Search Criteria : &nbsp;&nbsp; 
					            <input type="checkbox" name="category" value="category">Category &nbsp;&nbsp; 
					            <input type="checkbox" CHECKED name="tags" value="tags"> Tags&nbsp;&nbsp;					            
					            <input type="checkbox" CHECKED name="title" value="title"> Title &nbsp;&nbsp;
					            <input type="checkbox" name="article" value="article"> Article &nbsp;&nbsp;
					            </font></td>
					        </tr>
					        
					        <tr>
						        <td valign="top" align="left" class=lightgrey>
						        <input class="button" type="submit" name="adv search" value="Search">
						        </td>
					        </tr>	        			        						                
				                   
		           </TBODY>
				   </TABLE>					   			   
				   </form>
        		</td></tr>
        </tbody>     
        </table>
<c:if test="${!empty articles}">


<TABLE class="lightgrey" width="100%" cellspacing="1" >
	<TR>
        <TD vAlign=top>                
		  <TABLE class=siteBodyHeader cellSpacing=0 cellPadding=0 width="100%" border=0>
            <TBODY>
            <TR>
             <TD vAlign=center align=left ><FONT 
                class=contentName>&nbsp;Advance Search Results</FONT></TD>
              </TR></TBODY>
		</TABLE>
	  </TD></TR>
</TABLE>
</c:if>
<c:forEach items="${articles}" var="article" varStatus="status">
<TABLE class="lightgrey" width="100%" cellspacing="1" >
    <tr>
        <td align="left" width="25%"><a href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" class="article_title"> <c:out value="${article.title}"/></a> </td>        

    </tr>    
          
     <tr>
    	<td colspan="3" class="font_text"><p><c:out value="${article.storySummary}" escapeXml="false"/>...(more)</p></td>
    </tr>   

    <tr>
	    <td colspan="3" class="font_text"><hr></td>
    </tr>

	</table>
</c:forEach>

<c:if test="${not empty articles}">
<TABLE class="lightgrey" width="100%" cellspacing="1" >
	<tfooter>
		  <tr>
		    <td colspan="<c:out value="${colspan}"/>" align="right">
		        <c:set var="pageCount" value="${widget.pageCount}"/>
		
		        <c:if test="${widget.currentPage > 1}">
		        [<x:event name="articlesPaging" type="page" param="keyword=${widget.keyword}&page=${widget.currentPage - 1}">&lt;</x:event>]
		        </c:if>
		
		        
		        <select id="<c:out value='pageBox'/>" name="<c:out value='pageBox'/>" onchange="goToPage('pageBox')">
		        <c:set var="end" value="${pageCount}" />
		            <c:forEach begin="1" end="${end}" var="pg">
		              <option<c:if test='${pg == widget.currentPage}'> selected</c:if>><c:out value="${pg}" /></option>
		            </c:forEach>
		        </select>
		        <script>
		        <!--
		            function goToPage(id) {
		                var selectbox = document.getElementById(id);
		                var page = selectbox.options[selectbox.selectedIndex].text;
		                location.href='?keyword=${widget.keyword}&page=' + page;
		            }
		        //-->
		        </script>
		
		        <c:if test="${widget.currentPage < pageCount}">
		        [<x:event name="articlesPaging" type="page" param="keyword=${widget.keyword}&page=${widget.currentPage + 1}">&gt;</x:event>]
		        </c:if>
		    </td>
		  </tr>
		</tfooter>
</table>
</c:if>


