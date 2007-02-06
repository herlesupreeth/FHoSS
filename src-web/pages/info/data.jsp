<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ page import="org.hibernate.Session"%>
<%@ page import="de.fhg.fokus.hss.util.HibernateUtil" %>
<%
	// This jsp access hibernate directly to collect
	// information about the hss data content.
	Session hibernateSession = HibernateUtil.getCurrentSession();
	HibernateUtil.beginTransaction();
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
		<table class="as" width="500" border="0">
			<tr class="header">
				<td colspan="2">
					User Profiles
				</td>
			</tr>
			<tr class="formular">
				<td>Size of IMS Subscriptions</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Imsu.class).list().size() %></td>
			</tr>			
			<tr class="formular">
				<td>Size of Private Identifiers</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Impi.class).list().size() %></td>
			</tr>
			<tr class="formular">
				<td>Size of Public User Identifiers</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Impu.class).list().size() %></td>
			</tr>
			<tr class="formular">
				<td>Size of registered Public User Identifiers</td>
				<td bgcolor="#00FF00"><%=hibernateSession.createQuery("from de.fhg.fokus.hss.model.Impu where userStatus = ?").setString(0, de.fhg.fokus.hss.model.Impu.USER_STATUS_REGISTERED).list().size() %></td>
			</tr>
			<tr class="header">
				<td colspan="2">
					Service Profiles
				</td>
			</tr>
			<tr class="formular">
				<td>Size of Service Profiles</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Svp.class).list().size() %></td>
			</tr>		
			<tr class="formular">
				<td>Size of Application Servers</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Apsvr.class).list().size() %></td>
			</tr>
			<tr class="formular">
				<td>Size of IFC's</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Ifc.class).list().size() %></td>
			</tr>
			<tr class="formular">
				<td>Size of Trigger Points</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Trigpt.class).list().size() %></td>
			</tr>
			<tr class="formular">
				<td>Size of PSI's</td>
				<td><%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Psi.class).list().size() %></td>
			</tr>
		</table>
	</td>
	</tr>
</table>						
</body>
</html>
