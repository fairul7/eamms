<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<form name="articleHistory" method="post" action="history.jsp">
<input type="hidden" name="articleId" value="<c:out value='${param.articleId}'/>"/>
<table width="100%" cellpadding="2" cellspacing="1"> 

<script>
	function gotoLocation(){
		document.location='recentChanges.jsp?articleId=<c:out value="${widget.article.articleId}"/>';	
	}	
</script>

 <tr>  
    <td align="left" width="25%"><a href="viewArticle.jsp?articleId=<c:out value='${widget.article.articleId}'/>" class="contentChildName"> <c:out value="${widget.article.title}"/></a> </td>        
    <td width="25%">&nbsp;</td>
    <td align="right" width="50%" class="tsBody3">created on <fmt:formatDate pattern="dd MM yyyy" value="${widget.article.createdOn}"/></td>
</tr>   

<tr>    
    <td width="25%" colspan="3">&nbsp;</td>
</tr>     
<c:forEach items="${widget.articleHistory}" var="history" varStatus="status">    
		
		<tr>														
			<td class="font_date" colspan="3">
			<input type="radio" name="revisionId" value="<c:out value='${history.revisionId}'/>"
			<c:if test="${history.revisionId == history.currentRevision}">
				checked
			</c:if>
			/>
			<c:if test="${history.revisionId == history.currentRevision}">
				<font color="#FF0000">
			</c:if>
			 Modified by <c:out value='${history.userName}'/>&nbsp; on <i><fmt:formatDate pattern="dd MM yyyy" value='${history.modifiedOn}'/> at <fmt:formatDate pattern=" HH:mm" value='${history.modifiedOn}'/> Hrs &nbsp; 
			 <c:if test="${not empty history.editSummary}">
			 (<c:out value="${history.editSummary}"/>)
			 </c:if>
			 </td>			 
			 <c:if test="${history.revisionId == history.currentRevision}">
				</font>
			</c:if>
        </tr>	
                  
</c:forEach>
 		<tr>
			<TD valign="top" align="right" class=lightgrey colSpan=3>
			<input type="submit" class="button" value="Rollback"/> 							
		</tr>       
 </table>
 </form>
 
 
<TABLE class="lightgrey" width="100%" cellspacing="1" >
	<tfooter>
		  <tr>
		    <td colspan="<c:out value="${colspan}"/>" align="right">
		        <c:set var="pageCount" value="${widget.pageCount}"/>
		
		        <c:if test="${widget.currentPage > 1}">
		        [<x:event name="articlesPaging" type="page" param="articleId=${widget.articleId}&page=${widget.currentPage - 1}">&lt;</x:event>]
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
		                location.href='?articleId=<c:out value="${widget.articleId}"/>&page=' + page;
		            }
		        //-->
		        </script>
		
		        <c:if test="${widget.currentPage < pageCount}">
		        [<x:event name="articlesPaging" type="page" param="articleId=${widget.articleId}&page=${widget.currentPage + 1}">&gt;</x:event>]
		        </c:if>
		    </td>
		  </tr>
		</tfooter>
</table>