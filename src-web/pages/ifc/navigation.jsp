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
			<bean:message key="svp.title" />  <br>
			<a href="svpSearch.jsp" target="content">Search</a><br>
			<% if(request.isUserInRole(SecurityPermissions.SP_SVP)) { %>
			<a href="/hss.web.console/svpCreate.do?svpId=-1" target="content">Create</a> <br>
			<% } %>
			<br>		
			<bean:message key="as.title" />  <br>
			<a href="asSearch.jsp" target="content">Search</a><br>
			<% if(request.isUserInRole(SecurityPermissions.SP_APSVR)) { %>
			<a href="/hss.web.console/asCreate.do?asId=-1" target="content">Create</a> <br>
			<% } %>
			<br>
			<bean:message key="triggerPoint.title" /> <br>
			<a href="triggerPointSearch.jsp" target="content">Search</a><br>
			<% if(request.isUserInRole(SecurityPermissions.SP_IFC)) { %>
			<a href="/hss.web.console/triggerPointCreate.do?trigPtId=-1" target="content">Create</a> <br>
			<% } %>
			<br>			
			<bean:message key="ifc.title" /> <br>
			<a href="ifcSearch.jsp" target="content">Search</a><br>
			<% if(request.isUserInRole(SecurityPermissions.SP_IFC)) { %>
			<a href="/hss.web.console/ifcCreate.do?ifcId=-1" target="content">Create</a> <br>
			<% } %>
			<br>
			<bean:message key="psiTempl.title" /> <br>
			<a href="psiTemplSearch.jsp" target="content">Search Template</a><br>
			<% if(request.isUserInRole(SecurityPermissions.SP_PSI)) { %>
			<a href="/hss.web.console/psiTemplCreate.do?psiTemplId=-1" target="content">Create Template</a> <br>
			<% } %>
			<br>
			<a href="psiSearch.jsp" target="content">Search PSI</a><br>
			<% if(request.isUserInRole(SecurityPermissions.SP_PSI)) { %>
			<a href="/hss.web.console/psiCreate.do?psiId=-1" target="content">Create PSI</a> <br>
			<% } %>
		</td>
	</tr>
</table>

</body>