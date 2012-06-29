<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
    <jsp:include page="../form_header.jsp" flush="true"/>
    <table width="100%" cellpadding="2" cellspacing="1">
        
    <table border=0 cellspacing=0 cellpadding=4>
		<tr>
			<td>
				<IMG height=73 src="./latestArticles_files/logo.gif" width=307>
			</td>		
		</tr>
	</table>
        
    <table width="100%" cellpadding=0 cellspacing=0 align="center">
		<tr valign=top align="center">		
			<td align=center nowrap><input name=hl type=hidden value=en>
			<input maxlength=2048 name=q size=55 title="Go" value=""><br>
			<input class="box2" type="button" value="Go">&nbsp;
			<input class="box2" type="button" value="Search">&nbsp;
			<input class="box2" type="button" value="Advanced Search" onClick="document.location='search.htm';">
			</td>
		</tr>
	</table>
	</center>

  <jsp:include page="../form_footer.jsp" flush="true"/>