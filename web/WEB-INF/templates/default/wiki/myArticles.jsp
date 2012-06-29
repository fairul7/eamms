<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:forEach items="${widget.myArticles}" var="article" varStatus="status">
<TABLE class="lightgrey" width="100%" cellspacing="1" >
    <tr>
        <td align="left" width="25%"><a href="viewArticle.jsp?articleId=<c:out value='${article.articleId}'/>" class="contentChildName"> <c:out value="${article.title}"/></a> </td>        
        <td width="25%">
        <td class="font_text">Category:<a class="font_category" href="categoryTagSearch.jsp?keyword=<c:out value='${article.categoryId}'/>&type=category"/><c:out value="${article.category}"/></a></td>
    </tr>    
          
    <tr>
		<td align="left" width="25%" class="tsBody3"> created on <fmt:formatDate pattern="dd MM yyyy" value="${article.createdOn}"/> </td>
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

<TABLE class="lightgrey" width="100%" cellspacing="1" >
	<tfooter>
		  <tr>
		    <td colspan="<c:out value="${colspan}"/>" align="right">
		        <c:set var="pageCount" value="${widget.pageCount}"/>
		
		        <c:if test="${widget.currentPage > 1}">
		        [<x:event name="articlesPaging" type="page" param="page=${widget.currentPage - 1}">&lt;</x:event>]
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
		                location.href='?page=' + page;
		            }
		        //-->
		        </script>
		
		        <c:if test="${widget.currentPage < pageCount}">
		        [<x:event name="articlesPaging" type="page" param="page=${widget.currentPage + 1}">&gt;</x:event>]
		        </c:if>
		    </td>
		  </tr>
		</tfooter>
</table>


