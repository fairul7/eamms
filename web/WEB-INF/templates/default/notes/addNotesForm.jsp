<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
	<tr>
  		<td>
    		<x:display name="${form.childMap.notesTextBox.absoluteName}"/>
	    </td>    
	</tr>

	<tr>
		<td>
			<x:display name="${form.childMap.saveButton.absoluteName}"/>
			<x:display name="${form.childMap.clearButton.absoluteName}"/>
		</td>
	</tr>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>