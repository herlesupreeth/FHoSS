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
<title> Network Configuration Menu Page</title>
</head>

<body>
<table id="main" height="100%">
	<tr>
		<td id="bound_left">&nbsp;</td>
		<td valign="top" bgcolor="#FFFFFF">
			<h2> Network Configuration </h2>
			
			<ul>
			
			<!-- Visited Networks -->
			<li> <b> Visited Networks </b> <br>			

			<a href="visited_network_search.jsp" target="content"> Search </a> <br>
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/VN_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			<!-- Charging Sets -->
			<li> <b> Charging Sets </b> <br>
			<a href="charging_set_search.jsp" target="content"> Search </a> <br>
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/CS_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			<!-- Capability-->
			<li> <b> Capability </b> <br>
			<a href="capability_search.jsp" target="content"> Search </a> <br>
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/Cap_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			<!-- Capability Sets-->
			<li> <b> Capability Sets </b> <br>
			<a href="capability_set_search.jsp" target="content"> Search </a> <br>
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/CapS_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			<!-- Preferred S-CSCF Sets -->
			<li> <b> Preferred S-CSCF Sets </b> <br>
			<a href="preferred_scscf_set_search.jsp" target="content"> Search </a> <br>
			<% if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) { %>
				<a href="/hss.web.console/PrefS_Load.do?id=-1" target="content"> Create </a> <br>
			<% } %> <br>

			</ul>
		</td>
	</tr>
</table>

</body>