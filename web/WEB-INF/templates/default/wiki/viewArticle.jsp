<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="article" value="${widget.article}"/>
<script>
	function gotoLocation(){
		document.location='editArticle.jsp?articleId=<c:out value="${article.articleId}"/>';	
	}	
	function gotoLocation(comments){
		if(comments == 'leaveComments'){
			window.open('leaveComments.jsp?articleId=<c:out value="${article.articleId}"/>', 'mywindow', 'status=yes,resizable=yes,scrollbars=yes,width=600, height=500');
		}else {
			document.location='editArticle.jsp?articleId=<c:out value="${article.articleId}"/>';
		}			
	}	
	function gotoHistory(){
		document.location='history.jsp?articleId=<c:out value="${article.articleId}"/>';		
	}
</script>

<table width="100%" cellspacing="1">
    <tr>
        <td align="left" width="60%" class=contentName><c:out value="${article.title}"/> </td>        
        <td class="font_text">Category: <a href="categoryTagSearch.jsp?keyword=<c:out value='${article.categoryId}'/>&type=category" class="font_category"><c:out value="${article.category}"/></a><i></td>        
    </tr>
    <tr>
		<td align="left" width="60%" class="tsBody3">created on <fmt:formatDate pattern="dd MM yyyy" value="${article.createdOn}"/></td>
        <td class="font_text">Tags: <c:out value="${article.displayTags}" escapeXml="false"/></td>
    </tr>   
    
    <tr><td colspan='2'>&nbsp;</td></tr>
    
    <tr>
    	<td colspan="3" class="contentBody"><c:out value="${article.storyToHtml}" escapeXml="false"/></td>
    </tr>
    <tr>
        <td colspan="3" align="right" width="25%">
        <input type="button" onClick="gotoHistory();" class="button" value="History"/> 
        <input type="button" onClick="gotoLocation();" class="button" value="Edit" <c:if test="${article.articleStatus}"> disabled </c:if>/>
        <input type="button" onClick="gotoLocation('leaveComments');" class="button" value="Leave Comments"/>
        </td>        
    </tr>     
</table>

