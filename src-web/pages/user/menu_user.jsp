<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="de.fhg.fokus.hss.path.TreeNode"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ page import="de.fhg.fokus.hss.web.util.WebConstants" %>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Menu Page</title>
</head>
<body>
<table id="main" height="100%">
	<tr>
		<td id="bound_left">&nbsp;</td>
		<td valign="top" bgcolor="#FFFFFF">
			<h2> User Identities </h2>
			
			<ul>

			<!-- IMSU-->
			<li> <b> IMS Subscription </b> <br>
			
			<!-- link to search IMSU-->
			<a href="imsu_search.jsp?action=search" target="content"> Search </a> <br>
			
			<!-- link to create IMSU-->			
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/IMSU_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			<!-- IMPI-->
			<li> <b> Private Identity </b> <br>
			
			<!-- link to search IMPI-->
			<a href="impi_search.jsp" target="content"> Search </a> <br>
			
			<!-- link to search IMPI-->
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/IMPI_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			<!-- IMPU-->
			<li> <b> Public User Identity </b> <br>			
			
			<!-- link to search IMPU-->
			<a href="impu_search.jsp" target="content"> Search </a> <br>
			
			<!-- link to create IMPU -->
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/IMPU_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>
			
			</ul>
		</td>
	</tr>
</table>

</body>