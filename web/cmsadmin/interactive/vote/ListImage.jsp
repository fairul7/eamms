<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%if(request.getParameter("id")!=null)session.setAttribute("id",request.getParameter("id")); %>
<x:config>
    <page name="ImageList">
     <com.tms.collab.vote.image.ImageList name="imageList" id="<%=session.getAttribute("id")%>" subFolder="vote"/>

    </page>
</x:config>

<html>
<head/>
<body>
   <x:display name="ImageList.imageList"/>

</body>
</html>
