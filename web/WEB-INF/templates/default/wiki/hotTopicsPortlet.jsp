<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="articles" value="${widget.articles}"/>

<c:forEach items="${articles}" var="article" varStatus="status">
<TABLE bgColor=#f4cc98 width="100%" cellspacing="1">
    <tr>
    <td align="left" width="25%" ><li><a class="tablefontLink" href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" 
        class="article_title"> <c:out value="${article.title}"/></a> </td>        
        <td width="25%">        
    </tr>             
    
    <tr>
	    <td align="left" width="50%" class="tsTextSmall">created on:<fmt:formatDate pattern="dd MM yyyy" value="${article.createdOn}"/> </i></td>
    </tr>
          
    </table>
</c:forEach>

