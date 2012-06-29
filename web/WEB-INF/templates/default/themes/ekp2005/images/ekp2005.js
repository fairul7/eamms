//Open portlet edit window
function portletEdit(url)
{
	window.open(url, "entityPreferenceWindow", "height=400,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
}

//Invoked when a portlet is closed
function closeWindow(layer, url)
{
	if(navigator.appName == "Microsoft Internet Explorer")
	{
		portalserverhidden.document.location=url;
		document.getElementById(layer).style.display="none";
	}
	else
		document.location=url;
}

//Registering client items
function incrementList(list, item)
{
	if(list == "portlet")
		portletList[portletList.length] = item;
	else
		placeholderList[placeholderList.length] = item;
}

//Resetting layer widths after SET_DHTML() is called. Workaround for IE Behaviour
function initPortlets()
{
	for(i = 0; i < portletList.length; i++)
		document.getElementById(portletList[i]).style.width="100%";
	for(i = 0; i < placeholderList.length; i++)
		document.getElementById(placeholderList[i]).style.width="100%";
}

//Main menu
function toggleHeaderTopMenu()
{
	if(document.getElementById("ekmsTopMenu").style.visibility == "hidden" || document.getElementById("ekmsTopMenu").style.visibility == "")
	{
		document.getElementById("ekmsTopMenu").style.visibility="visible";
		if(isIE)
			document.getElementById("ekmsTopMenu").style.left=document.body.offsetWidth-(parseInt(document.getElementById("ekmsTopMenu").style.width)+parseInt(27));
		else
			document.getElementById("ekmsTopMenu").style.left=document.body.offsetWidth-(parseInt(document.getElementById("ekmsTopMenu").style.width)+parseInt(8));
	}
	else
		document.getElementById("ekmsTopMenu").style.visibility="hidden";
}

function initLeftMenu()
{
    treeLoad("ekmsLeftMenu");
    treeLoad("leftMenuShortcuts");
    treeLoad("leftMenuSearch");
    treeLoad("leftMenuHelp");
    if(document.getElementById("ekmsLeftMenu").style.display == "block")
    {
		document.getElementById("ekmsLeftColumn").style.display="block";
        document.getElementById("ekmsLeftColumn").style.width = "166px";
    }
    else
    {
		document.getElementById("ekmsLeftColumn").style.display="none";
        document.getElementById("ekmsLeftColumn").style.width = "0px";
    }
    //Initializing Tabs
    if((document.getElementById("leftMenuShortcuts").style.display == "none")&&(document.getElementById("leftMenuSearch").style.display == "none")&&(document.getElementById("leftMenuHelp").style.display == "none"))
        toggleLeftTabs("leftMenuHelp", "leftMenuSearch", "leftMenuShortcuts");
    //Toggling Images
    if(document.getElementById("leftMenuShortcuts").style.display == "block")
        document.getElementById("leftMenuShortcutsImage").src = "/ekms/images/ekp2005/leftMenuShortcuts_on.gif";
    else
        document.getElementById("leftMenuShortcutsImage").src = "/ekms/images/ekp2005/leftMenuShortcuts_off.gif";
    if(document.getElementById("leftMenuSearch").style.display == "block")
        document.getElementById("leftMenuSearchImage").src = "/ekms/images/ekp2005/leftMenuSearch_on.gif";
    else
        document.getElementById("leftMenuSearchImage").src = "/ekms/images/ekp2005/leftMenuSearch_off.gif";
    if(document.getElementById("leftMenuHelp").style.display == "block")
        document.getElementById("leftMenuHelpImage").src = "/ekms/images/ekp2005/leftMenuHelp_on.gif";
    else
        document.getElementById("leftMenuHelpImage").src = "/ekms/images/ekp2005/leftMenuHelp_off.gif";
    if(document.getElementById("ekmsLeftMenu").style.display == "block")
        document.getElementById("leftMenuArrow").src = "/ekms/images/ekp2005/but_arw_left.gif";
    else
        document.getElementById("leftMenuArrow").src = "/ekms/images/ekp2005/but_arw_right.gif";
}

//Left menu
function toggleLeftMenu()
{
	if(document.getElementById("ekmsLeftMenu").style.display == "none" || document.getElementById("ekmsLeftMenu").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null)
	{
		document.getElementById("ekmsLeftMenu").style.display="block";
		document.getElementById("ekmsLeftColumn").style.display="block";
		document.getElementById("ekmsLeftColumn").style.width="166px";
		document.getElementById("leftMenuArrow").src="/ekms/images/ekp2005/but_arw_left.gif";
	}
	else
	{
		document.getElementById("ekmsLeftMenu").style.display="none";
		document.getElementById("ekmsLeftColumn").style.display="none";
		document.getElementById("ekmsLeftColumn").style.width="0px";
		document.getElementById("leftMenuArrow").src="/ekms/images/ekp2005/but_arw_right.gif";
	}
	treeSave("ekmsLeftMenu");
	initPortlets();
}

//Left tabs
function toggleLeftTabs(show, hide1, hide2)
{
	document.getElementById(show).style.display="block";
	document.getElementById(show + "Image").src="/ekms/images/ekp2005/" + show + "_on.gif";
	document.getElementById(hide1).style.display="none";
	document.getElementById(hide1 + "Image").src="/ekms/images/ekp2005/" + hide1 + "_off.gif";
	document.getElementById(hide2).style.display="none";
	document.getElementById(hide2 + "Image").src="/ekms/images/ekp2005/" + hide2 + "_off.gif";
	treeSave(show);
	treeSave(hide1);
	treeSave(hide2);
	//Opening left menu
	document.getElementById("ekmsLeftMenu").style.display="block";
	document.getElementById("ekmsLeftColumn").style.display="block";
	document.getElementById("ekmsLeftColumn").style.width="166px";
	document.getElementById("leftMenuArrow").src = "/ekms/images/ekp2005/but_arw_left.gif";
	treeSave("ekmsLeftMenu");
	initPortlets();
	return false;
}

//Lock/Unlock Dashboard
function toggleDashboard()
{
	if(getCookie("ekpLockDashboard") == "false")
		setCookie("true");
	else
		setCookie("false");
	document.location="/ekms/home.jsp";
}

// Sets cookie values. Expiration date is optional
function setCookie(value)
{
	var exp = new Date();
	exp.setTime(exp.getTime() + (3650*24*60*60*1000));
	var cookieStr = "ekpLockDashboard=" + value + "; expires=" + exp.toGMTString();
	document.cookie = cookieStr;

    var sessionId = getCookie("JSESSIONID");
    var sessionCookie = "JSESSIONID=" + sessionId + "; path=/;";
    document.cookie = sessionCookie;
}

//The following function returns a cookie value, given the name of the cookie:
function getCookie(name)
{
	var search = name + "="
	if (document.cookie.length > 0)
	{
		// if there are any cookies
		offset = document.cookie.indexOf(search)
		if (offset != -1)
		{
			// if cookie exists
			offset += search.length
			// set index of beginning of value
			end = document.cookie.indexOf(";", offset)
			// set index of end of cookie value
			if (end == -1)
				end = document.cookie.length
			return unescape(document.cookie.substring(offset, end))
		}
	}
}