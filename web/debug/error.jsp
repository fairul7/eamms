<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<html>
<head><title>Sorry, an exception has occured</title></head>
<script>
<!--
    function toggle() {
        var node = document.getElementById("stacktrace");
        var link = document.getElementById("link");
        if (node.style.display != "block") {
            node.style.display = "block";
            link.innerHTML = "hide details";
        }
        else {
            node.style.display = "none";
            link.innerHTML = "show details";
        }
    }
//-->
</script>
<body>

<table style="width:100%; border: double 1px gray">
<tr>
<td>
    <b>Sorry, an exception has occured.</b>
    <br>
    <pre><c:out value="${exception.message}"/></pre>
    [<a id="link" href="javascript:toggle()">show details</a>]

    <div id="stacktrace" style="display:none; font-family:arial; font-size:10pt">
    <pre><c:out value="${stacktrace}"/></pre>
    </div>
</td>
</tr>
</table>

</body>
</html>
