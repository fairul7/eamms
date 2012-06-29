<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<x:config>
    <page name="formPage">
	        <com.tms.collab.formwizard.ui.FormMessagePanel name="msgPanel"/>
    </page>
</x:config>


<c:if test="${!empty param.formID}">
	<x:set name="formPage.msgPanel" property="formID" value="${param.formID}"/>
</c:if>

<x:display name="formPage.msgPanel" ></x:display>