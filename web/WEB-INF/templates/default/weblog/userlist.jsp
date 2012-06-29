<%@include file="/common/header.jsp"%>


<table width="98%">
    <tr>
    <td>
        [<c:choose><c:when test="${empty widget.alphabet}" ><strong><fmt:message key='weblog.label.all'/></strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al="  >All</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='A'||widget.alphabet=='a'}" ><strong>A</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=a"  >A</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='B'||widget.alphabet=='b'}" ><strong>B</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=b"  >B</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='C'||widget.alphabet=='c'}" ><strong>C</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=c"  >C</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='D'||widget.alphabet=='d'}" ><strong>D</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=d"  >D</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='E'||widget.alphabet=='e'}" ><strong>E</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=e"  >E</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='F'||widget.alphabet=='f'}" ><strong>F</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=f"  >F</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='G'||widget.alphabet=='g'}" ><strong>G</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=g"  >G</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='H'||widget.alphabet=='h'}" ><strong>H</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=h"  >H</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='I'||widget.alphabet=='i'}" ><strong>I</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=i"  >I</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='J'||widget.alphabet=='j'}" ><strong>J</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=j"  >J</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='K'||widget.alphabet=='k'}" ><strong>K</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=k"  >K</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='L'||widget.alphabet=='l'}" ><strong>L</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=l"  >L</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='M'||widget.alphabet=='m'}" ><strong>M</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=m"  >M</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='N'||widget.alphabet=='n'}" ><strong>N</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=n"  >N</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='O'||widget.alphabet=='o'}" ><strong>O</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=o"  >O</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='P'||widget.alphabet=='p'}" ><strong>P</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=p"  >P</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='Q'||widget.alphabet=='q'}" ><strong>Q</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=q"  >Q</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='R'||widget.alphabet=='r'}" ><strong>R</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=r"  >R</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='S'||widget.alphabet=='s'}" ><strong>S</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=s"  >S</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='T'||widget.alphabet=='t'}" ><strong>T</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=t"  >T</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='U'||widget.alphabet=='u'}" ><strong>U</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=u"  >U</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='V'||widget.alphabet=='v'}" ><strong>V</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=v"  >V</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='W'||widget.alphabet=='w'}" ><strong>W</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=w"  >W</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='X'||widget.alphabet=='x'}" ><strong>X</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=x"  >X</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='Y'||widget.alphabet=='y'}" ><strong>Y</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=y"  >Y</x:event></c:otherwise></c:choose> |
        <c:choose><c:when test="${widget.alphabet=='Z'||widget.alphabet=='z'}" ><strong>Z</strong></c:when><c:otherwise><x:event name="${widget.absoluteName}" type="select" param="al=z"  >Z</x:event></c:otherwise></c:choose> |
        ]
    </td>
    </tr>

    <tr>
    <td>
        <x:display name="${widget.userTable.absoluteName}" />
    </td>
    </tr>
</table>


<%--<c:set var="users" value="${widget.users}" />--%>
<%--
<c:set var="start" value="1"/>
<%
    int index = 0;
    String [] title = new String[]{
        "ABC","DEF","GHI","JKL","MNO","PQR","STU","VWX","YZ"
    };
%>


<c:forEach var="user" items="${users}" >
    <c:set var="name" value="${user.userName}"/>
    <%
        String name = (String)pageContext.getAttribute("name");
        String alps = title[index];
        for(int i = 0;i<name.length();i++){
            if(name.startsWith(alps,i)){

            }
        }

    %>




    <c:if test="${start == 1}" >
        <table>
        <tr>
            <td><b>
                <%=title[index]%></b>
            </td>
        </tr>
    </c:if>
        <Tr>
            <Td>
            </td>
        </tr>
    </table>
</c:forEach>--%>

<%--<c:forEach var="user" items="${users}">

   <c:forEach var="blogId" items="${user.blogIds}">
      <a href="blogview.jsp?blogId=<c:out value="${blogId}" />">
               <c:out value="${user.userName}" />

   </a>&nbsp; <a href="addPostForm.jsp?blogId=<c:out value="${blogId}" />"><fmt:message key='weblog.label.addPost'/></a>

 </c:forEach>

</c:forEach>--%>
