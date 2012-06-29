<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

        <table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
            <tr>
                <td class="classRowLabel" valign="top" align="right" width="15%">
                    <fmt:message key="fms.administration.msg.email1"/>
                </td>
	            <td class="classRow" valign="top" width="40%">
	                <x:display name="${form.absoluteName}.email0"/>
	            </td>
	        </tr>
	        <tr>
                <td class="classRowLabel" valign="top" align="right" width="15%">
                    <fmt:message key="fms.administration.msg.email2"/>
                </td>
                <td class="classRow" valign="top" width="40%">
                    <x:display name="${form.absoluteName}.email1"/>
                </td>
            </tr>
            <tr>
                <td class="classRowLabel" valign="top" align="right" width="15%">
                    <fmt:message key="fms.administration.msg.email3"/>
                </td>
                <td class="classRow" valign="top" width="40%">
                    <x:display name="${form.absoluteName}.email2"/>
                </td>
            </tr>
            <tr>
                <td class="classRowLabel" valign="top" align="right" width="15%">
                    <fmt:message key="fms.administration.msg.email4"/>
                </td>
                <td class="classRow" valign="top" width="40%">
                    <x:display name="${form.absoluteName}.email3"/>
                </td>
            </tr>
            <tr>
                <td class="classRowLabel" valign="top" align="right" width="15%">
                    <fmt:message key="fms.administration.msg.email5"/>
                </td>
                <td class="classRow" valign="top" width="40%">
                    <x:display name="${form.absoluteName}.email4"/>
                </td>
            </tr>
            <tr><td class="classRowLabel" valign="top" align="right" width="15%">
                &nbsp;</td>
            <td class="classRow" valign="top" width="40%">
                &nbsp;</td>
            </tr>
            <tr><td class="classRowLabel" valign="top" align="right" width="15%">
                &nbsp;</td>
            <td class="classRow" valign="top" width="40%">
                <x:display name="${form.absoluteName}.submit"/>
                <x:display name="${form.absoluteName}.cancel_form_action"/></td>
            </tr>
        </table>

<jsp:include page="../../form_footer.jsp" flush="true"/>