<%@include file="/common/header.jsp" %>

<table align="center" border=0 cellpadding=2 width="100%">
 <tr>
  <td align="center">
<form target="mw" method="post" action="http://www.m-w.com/cgi-bin/dictionary">
   <input type="hidden" name="book" value="Dictionary">
   <input class="textField" type="text" name="va" size="15">
   <input class=button type="submit" value="Dictionary">
  </form>
  <form target="mw" method="post" action="http://www.m-w.com/cgi-bin/thesaurus">
   <input type="hidden" name="book" value="Thesaurus">
   <input class="textField" type="text" name="va" size="15">
   <input class=button type="submit" value="Thesaurus">
   </form>
  </td>

 </tr>

  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>

</table>