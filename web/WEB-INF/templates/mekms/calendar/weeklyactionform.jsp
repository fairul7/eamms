<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}" />
<c:if test="${form == form.rootForm}">
<table cellpadding="0" cellspacing="0" width="100%" border="0">
    <form name="<c:out value="${form.absoluteName}"/>"
          action="?"
          method="<c:out value="${form.method}"/>"
          target="<c:out value="${form.target}"/>"
          <c:if test="${!empty form.enctype}">
              enctype="<c:out value="${form.enctype}"/>"
          </c:if>
          onSubmit="<c:out value="${form.attributeMap['onSubmit']}"/>"
          onReset="<c:out value="${form.attributeMap['onReset']}"/>"
    >
        <tr>
            </td>
            <input type="hidden" name="cn" value="<c:out value="${form.absoluteName}"/>">
            </c:if>
                <c:forEach var="child" items="${form.children}">
                     <x:display name="${child.absoluteName}"/>
                    </c:forEach>

            <c:if test="${form == form.rootForm}">
            </td>
        </tr>
    </form>
</table>
</c:if>


