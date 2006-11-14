<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
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
		<table width="500" class="as">
			<tr class="header">
				<td colspan="2">
					System
				</td>
			</tr>
			<tr class="formular">
				<td>Current Time</td>
				<td><%=java.util.Calendar.getInstance().getTime().toLocaleString()%></td>
			</tr>
			<tr class="formular">
				<td>Server running time (in sec)</td>
				<% long diff = (int)((System.currentTimeMillis() - de.fhg.fokus.hss.main.HssServer.STARTUP)/1000); %>
				<td><%=diff %></td>
			</tr>
			<tr class="header">
				<td colspan="2"><bean:message key="info.cx.title" /></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.cx.counter.uar" /></td>
				<td><%=UARCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.cx.counter.mar" /></td>
				<td><%=MARCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.cx.counter.sar" /></td>
				<td><%=SARCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.cx.counter.lir" /></td>
				<td><%=LIRCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.cx.counter.ppr" /></td>
				<td><%=PPRCommandAction.rCounter%></td>
			</tr>			
			<tr class="formular">
				<td><bean:message key="info.cx.counter.ppa" /></td>
				<td><%=PPRCommandAction.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.cx.counter.rtr" /></td>
				<td><%=RTRCommandAction.rCounter%></td>
			</tr>			
			<tr class="formular">
				<td><bean:message key="info.cx.counter.rta" /></td>
				<td><%=RTRCommandAction.counter%></td>
			</tr>
			<tr class="header">
				<td colspan="2"><bean:message key="info.sh.title" /></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.sh.counter.udr" /></td>
				<td><%=UDRCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.sh.counter.pur" /></td>
				<td><%=PURCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.sh.counter.snr" /></td>
				<td><%=SNRCommandListener.counter%></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.sh.counter.pnr" /></td>
				<td><%=PNRCommandAction.counter%></td>
			</tr>
			<tr class="header">
				<td colspan="2"><bean:message key="info.zh.title" /></td>
			</tr>
			<tr class="formular">
				<td><bean:message key="info.zh.counter.mar" /></td>
				<td><%=MARzhCommandListener.counter%></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
