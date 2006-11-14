<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="de.fhg.fokus.hss.path.TreeNode"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Page</title>
</head>
<body bgcolor="#CCCCCC">
<table id="main" height="100%">
	<tr id="inner_nav">
		<td id="bound_left">&nbsp;</td>
		<td id="level01" valign="top" bgcolor="#FFFFFF">
			<bean:message key="imsu.title" /><br>
			<a href="imsuSearch.jsp" target="content">Search</a> <br>
			<% if(request.isUserInRole(SecurityPermissions.SP_IMSU)) { %>
				<a href="/hss.web.console/imsuCreate.do?imsuId=-1" target="content">Create</a> <br>
			<% } %>
			<br>
			<bean:message key="impi.title" /><br>
			<a href="impiSearch.jsp" target="content">Search</a> <br>
			<br>
			<bean:message key="impu.title" /><br>
			<a href="impuSearch.jsp" target="content">Search</a> <br>
			<% if(request.isUserInRole(SecurityPermissions.SP_IMPU)) { %>
				<a href="/hss.web.console/impuCreate.do?impuId=-1" target="content">Create</a> <br>
			<% } %>
			<br>
			<a href="/hss.web.console/pages/profiles/dStruct.jsp" target="innerNav">Data Navigator</a> <br>
		</td>
	</tr>
</table>

</body>