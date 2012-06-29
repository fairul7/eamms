<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="articles" value="${widget.articles}"/>

<c:forEach items="${articles}" var="article" varStatus="status">
<TABLE class="lightgrey" width="100%" cellspacing="1" >
    <tr>
        <td align="left" width="25%"><img src="images/ic_article.gif" border="0" alt="Article"><a href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" class="contentChildName"> <c:out value="${article.title}"/></a> </td>        
        <td width="25%">
        <td class="font_text">Category:<a class="font_category" href="categoryTagSearch.jsp?keyword=<c:out value='${article.categoryId}'/>&type=category"/><c:out value="${article.category}"/></a></td>
    </tr>    
          
    <tr>
		<td align="left" width="25%" ><span class="tsBody3">created on <fmt:formatDate pattern="dd MM yyyy" value="${article.createdOn}"/> </span></td>
		<td width="25%">
        <td class="font_text">Tags: <c:out value="${article.displayTags}" escapeXml="false"/></td>
    </tr>  
    
     <tr>
    	<td colspan="3" class="contentChildSummary"><p><c:out value="${article.storySummary}" escapeXml="false"/>...(more)</p></td>
    </tr>   

    <tr>
	    <td colspan="3" class="font_text"><hr> </td>
    </tr>

	</table>
</c:forEach>

