<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>

<%@include file="/common/header.jsp"%>

<html>
<head>
    <title><fmt:message key='general.label.popupFormWindow.pleaseSelect'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
       <body onload="focus()">

           <c:if test="${!empty param.popupSelectBoxCn}">
               <c:set var="popupSelectBoxCn" scope="session" value="${param.popupSelectBoxCn}"/>
               <c:set var="popupFormWindowCn" scope="session" value="${param.popupFormWindowCn}"/>
           </c:if>

       <script language="Javascript">
       <!--
           function popupFormWindow_select() {
               targetBox = window.opener.document.forms['<c:out value="${widgets[popupSelectBoxCn].rootForm.absoluteName}"/>'].elements['<c:out value="${popupSelectBoxCn}"/>'];
               sourceBox = document.forms['<c:out value="${popupFormWindowCn}"/>'].elements['<c:out value="${popupFormWindowCn}"/>.sbOptions'];

               // copy source to target
               var values='';
               var names='';
               for(c=0; c<sourceBox.length; c++) {
                   if (sourceBox.options[c].value == '')
                       break;
                   values += sourceBox.options[c].value + "|||";
                   names += sourceBox.options[c].text + "|||";
                   //targetBox.options[targetBox.length] = new Option(sourceBox.options[c].text, sourceBox.options[c].value, false, true);
               }
               window.opener.document.getElementById('<c:out value="${widgets[popupSelectBoxCn].absoluteNameForJavaScript}"/>_selectedValue').value=values;
               window.opener.document.getElementById('<c:out value="${widgets[popupSelectBoxCn].absoluteNameForJavaScript}"/>_selectedName').value=names;
               window.opener.<c:out value="${widgets[popupSelectBoxCn].absoluteNameForJavaScript}"/>_popupSelectBoxPopulate();
               window.opener.location.reload();
               window.close();
           }

           function popupFormWindow_remove() {
               sourceBox = document.forms['<c:out value="${popupFormWindowCn}"/>'].elements['<c:out value="${popupFormWindowCn}"/>.sbOptions'];

               // remove selected options
               for(c=sourceBox.length-1; c>=0; c--) {
                   if (sourceBox.options[c].selected)
                       sourceBox.options[c] = null;
               }

               // submit form
               <c:out value="${widgets[popupFormWindowCn].childMap.sbOptions.absoluteNameForJavaScript}"/>submit()
               document.forms['<c:out value="${popupFormWindowCn}"/>'].submit();
           }

       //-->
       </script>

       <%--
           <input type="button" class="button" value="<fmt:message key='general.label.okDone'/>" onclick="popupFormWindow_select()">
           <input type="button" class="button" value="<fmt:message key='general.label.removeSelected'/>" onclick="popupFormWindow_remove()">
           <input type="button" class="button" value="<fmt:message key='general.label.cancel'/>" onclick="window.close()">
       --%>

           <x:display name="${widgets[sessionScope.popupFormWindowCn].absoluteName}"/>

       <c:if test="${forward.name == 'select'}">
       <script language="Javascript">
       <!--
               popupFormWindow_select();
       //-->
       </script>
        </c:if>
       </body>
</html>
