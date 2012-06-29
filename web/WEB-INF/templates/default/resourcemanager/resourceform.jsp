 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>




<script>
    function checkApproval(){
        var radio = document.forms['<c:out value="${widget.absoluteName}" />'].elements['<c:out value="${form.approveBG.absoluteName}"/>.approveBG'];
        for(i=0;i<radio.length;i++)
        {
            if(radio[i].checked == true){
                    var row = document.getElementById('approvalRow');
                if(radio[i].value=='<c:out value="${form.radioAprrovalYes.absoluteName}" />'){
                    row.style.display = 'block';

                }else{
                    row.style.display = 'none';
                }
            }
        }
    }

    function checkClassification(){
        var radio = document.forms['<c:out value="${widget.absoluteName}" />'].elements['<c:out value="${form.classBG.absoluteName}"/>'];
        for(i=0;i<radio.length;i++)
        {
            var row = document.getElementById('classRow');
            if(radio[i].checked == true){
                if(radio[i].value == '<c:out value="${form.radioPrivate.absoluteName}" />'){
                    row.style.display = 'block';
                }else{
                    row.style.display = 'none';
                }
            }
        }

    }
</script>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >

    <tr>
        <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.Name'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.nameTextField.absoluteName}" ></x:display>
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.Description'/>&nbsp; <FONT class="classRowLabel">*</FONT></td>
        <td class="classRow">
            <x:display name="${form.descriptionTextBox.absoluteName}" ></x:display>
        </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.Category'/></td>
         <td class="classRow">
            <x:display name="${form.categoryList.absoluteName}" />
           <%-- <br><x:display name="${form.newCategory.absoluteName}" />
--%>
         </td>
    </tr>
   <tr>
        <td class="classRow"></td>
        <td class="classRow"><fmt:message key='resourcemanager.label.NewCategory'/><x:display name="${form.newCategory.absoluteName}" />
        </td>
    </tr>

    <tr>
        <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.Image'/></td>
        <td class="classRow">
            <c:if test="${form.resource== null || form.editImage}" >
            <x:display name="${form.radioImageUpload.absoluteName}" /><br>
            <x:display name="${form.imageFileUpload.absoluteName}" /><br>
            <c:if test="${forward.name =='invalid file type'}" >
             <FONT COLOR="#FF0000"><fmt:message key='resourcemanager.label.Unsupportedfiletype'/></FONT>
            </c:if>

            <br>
            <x:display name="${form.radioImageUrl.absoluteName}" />
            <br>
            <x:display name="${form.imageUrlTextField.absoluteName}" ></x:display><br>
            </c:if>
            <c:if test="${form.resource!=null&&form.resource.imageType == 2&&!form.editImage}">
                <IMG src="<c:out value="${form.resource.image}"/>"/> <br><fmt:message key='resourcemanager.label.ImageUrl'/><br><x:display name="${form.imageUrlTextField.absoluteName}" ></x:display><br>
                <x:display name="${form.changeButton.absoluteName}" ></x:display>
            </c:if>
            <c:if test="${form.resource!=null&&form.resource.imageType == 1&&!form.editImage}">
            <% if (request.getServerPort() == 80){%>
                    <IMG src="http://<%=request.getServerName()%><%=request.getContextPath()%>/storage<c:out value="${form.resource.image}"/>"/>
                    <%}else{ %>
                    <IMG src="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/storage<c:out value="${form.resource.image}"/>"/>
                    <%}%><fmt:message key='resourcemanager.label.ImageFile'/><c:set value="${form.resource.image}" var="image"/>

                <br>
                <x:display name="${form.changeButton.absoluteName}" ></x:display>
            </c:if>
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.RequireApproval'/></td>
        <td class="classRow">
            <x:display name="${form.radioAprrovalYes.absoluteName}" />
            <x:display name="${form.radioApprovalNo.absoluteName}" />
            <br>
            <div id="approvalRow">
              <br>
              <x:display name="${form.authorizedUsers.absoluteName}" />
            </div>
        </td>
    </tr>

    <tr>
        <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.Classification'/></td>
        <td class="classRow">
            <x:display name="${form.radioPublic.absoluteName}" />
            <x:display name="${form.radioPrivate.absoluteName}" />
            <br>
            <div id="classRow">
              <br>
              &nbsp;<fmt:message key='resourcemanager.label.Group'/>
              <br>
              <x:display name="${form.groupSelectBox.absoluteName}" />
              <br>
              &nbsp;<fmt:message key='resourcemanager.label.User'/>
              <br>
              <x:display name="${form.usersSelectBox.absoluteName}" />
        </td>
    </tr>

    <tr>
        <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">&nbsp;</td>
        <td class="classRow">
            <x:display name="${form.active.absoluteName}" />
        </td>
    </tr>

    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>


<script>
    checkApproval();
    checkClassification();            
</script>