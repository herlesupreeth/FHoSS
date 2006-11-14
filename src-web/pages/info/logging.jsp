<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ page import="de.fhg.fokus.hss.util.LoggerHelper"%>
<%
	// This jsp access hibernate directly to collect
	// information about the hss data content.
	if(request.getParameter("clear") != null){
		LoggerHelper.clear();
	}
	if(request.getParameter("off") != null){
		LoggerHelper.off();
	}
	if(request.getParameter("start") != null){
		LoggerHelper.init("debug");
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
					Logging - Current Buffer
					<a href="logging.jsp" target="content">[REFRESH]</a>
					<a href="logging.jsp?clear=true" target="content">[CLEAR]</a>
					<% if(LoggerHelper.STATUS == true) { %>
						<a href="logging.jsp?off=true" target="content">[TURN OFF]</a>
					<% } else { %>
						<a href="logging.jsp?start=true" target="content">[TURN ON]</a>
					<% } %>
					
				</td>
			</tr>
			<tr class="formular">
				<td><textarea style="width:100%;height:100%;"><%=LoggerHelper.BUFFER.getBuffer().toString()%></textarea></td>
			</tr>
		</table>
		</td>
	</tr>
</table>				
</body>
</html>
