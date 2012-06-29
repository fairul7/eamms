<%@ page import="kacang.ui.Widget,
				 kacang.stdui.Form,
				 java.util.*,
				 java.awt.*,
                 kacang.stdui.Hidden"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%">
    <tr>
        <td>
            <x:display name="${form.userSelectBox.absoluteName}"/><br/>
            <x:display name="${form.submit.absoluteName}"/>
            
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>