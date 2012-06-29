<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>
<table  width="100%">
    
    <tr>        
        <td align="right" width="40%" ><b><x:display name="${form.childMap.requestTitleLabel.absoluteName}"/></b></td>
        <td>&nbsp;</td>
        <td ><x:display name="${form.childMap.requestTitle.absoluteName}"/></td>
    </tr>
    <tr>        
        <td align="right"  ><b><x:display name="${form.childMap.requestorLabel.absoluteName}"/></b></td>
        <td>&nbsp;</td>
        <td ><x:display name="${form.childMap.requestor.absoluteName}"/></td>
    </tr>
    <tr>        
        <td align="right" ><b><x:display name="${form.childMap.remarksLabel.absoluteName}"/></b></td>
        <td>&nbsp;</td>
        <td ><x:display name="${form.childMap.remarks.absoluteName}"/></td>
    </tr>
    <tr>        
        <td colspan="3" ><b><x:display name="${form.childMap.itemNotCheckinLabel.absoluteName}"/></b></td>
        
    </tr>
    
</table>
<jsp:include page="../../form_footer.jsp" flush="true"/>