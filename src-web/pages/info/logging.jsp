<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ page import="de.fhg.fokus.hss.web.util.LoggerHelper" %>
<%
	// This jsp access hibernate directly to collect
	// information about the hss data content.
	String loglevel = "";
	if(request.getParameter("clear") != null){
		LoggerHelper.clear();
	}
	if(request.getParameter("off") != null){
		LoggerHelper.off();
	}
	loglevel= request.getParameter("start");
	if (loglevel != null){
		if(loglevel.equals("info"))
		{
	    	LoggerHelper.STATUS=true;
		    LoggerHelper.loglevel="info";
		    LoggerHelper.init();
		}else{
		    LoggerHelper.STATUS=true;
		    LoggerHelper.loglevel="debug";
		    LoggerHelper.init();
		}
	}
%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Info Page</title>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table class="as" width="100%" height="100%" border="0">
			<tr class="header">
				<td height="10px">
					Logging
					<a href="logging.jsp" target="content">[REFRESH]</a>
					<a href="logging.jsp?clear=true" target="content">[CLEAR]</a>
					<% if(LoggerHelper.STATUS == true) { %>
						<a href="logging.jsp?off=true" target="content">[TURN OFF]</a>
					<% } else { %>
						<a href="logging.jsp?start=debug" target="content">[TURN ON - DEBUG]</a>
						<a href="logging.jsp?start=info" target="content">[TURN ON - INFO]</a>
						
					<% } %>
					
				</td>
			</tr>
			<tr class="formular">
			    <td><textarea style="width:100%;height:100%;wrap:hard"><%=LoggerHelper.BUFFER.getBuffer().toString()%></textarea></td>
			</tr>
		</table>
		</td>
	</tr>
</table>				
</body>
</html>