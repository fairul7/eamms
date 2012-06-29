<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 24, 2004
  Time: 11:33:05 AM
  To change this template use File | Settings | File Templates.
--%>
       <%@ include file="/common/header.jsp" %>

       <x:template type="TemplateProcessLoginForm" properties="forward=index.jsp" />

       <jsp:include page="/cmsadmin/blank.jsp" flush="true" />

       <!-- Header -->
       <HTML>
       <HEAD>
       <TITLE>Enterprise Knowledge Portal</TITLE>
           <link rel="stylesheet" href="styles/style.css">
       </HEAD>
       <BODY leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" bgcolor="#E2E2E2">



       <TABLE width="100%" cellpadding=0 cellspacing=0 border=0 bgcolor="#333333" background="images/bannerbg.gif">
       <TR>
       	<TD width="407"><img src="images/banner.gif"></TD>
       	<TD valign="top" align="right">
       	</TD>
       	<TD bgcolor="#CCCCCC" width="1"><img src="images/clear.gif" width="1"></TD>
       	<TD align="right" valign="bottom">
       	</TD>
       </TR>
       </TABLE>
       <img src="images/clear.gif" height="1"><br>

       <!-- End Header -->




       <blockquote>
       <table width="300">
         <tr>
           <td>
               <hr size="1">
               <fmt:message key='general.label.welcome'/>.<br>
               <fmt:message key='security.message.username'/>
               <x:template type="TemplateDisplayLoginForm"/>
           </td>
         </tr>
       </table>
       </blockquote>

       <script>
       <!--
           function loginFocus() {
               try {
                   document.forms['loginForm'].elements['loginUsername'].focus();
               }
               catch(e) {
               }
           }
           window.onload=loginFocus;
       //-->
       </script>

       </BODY>
       </HTML>

