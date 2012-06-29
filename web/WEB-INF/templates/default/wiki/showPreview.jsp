<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="article" value="${widget}"/>
<table width="100%" cellspacing="1" >
	      
    <tr>       
        <td align="left" class="contentBody"><c:out value="${article.previewStory}" escapeXml="false"/></td>
    </tr>       
    						
</table>