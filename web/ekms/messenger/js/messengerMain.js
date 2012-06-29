	var cle;
	var cui;
	var path;
	var pathProperty;
	var storeChatIdArray = new Array();
	var storeChatIdArray2 = new Array();
	var a = 0;
	var chatHeader;
	var chatFooter;
	var trying = 1;
	var counter
	
	
	
	function init()
	{
		YAHOO.namespace("example.container");
		YAHOO.example.container.messengerPanel = new YAHOO.widget.Panel("messengerPanel", 
		{ 
		xy:[765,0],
		width:"250px", 
		fixedcenter:false, 
		visible:false , 
		constraintoviewport:true
		} );
		
		var escKey = new YAHOO.util.KeyListener(document, { keys:27 },
            { fn:YAHOO.example.container.messengerPanel.hide,
            scope:YAHOO.example.container.messengerPanel,
            correctScope:true } );
        YAHOO.example.container.messengerPanel.cfg.queueProperty("keylisteners", escKey);

        YAHOO.example.container.messengerPanel.render();
		
	}
	YAHOO.util.Event.addListener(window, "load", startAll);
	
	function startAll()
	{
	DWREngine.setVerb("GET");
//	DWREngine.setMethod(DWREngine.IFrame);
	
//		alert(window.location.href);
//		var first = window.location.href;
//		window.location.href = first+"?cmd=somecmd&rand=helllooooooo";
		init();
		var x = readCookie('creatingOpenPanel');
		if(x == "true")
		{
			initCheckMessages();
		}
		else
		{
			checkMessages();
		}
		startingHeaderFooter();
		
		setTimeout("submitSearch()",200000);
	}
	
	function checkingMessages() 
	{
			checkMessages();
	}
	
/*	function randomString(len) {
		var chars ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
		var string_length = len;
		var randomstring = "";
		for (var i=0; i
		var rnum = Math.floor(Math.random()*chars.length);
		randomstring += chars.substring(rnum,rnum+1);
		}
		return randomstring;
	}*/
	
	function startingHeaderFooter()
	{
		var newitem0 =document.createElement("span");
		var newitem5 =document.createElement("span");
		if(chatHeader == undefined || chatHeader=="")
		{
			myHeader = "HEADER";
		}
		else
		{
			myHeader = chatHeader;
		}
		if(chatFooter == undefined || chatFooter=="")
		{
			myFooter = "FOOTER";
		}
		else
		{
			myFooter = chatFooter;
		}
		newitem0.innerHTML=myHeader;
		newitem5.innerHTML=myFooter;
		(document.getElementById("chatSession")).appendChild(newitem0);
		(document.getElementById("conver")).appendChild(newitem5);
	}
	
	function detect(Event) 
	{
	  // Event appears to be passed by Mozilla
	  // IE does not appear to pass it, so lets use global var
		if(Event==null) {
	 		alert('null');
	  		Event=event;
		}
	cle = Event.keyCode;
	}
		 
	function chang(aid,userName,Event,quoi) 
	{
		detect(Event);
		if(cle=='13')
		{
		 	inputs(aid,userName);
		}
	}

	function openTabDone(id)
	{
		var x = readCookie('creatingTabPanelOpen');
		//close all blocks first
		for(var i = 0; i < storeChatIdArray.length; i++)
		{
			if(document.getElementById(storeChatIdArray[i]) != null)
			{
				document.getElementById(storeChatIdArray[i]).style.display = "none";
			}
		}
		//open target block
		if(document.getElementById(id).style.display == "none" || document.getElementById(id).style.display == "")
		{
			document.getElementById(id).style.display = "block";
			document.getElementById("workingTab_"+id).style.backgroundColor ="#DEF2FF";
			document.getElementById("workingTab_"+id).style.color ="#1950C0";
			document.getElementById("workingTab_"+id).style.border ="1px solid #00ADE6";
		}
		else
		{
			document.getElementById(id).style.display = "none";
		}
		if (x == id)
		{
			createCookie('openingTabPanel',true,1)
		}
	}
	
	function openTab(id)
	{
		createCookie('creatingTabPanelOpen',id,1);
		openTabDone(id);
		
	}
	
	function openingTab(id)
	{
		var x = readCookie('openingTabPanel');
		var y = readCookie('creatingTabPanelOpen');
		if ( x == "true" && document.getElementById(y) != null)
		{
			for(var i = 0; i < storeChatIdArray.length; i++)
			{
				if(document.getElementById(storeChatIdArray[i]) != null)
				{
					document.getElementById(storeChatIdArray[i]).style.display = "none";
				}
			}
//			alert(y);
			if(y == "")
			{
				document.getElementById(storeChatIdArray[0]).style.display = "block";
			}
			else
			{
				document.getElementById(y).style.display = "block";
				document.getElementById("workingTab_"+y).style.backgroundColor ="#DEF2FF";
				document.getElementById("workingTab_"+y).style.color ="#1950C0";
				document.getElementById("workingTab_"+y).style.border ="1px solid #00ADE6";
				document.getElementById("cv_" + y).focus();
			}
		}
	}

	function createAccor(aid,cui,msg,chatId,userName,userId,saved,create)
	{	
//		alert(create);
		if (chatId.substring(0,6) == "group_" && document.getElementById("hci_"+aid)== null)
		{
//			submitSearch();
			trying++;
		}	
		var newAid;
		if (aid.substring(0,7) == "chtwnd_")
		{
			newAid = aid.substring(aid.indexOf("_")+1);
		}	
		else
		{
			newAid = aid;
		}	
		if (create == "true")
		{
			createCookie('creatingTabPanelOpen',aid,1);
			var x = readCookie('creatingTabPanelOpen');
//			alert(x);
		}
//		createCookie('creatingOpenPanel',true,1);
		if (document.getElementById(aid)== null)
		{
			createCookie('openingTabPanel',true,1);
			var f = document.createDocumentFragment();			
			var newitem2 = document.createElement("div");
			newitem2.id = "closingTab_" + aid;
//			newitem2.setAttribute("style","display : block");
			newitem2.innerHTML = "<div id=\"workingTab_" + aid + "\" class=\"messengerPopOutBar\" style=\"cursor:pointer;\" width = \"100px\" height =\"10px\" href = '#' onclick = \"openTab('"+ aid +"'); return false;\")>" + newAid + "</div>";	
			var newitem=document.createElement("div");
			newitem.id = aid;
//			newitem.setAttribute("style","display : block");			
			var browserName=navigator.userAgent; 
			if (browserName.toLowerCase().match("firefox"))
			{ 
				newitem.innerHTML ="<div class=\"msg\" id=\"msg_" + aid + "\" style =\"width:230px;padding:5px; top:0px; height:110px;float:bottom; overflow:auto; padding-bottom:0px;\">" + msg + "</div>"+
						   	       "<div align=\"center\" style=\"padding:5px;\">"+
				                   "<textarea onkeyup=\"chang('" + aid + "','" + userName + "',event,this);\" WRAP=\"on\" id=\"cv_" + aid + "\" name=\"conversation\" rows=\"2\" cols=\"35\" value=\"\"></textarea>"+
								   "</div>"+
								   "<input type=\"checkbox\" id=\"save_" + aid + "\" onclick = \"savingMessage('" + aid + "')\"> Save"+
								   "<div id = \"hci_" + aid + "\" style = \"display : none\">" + chatId + "</div>"+
								   "<br>";
			}
			else 
			{ 
				newitem.innerHTML ="<div class=\"msg\" id=\"msg_" + aid + "\" style =\"width:230px; height:100px; padding:5px; top:0px; overflow:auto; padding-bottom:0px;\">" + msg + "</div>"+
						           "<form id=\"msgf_" + aid + "\" name=\"message\" method=\"post\" action=\"\">"+
						           "<div align=\"center\" style=\"padding:5px;\">"+
								   "<textarea onkeyup=\"chang('" + aid + "','" + userName + "',event,this);\" WRAP=\"virtual\" id=\"cv_" + aid + "\" name=\"conversation\" rows=\"4\" cols=\"40\" value=\"\"></textarea>"+
 								   "</div>"+
 								   "<input type=\"checkbox\" id=\"save_" + aid + "\" onclick = \"savingMessage('" + aid + "')\"> Save"+
								   "</form>"+
								   "<div id = \"hci_" + aid + "\" style = \"display : none\">" + chatId + "</div>";
			}
			f.appendChild(newitem2);
			f.appendChild(newitem);
			document.getElementById("messageCollapsar").appendChild(f);
			YAHOO.example.container.messengerPanel.show();
			if (document.getElementById("hci_" + aid ).innerHTML== "")
			{
				initChat(userId,cui,aid);
			}
			storeChatIdArray[a] = aid;
			a++;
			if (chatId.substring(0,6) == "group_")
			{
				if (document.getElementById("hci_"+aid)== null)
				{
					if (document.getElementById("hci_"+aid).innerHTML == chatId)
					{

						if (counter > trying)
						{
							trying= counter;
						}
					}
				}
			}	
		}
		else
		{
			messages = document.getElementById("msg_" + aid);
			if (msg != "")
			{
				messages = document.getElementById("msg_" + aid);
				messages.innerHTML = msg;
				YAHOO.example.container.messengerPanel.show();
			}
			else
			{
				YAHOO.example.container.messengerPanel.show();
			}
		}
		if (saved == false)
		{
			document.getElementById("save_" + aid).checked = false;
		}
		else
		{
			document.getElementById("save_" + aid).checked = true;
		}
/*		if (chatId.substring(0,6) == "group_")
		{
			if (document.getElementById(chatId) == null)
			{
				document.getElementById("groupChatConversation").innerHTML = "<a href =\"#\" class = \"groupClass\" id=\""+chatId+"\" onclick=\"createAccor('"+aid+"','"+cui+"','','"+chatId+"','"+userName+"','','false','true')\">"+aid+"</a><br/>";
			}
		}*/
		openingTab(aid);
	}
	
	function savingMessage() 
	{
//		DWREngine.setMethod(DWREngine.IFrame);
		MessageDWRModule.savingMessage(doNothing,document.getElementById("save_" + aid).checked, cui, document.getElementById("hci_" + aid).innerHTML);
	}
	
	function retrieveMessage(message) 
	{
//	alert("retrieveaaaaaaaaaaaaaaaaaaaaaaaa");
		for (var data in message)
	    {
//	    alert(message[data]+"aaaaaaaaaaaaaaaa");
		if (message[data].chatId != null)
		{
//		alert("in");
	 	    msg = message[data].msg;
	        chatId = message[data].chatId;
			msgInfo = message[data].info;
			for (var data1 in msgInfo)
			{
				uid = msgInfo[data1].userId;
//				alert("here");
				userData = msgInfo[data1].user;
//				if(userData != null)
//				{
					if (cui == uid)
					{
		//				alert(userData);
					}
					else
					{
	//				alert(userData.name);
						if(userData != null)
						{
						if(chatId.substring(0,6) == "group_")
						{
							if (document.getElementById(chatId) != null)
							{
								aid = document.getElementById(chatId).innerHTML;
							}
							else
							{
								aid = "Group No." + (trying);
							}
						}
						else
						{
							userName = userData.name;
							aid = "chtwnd_" + userName;
						}	
					}}
//				}
			}
//			truncation(aid, msg);
			
		}
		}	
		messages = document.getElementById("msg_" + aid);
			messages.innerHTML = msg;
			YAHOO.example.container.messengerPanel.show();
		checkMessages();
	}
		
	var i = 0;
	function submitSearch()
    {

   		//alert("ok");
    	ajax_loadContent("contactsSummary",'/ekms/messenger/MessengerIFrame.jsp?test=' + i);
    	setTimeout("submitSearch()",500000);
    }
    
/*    function truncation(aid, msg)
    {
    	var len = 500;
			messages = document.getElementById("msg_" + aid);
			if (msg.length > len)
			{
				msg = msg.substring(0, len);
   				msg = msg.replace(/\w+$/, 'truncated');
   				messages.innerHTML = msg;
			}
			else
			{
				messages.innerHTML = msg;
			}
    }
			
	function loadContact(url) 
	{
    	var conFrame = document.getElementById("contactsSummaryFrame");
    	var contactFrame = window.frames["contactsSummaryFrame"];
    	contactFrame.document.location.href=url;
    	observeEvent(conFrame, "load", loadContactContent);
    	return false;
    }
                            
    function loadContactContent() 
    {   	
    	var contactFrame = window.frames["contactsSummaryFrame"];
        var contactDiv = document.getElementById("contactsSummary");        
        contactDiv.innerHTML = contactFrame.document.body.innerHTML;
    }
                            
    function submitSearch()
    {
        loadContact(path);
       	setTimeout("submitSearch()",2000000);
        return false;
    }
	
	function observeEvent(element, name, observer, useCapture)
    {
        if (element.addEventListener)
        {
            element.addEventListener(name, observer, useCapture);
        }
        else if (element.attachEvent)
        {
            element.attachEvent('on' + name, observer);

        }
    }*/
			
	function inputs(id, userName)
	{	
		document.getElementById("workingTab_"+id).style.backgroundColor ="#DEF2FF";
		document.getElementById("workingTab_"+id).style.color ="#1950C0";
		document.getElementById("workingTab_"+id).style.border ="1px solid #00ADE6";
//		createCookie('creatingOpenPanel',true,1);
		if (document.getElementById("save_" + id).checked == true)
		{
			saved = true;
		}
		else
		{
			saved = false;
		}
		field = document.getElementById("cv_" + id);	
		hci = document.getElementById("hci_" + id).innerHTML;

		if (document.getElementById("cv_"+ id).value != "" && document.getElementById("cv_"+ id).value != "\n" && document.getElementById("cv_"+ id).value != "\r\n")
		{	
//				DWREngine.setMethod(DWREngine.IFrame);
				var description = field.value;
			    description.replace(/[\"\'][\s]*javascript:(.*)[\"\']/g, "\"\"");
			    description = description .replace(/</g, "&lt;").replace(/>/g, "&gt;");
			    description = description.replace(/script(.*)/g, "");    
			    description = description.replace(/eval\((.*)\)/g, "");
				firstChatMessage ="<b>" + userName + "</b> :"+description+ "<br>" ;
				MessageDWRModule.initChatting(doSomething,firstChatMessage,hci,cui,saved,id);
				
		}
		field.value="";
		createCookie('creatingTabPanelOpen',id,1);
	}
	
	function doSomething(hci)
	{
		var extra;
		if (hci.substring(0,2)=="ok")
		{
			extra = hci.substring(2,hci.indexOf("("));
			MessageDWRModule.getExistMessage(retrieveMessage, extra);
		}
		else
		{
			alert("Current chat has expired");
			extra = hci.substring(2,hci.indexOf("("));
			extra2 = hci.substring(hci.indexOf(")")+1);
//			var closeHci = document.getElementById("closingTab_"+ extra2);
//			closeHci.innerHTML = "";
			var closeAid = document.getElementById("messageCollapsar");
			var closeIn = document.getElementById("closingTab_"+extra2);
			var closeIn2 = document.getElementById(extra2);
			
			
//			var j = 0;
			for(var i = 0; i < storeChatIdArray.length; i++)
			{
				if (storeChatIdArray[i] != extra2)
				{
					storeChatIdArray[i]= storeChatIdArray[i];
				}
				else
				{
					storeChatIdArray[i] = "";
				}
			}
			var openAid = storeChatIdArray[0];
//			alert(openAid+"aaaaaaaaaaaaaaaaa");
			
			closeAid.removeChild(closeIn);
			closeAid.removeChild(closeIn2);
//			document.getElementById(extra2).style.display = "none";
//			closeIn2.id = "";
			if (openAid == "" && storeChatIdArray[1] == "")
			{
				YAHOO.example.container.messengerPanel.hide();
			}
			else
			{
				if (openAid == "" && storeChatIdArray[1] != "")
				{
					createCookie('creatingTabPanelOpen',storeChatIdArray[1],1);
					openingTab(storeChatIdArray[1]);
				}
				else
				{
					createCookie('creatingTabPanelOpen',openAid,1);
					openingTab(openAid);
				}
			}
	//		closeAid.removeChild(closeIn);
	//		closeAid.removeChild(closeIn2);
			
			
//			YAHOO.example.container.messengerPanel.hide();
//			initCheckMessages();
		}
	}
	
	function initChat(userId,cui,aid)
	{
//		DWREngine.setMethod(DWREngine.IFrame);
		MessageDWRModule.initChat(storeChatId, userId, cui, aid);
	}
	
	function storeChatId(chatId)
	{
//		DWREngine.setMethod(DWREngine.IFrame);
		strChatId1 = chatId.substring(0, chatId.indexOf("("));
		strChatId2 = chatId.substring(chatId.indexOf(")")+1);
		var hiddenChatId = document.getElementById("hci_" + strChatId2);
		var newChatId=document.createTextNode(strChatId1);
		hiddenChatId.appendChild(newChatId);
		MessageDWRModule.getExistMessage(retrieveMessage, strChatId1);
	}
	
	function doNothing()
	{}
	
	function checkMessages()
	{
//		alert("really");
//		DWREngine.setMethod(DWREngine.IFrame);
//		DWREngine.setMethod(DWREngine.XMLHttpRequest);
		MessageDWRModule.checkMessage(gotMessage2, cui, true);		
	}
	
	function initCheckMessages()
	{
//		DWREngine.setMethod(DWREngine.IFrame);
		MessageDWRModule.initCheckMessage(gotMessage, cui);		
	}

	
	function gotMessage(messages)
	{	
//	alert("init");
		var currentUserName;
		var userName;
		var create;
		for (var data in messages)
	    {
	    if (messages[data].chatId != null)
		{
	 	    msg = messages[data].msg;
	        chatId = messages[data].chatId;
			msgInfo = messages[data].info;
			for (var data1 in msgInfo)
			{
				uid = msgInfo[data1].userId;
				userData = msgInfo[data1].user;
				if (cui == uid)
				{
					currentUserName = userData.username;
					saved = msgInfo[data1].save;
				}
				else
				{	
					if(userData != null)
					{
					if(chatId.substring(0,6) == "group_")
					{
						if (document.getElementById(chatId) == null)
						{
							for(var i = 1; i < trying+1; i++)
							{
								if(document.getElementById("hci_Group No."+i) == null)
								{
									aid = "Group No." +  trying;
								}
							}
						}
						else
						{
							aid = document.getElementById(chatId).innerHTML;
							
						}
					}
					else
					{
						userName = userData.name;
						aid = "chtwnd_" + userName;
					}
				}}
			}	
			create = "false";
			readingMessages(chatId, cui);
/*			var len = 500;
			if (msg.length > len)
			{
				msg = msg.substring(0, len);
   				msg = msg.replace(/\w+$/, 'truncated');
			}*/
			createAccor(aid,cui,msg,chatId,currentUserName,uid,saved,create);
			createCookie('openingTabPanel',true,1);
			createCookie('creatingOpenPanel',true,1);	
			}	
	    }
	    setTimeout("checkingMessages()",20000);
	}
	
	function gotMessage2(messages)
	{	
//	alert("check");
		var currentUserName;
		var userName;
		var create;
		for (var data in messages)
	    {
	    if (messages[data].chatId != null)
		{
	 	    msg = messages[data].msg;
	        chatId = messages[data].chatId;
			msgInfo = messages[data].info;
			for (var data1 in msgInfo)
			{
				uid = msgInfo[data1].userId;
				userData = msgInfo[data1].user;
				if (cui == uid)
				{
					currentUserName = userData.username;
					saved = msgInfo[data1].save;
				}
				else
				{	
				if(userData != null)
				{
					if(chatId.substring(0,6) == "group_")
					{
						if (document.getElementById(chatId) == null)
						{
							for(var i = 1; i < trying+1; i++)
							{
								if(document.getElementById("hci_Group No."+i) == null)
								{
									var groupUser = "";
									aid = "Group No." +  trying;
									groupUser = groupUser+","+ userData.name;
								}
							}
							document.getElementById("groupChatConversation").innerHTML = "<a href =\"#\" class = \"groupClass\" id=\""+chatId+"\" onclick=\"createAccor('"+aid+"','"+cui+"','','"+chatId+"','"+userName+"','','false','true') onmouseover=\"Tip('"+groupUser+"')\"\">"+aid+"</a><br/>";
						}
						else
						{
							aid = document.getElementById(chatId).innerHTML;
							
						}
					}
					else
					{
						userName = userData.name;
						aid = "chtwnd_" + userName;
					}
				}}
			}
			create = "false";
			readingMessages(chatId, cui);
//			MessageDWRModule.readMessage(doNothing, chatId, cui);
/*			var len = 500;
			if (msg.length > len)
			{
				msg = msg.substring(0, len);
   				msg = msg.replace(/\w+$/, 'truncated');
			}	*/
			createAccor(aid,cui,msg,chatId,currentUserName,uid,saved,create);
			createCookie('openingTabPanel',true,1);
			createCookie('creatingOpenPanel',true,1);
			document.getElementById("workingTab_"+aid).style.backgroundColor ="#FF3333";
			document.getElementById("workingTab_"+aid).style.color ="#FFFFFF";	
			document.getElementById("workingTab_"+aid).style.border ="1px solid #CC3333";

			if(!blur) 
			{
				blur = true;
			}
			setTimeout("bluring()", 1000);
			}
	    }
	    var x = readCookie('creatingOpenPanel');
		if(x == "true")
		{
	   		 setTimeout("checkingMessages()", 30000);
	   	}
	   	else
	   	{
	   		setTimeout("checkingMessages()", 100000);
	   	}
	}
	
	function readingMessages(chatId, cui)
	{
//		DWREngine.setMethod(DWREngine.IFrame);
		MessageDWRModule.readMessage(doNothing, chatId, cui);
	}
	
	var blur = false;
	
	function bluring()
	{
		if(blur)
		{
			if(document.title=="New Message - Enterprise Knowledge Portal")
			{
				document.title = "Enterprise Knowledge Portal";
			}
			else
			{			
				document.title = "New Message - Enterprise Knowledge Portal";
			}
			setTimeout("bluring()", 30000)
		}
	}
	
	window.onfocus = function()
	{
		document.title = "Enterprise Knowledge Portal";
		blur = false;
	}