<%@ page import="kacang.Application,
                 com.tms.collab.formwizard.model.FormModule"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>




<c:set var="form" value="${widget}"/>





<jsp:include page="../form_header.jsp" flush="true"/>

<table>

<tr>
    <td valign="top" align="right" width = "20%"><B><fmt:message key='formWizard.label.templateName'/> * &nbsp;</B></td>
    <td>
    	<x:display name="${form.templateNameTextField.absoluteName}"/>
        <c:out value = "${form.templateNameTextField.message}" />
    </td>
</tr>

<tr>
    <td valign="top" align="right" width = "20%"><B><fmt:message key='formWizard.label.tableColumn'/> *</B></td>
    <td>
    	<x:display name="${form.tfTableColumn.absoluteName}"/>
        <c:out value = "${form.tfTableColumn.message}" />
    </td>
</tr>


<tr>
    <td valign="top"  align="center" colspan = "2">
        <x:display name="${form.addButton.absoluteName}"/>
    </td>
</tr>



</table>


<jsp:include page="../form_footer.jsp" flush="true"/>
