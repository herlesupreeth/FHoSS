<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested"
	prefix="nested"%>
<%@ page import="de.fhg.fokus.hss.model.GussBO" %>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GUSS Page</title>
<script language="JavaScript" type="text/javascript">
	function changeType(){
		alert("TEST");
	}
</script>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td><jsp:include page="/pages/tiles/error.jsp"></jsp:include>
				<h1><bean:message key="guss.title" /></h1>
				<html:form action="/gussSubmit">
				<html:hidden property="impiId" />
					<table border="0">
						<tr>
							<td><bean:message key="impi.head.id" /></td>
							<td>
								<html:text styleClass="inputtext" property="impiString" style="width:325px;" readonly="true" disabled="true"/>
								<a href="impiShow.do?impiId=<bean:write name="gussForm" property="impiId" />">
									<img src="/hss.web.console/images/edit_small.gif" width="16"
										height="16">
								</a>
							</td>
						</tr>
						<tr>
							<td><bean:message key="guss.head.uiccType" />&nbsp;</td>
							<td>
								GBA <html:radio styleClass="inputtext" property="uiccType" value="<%=String.valueOf(GussBO.GUS) %>"/>
								GBA_U <html:radio styleClass="inputtext" property="uiccType" value="<%=String.valueOf(GussBO.GUS_U) %>"/>
							</td>
						</tr>
						<tr>
							<td><bean:message key="guss.head.keyLifeTime"/></td>
							<td><html:text styleClass="inputtext" property="keyLifeTime" style="width:80px;" styleClass="inputtext"/></td>
						</tr>						
					</table>
					<h1><bean:message key="guss.uss.title" /></h1>
					<table class="as" cellpadding="0" cellspacing="1" border="0">
					<tr class="header">
						<td nowrap="nowrap"><bean:message key="guss.head.ussType" /></td>
						<td nowrap="nowrap"><bean:message key="guss.head.flag" /></td>
						<td nowrap="nowrap"><bean:message key="guss.head.nafGroup" /></td>
						<td nowrap="nowrap"><bean:message key="form.head.delete" /></td>
					</tr>					
					<nested:iterate property="ussList" name="gussForm" id="uss"
							type="de.fhg.fokus.hss.form.UssForm" indexId="ix">
						<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
							<td>
								<nested:hidden property="id"></nested:hidden>
								<nested:select property="ussType" styleClass="inputtext">
									<nested:optionsCollection property="ussTypeList" name="uss" label="label" value="value"/>
								</nested:select>
							</td>
							<td>
								<table><tr><td>
								Authentication allowed</td><td><nested:radio property="flag" value="<%=String.valueOf(GussBO.FLAG_Authentication_allowed)%>"/>
								</td></tr><tr><td>
								Non-repudiation allowed</td><td><nested:radio property="flag" value="<%=String.valueOf(GussBO.FLAG_Non_repudiation_allowed)%>"/>
								</td></tr></table>
							</td>
							<td><nested:text property="nafGroup" styleClass="inputtext"></nested:text></td>	
							<td><nested:checkbox property="delete" styleClass="inputtext" /></td>
						</tr>
					</nested:iterate>
					</table>
					<% if(request.isUserInRole(SecurityPermissions.SP_IMPI)) { %>
						<p style="text-align:right;">
						<a href="/hss.web.console/gussShow.do?impiId=<bean:write name="gussForm" property="impiId" />&addUss=true" >
						<img
							src="/hss.web.console/images/add_obj.gif" width="16" height="16"
							alt="Add" />
						</a>
						<input type="image"
							src="/hss.web.console/images/save_edit.gif" width="16" height="16"
							alt="Save">
						</p>
					<% } %>
				</html:form></td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>

</body>
</html>
