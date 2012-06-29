<%@ page import="com.tms.portlet.theme.themes.DefaultTheme"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%-- Initializing Variables --%>
<c:set var="entity" value="${requestScope.entity}"/>
                    </td>
                </tr>
            </table>
            </div>
            <script language="javascript">blockLoad('<c:out value="${entity.entityId}"/>');</script>
        </td>
    </tr>
</table>
