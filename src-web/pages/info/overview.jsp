<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="de.fhg.fokus.hss.diam.cx.MARCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.cx.UARCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.cx.LIRCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.cx.SARCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.cx.RTRCommandAction"%>
<%@ page import="de.fhg.fokus.hss.diam.cx.PPRCommandAction"%>
<%@ page import="de.fhg.fokus.hss.diam.sh.PNRCommandAction"%>
<%@ page import="de.fhg.fokus.hss.diam.sh.UDRCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.sh.PURCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.sh.SNRCommandListener"%>
<%@ page import="de.fhg.fokus.hss.diam.zh.MARzhCommandListener"%>
<%@ page import="org.hibernate.Session"%>
<%@ page import="de.fhg.fokus.hss.util.HibernateUtil" %>
<html>
<head>
	<title>Unbenannt</title>
<style>
.interface {
	background-color:#FFCC00;
	text-align:left;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	padding-left:10px;
	font-weight:600;
	color:#000000;
	border: 1px solid #000000
}
.link{
	text-align:left;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-weight:400;
	text-decoration:none;
	color:#808000;
}
.project {
	background-color:#99DDCE;
	text-align:left;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	padding-left:10px;
	font-weight:600;
	border: 1px solid #000000
}
</style>
	
</head>
<%
	// This jsp access hibernate directly to collect
	// information about the hss data content.
	Session hibernateSession = HibernateUtil.getCurrentSession();
	HibernateUtil.beginTransaction();
	
%>
<body>
<table width="400" height="300" bordercolor="#000000" border="1" cellpadding="0" cellspacing="9" bgcolor="#99DDCE">
<tr>
	<td class="project">Console</td>
	<td class="interface">
		Cx <A href="commands.jsp" class="link" target="content">[<%=(MARCommandListener.counter + SARCommandListener.counter + UARCommandListener.counter + LIRCommandListener.counter + PPRCommandAction.counter + RTRCommandAction.counter)%>]</A>
	</td>
</tr>
<tr>
	<td class="project">Core <A href="commands.jsp" class="link" target="content">[...]</A></td>
	<td class="interface">
		Sh <A href="commands.jsp" class="link" target="content">[<%=(UDRCommandListener.counter + SNRCommandListener.counter + PURCommandListener.counter + PNRCommandAction.counter) %>]</A>
	</td>
</tr>
<tr>
	<td class="project">Model <A href="data.jsp" class="link" target="content">[<%=hibernateSession.createCriteria(de.fhg.fokus.hss.model.Imsu.class).list().size()%>]</A></td>
	<td class="interface">
		Zh <A href="commands.jsp" class="link" target="content">[<%=MARzhCommandListener.counter%>]</A>
	</td>
</tr>
</table>



</body>
</html>
