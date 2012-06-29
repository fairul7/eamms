
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<frameset cols="300,0">

    <frame name="InsertImage" src="imageSelectorInsert.jsp?<c:out value='id=${param.id}&new=true'/>" frameborder="yes"/>
    <frame name="right" src="" />

</frameset>