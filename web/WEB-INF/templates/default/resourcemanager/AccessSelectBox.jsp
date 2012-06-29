<%@ include file="/common/header.jsp" %>


 <c:set var="accessSelectBox" value="${widget}"/>

<script language="JavaScript">

users=new Array(<c:out value="${accessSelectBox.usersCount}"/>);
groups = new Array(<c:out value="${accessSelectBox.groupsCount}"/>);
//selectedUsers = new Array(<c:out value="${accessSelectBox.selectedUsersCount}"/>);
//selectedGroups = new Array(<c:out value="${accessSelectBox.selectedGroupsCount}"/>);

<% int i=0;%><c:forEach items="${accessSelectBox.userOptions}" var="user"   >
users[<%=i%>]= new Option("<c:out value="${user.name}" escapeXml="false" />","<c:out value="${user.id}" escapeXml="false"/>");<%i++;%></c:forEach>

<% i=0;%><c:forEach items="${accessSelectBox.groupOptions}" var="groupname"   >
groups[<%=i%>]=new Option("<c:out value="${groupname.name}" escapeXml="false"/>","<c:out value="${groupname.id}" escapeXml="false"/>");<%i++;%></c:forEach>


function loadUsers(access)
{
var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
var pre = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].preaccess;
    if(pre.value!= access.value)
    {
         for(i=0;i<left.options.length;i++)
        {
            if(left.options[i].selected)
                left.options[i].selected = false;
        }
        left.options.length = users.length;
        for(var i = 0,o=0;i<users.length;i++) {
            if(!users[i].selected){
                left.options[o]= users[i];
                o++;
            }else
                left.options.length--;
        }
        pre.value = access.value;
       <%-- if(selectedUsers.length>0)
            right.options.length = selectedUsers.length;
        for(i=0;i<selectedUsers.length;i++){
            right.options[i] = new Option(selectedUsers[i],selectedUsers[i]);}--%>
    }
}


function loadGroups(access)
{
var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
var pre = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].preaccess;
    if(pre.value!= access.value)
    {
        for(i=0;i<left.options.length;i++)
        {
            if(left.options[i].selected)
                left.options[i].selected = false;
        }
        left.options.length = groups.length;
        for(var i = 0,o=0;i<groups.length;i++) {
            if(!groups[i].selected){
                left.options[o]= groups[i];
                o++
            }else
                left.options.length--;
        }
        pre.value = access.value;

    }
}


function assign()
{
    var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
    var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
    if(left.options.length <=0) return;
    for(i=0;i<left.options.length;i++)
    {
        if(left.options[i].selected)
        {
            right.options[right.options.length] = new Option(left.options[i].text,left.options[i].value,false,true);

        }
    }
    for(i=0;i<left.options.length;i++)
    if(left.options[i].selected)
    {
         left.options[i] =null;
         i--;
    }
    if(left.options.length==1&&left.options[0].selected){
         left.options[0] =null;
    }

}

function remove()
{
    var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
    var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
    if(right.options.length<=0) return;
    for(i=0;i<right.options.length;i++)
    {
        if(right.options[i].selected)
        {
                for(j = 0; j<users.length;j++)
                {
                    if(users[j].value ==right.options[i].value)
                    {
                        users[j].selected = false;
                        right.options[i] = null;
                        i=0;
                        break;
                    }
                }
                if(right.options.length>0){
                for(j = 0;j<groups.length;j++){
                    if(groups[j].value ==right.options[i].value){
                        groups[j].selected = false;
                        right.options[i] = null;
                        i=0;
                        break;
                    }
                }
                }
        }
    }
     if(right.options.length==1&&right.options[0].selected)
     {
            a = right.options[0].value;
            for(j = 0; j<users.length;j++)
            {
                if(users[j].value==a)
                {
                    users[j].selected = false;
                    right.options[0] = null;
                    break;
                }
            }
            for(j = 0;j<groups.length;j++){
                if(groups[j].value==a){
                    groups[j].selected = false;
                    right.options[0] = null;
                    break;}
            }
     }

            radios =  document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype;
                    for(k=0;k<radios.length;k++){
                                        if(radios[k].checked && radios[k].value == 1)
                            loadUsers(document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype);

                    else if(radios[k].checked && radios[k].value == 2)
                            loadGroups(document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype);
                       }
}


    function selectAccessMembers()
    {
        var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
        for(c=0; c < right.length; c++) right.options[c].selected = true;
        return true;
    }

</script>

<input type="hidden" name="preaccess" value="-1"/>

<table width="100%" CELLPADDING=0 CELLSPACING=0>
    <tr>
         <td valign = "top">
            <INPUT type="radio" name="accesstype" value="2" onClick="javascript:loadGroups(this)"/><fmt:message key='resourcemanager.label.Group'/><INPUT type="radio" name="accesstype" value="1" onClick="javascript:loadUsers(this)"/><fmt:message key='resourcemanager.label.User'/></td>
    </tr>
    <tr>
        <Td>
            <table>
                <tr>
                    <td><b><fmt:message key='resourcemanager.label.Available'/></b><br>
                        <x:display name="${accessSelectBox.left.absoluteName}" ></x:display>
                    </td>
                    <td valign="CENTER">
                        <INPUT TYPE="button" value=">>" class="button" onClick="assign();return false;"/>     <br>
                        <INPUT TYPE="button" value="<<"  class="button" onClick="remove();return false;"/>
                    </td>
                    <td>      <b><fmt:message key='resourcemanager.label.Selected'/></b><br>
                      <x:display name="${accessSelectBox.right.absoluteName}" ></x:display>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>

<script language="javascript">

var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];

if(<c:out value="${accessSelectBox.selectedIdsCount}"/>>0)
{
    right.options.length = <c:out value="${accessSelectBox.selectedIdsCount}"/>;
    <% i=0;%><c:forEach items="${accessSelectBox.selectedIds}" var="id"   >
    for(i=0;i<users.length;i++){
        if(users[i].value == "<c:out value="${id}" escapeXml="false"/>" ){
            users[i].selected = true;
            right.options[<%=i%>] =new Option(users[i].text,users[i].value);
            right.options[<%=i%>].selected = true;
            break;
            }
    }
    for(i=0;i<groups.length;i++){
        if(groups[i].value == "<c:out value="${id}" escapeXml="false"/>" ){
            groups[i].selected = true;
            right.options[<%=i%>] =new Option(groups[i].text,groups[i].value);
            right.options[<%=i%>].selected = true;
            break;
            }
    }
    <%i++;%>
    </c:forEach>
//document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype[1].checked=true;
//loadUsers(document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype);
}
<%--

if(selectedGroups.length>0)
{
 <% i=0;%><c:forEach items="${accessSelectBox.selectedGroupsName}" var="groupname"   >
selectedGroups[<%=i%>]="<c:out value="${groupname}" escapeXml="false"/>";<%i++;%></c:forEach>
   document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype[0].checked=true;
    loadGroups(document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype);
}
--%>

</script>
