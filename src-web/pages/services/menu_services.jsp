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
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> Services Menu Page</title>
</head>

<body>
<table id="main" height="100%">
	<tr>
		<td id="bound_left">&nbsp;</td>
		<td valign="top" bgcolor="#FFFFFF">
			<h2> Services </h2>
			<ul>
				<!-- Service Profile -->
				<li>
					<b> Service Profiles </b>
					<br>
					<a href="sp_search.jsp" target="content"> Search </a>
					<br>
				<%
					if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) {
				%>
					<a href="/hss.web.console/SP_Load.do?id=-1" target="content"> Create </a>
					<br>
				<%
					}
				%>
				</li>
				<br>


				<!-- Application Server -->
				<li>
					<b> Application Servers </b>
					<br>
					<a href="as_search.jsp" target="content"> Search </a>
					<br>
				<%
					if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) {
				%>
					<a href="/hss.web.console/AS_Load.do?id=-1" target="content"> Create </a>
					<br>
				<%
					}
				%>
				</li>
				<br>


				<!-- Trigger Points -->
				<li>
					<b> Trigger Points </b>
					<br>
					<a href="tp_search.jsp" target="content"> Search </a>
					<br>
				<%
					if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) {
				%>
					<a href="/hss.web.console/TP_Load.do?id=-1" target="content"> Create </a>
					<br>
				<%
					}
				%>
				</li>
				<br>


				<!-- Initial Filter Criteria -->
				<li>
					<b> Initial Filter Criteria </b>
					<br>
					<a href="ifc_search.jsp" target="content"> Search </a>
					<br>
				<%
					if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) {
				%>
					<a href="/hss.web.console/IFC_Load.do?id=-1" target="content"> Create </a>
					<br>
				<%
					}
				%>
				</li>
				<br>


				<!-- Shared iFC Sets -->
				<li>
					<b> Shared iFC Sets </b>
					<br>
					<a href="s_ifc_search.jsp" target="content"> Search </a>
					<br>
				<%
					if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) {
				%>
					<a href="/hss.web.console/S_IFC_Load.do?id=-1" target="content"> Create </a>
					<br>
				<%
					}
				%>
				</li>
				<br>


				<!-- DSAI -->
				<li>
					<b> DSAI </b>
					<br>
					<a href="dsai_search.jsp" target="content"> Search </a>
					<br>
				<%
					if(request.isUserInRole(WebConstants.Security_Permission_ADMIN)) {
				%>
					<a href="/hss.web.console/DSAI_Load.do?id=-1" target="content"> Create </a>
					<br>
				<%
					}
				%>
				</li>
				<br>
			</ul>
		</td>
	</tr>
</table>

</body>