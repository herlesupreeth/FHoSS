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
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Application Server Page</title>
</head>
<body bgcolor="#FFFFFF">
<table id="main" height="100%">
	<tr id="bound_bottom_black">
		<td valign="top" bgcolor="#FFFFFF">
		<table width="500">
			<tr>
				<td><jsp:include page="/pages/tiles/error.jsp"></jsp:include>
				<h1><bean:message key="as.title" /></h1>
				<html:form action="/asSubmit">
				<html:hidden
								property="asId" />
					<table border="0" width="480">
						<tr>
							<td nowrap="nowrap"><bean:message key="as.head.asName" />*</td>
							<td><html:text styleClass="inputtext" property="asName" style="width:270px;"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="as.head.asAddress" /></td>
							<td><html:text styleClass="inputtext" property="asAddress" style="width:270px;" /></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="as.head.defaultHandling" /></td>
							<td>
								<html:radio styleClass="inputtext" property="defaultHandling" value="0"/> continued
								<html:radio styleClass="inputtext" property="defaultHandling" value="1"/> terminated
							</td>
						</tr>
					</table>
					<h1><bean:message key="as.perm" /></h1>
					<table class="as" width="480">
						<tr class="header">
							<td><bean:message key="as.perm.tag" /></td>
							<td><bean:message key="as.perm.pull" /></td>
							<td><bean:message key="as.perm.upd" /></td>
							<td><bean:message key="as.perm.sub" /></td>							
						</tr>
						<tr class="even">
							<td><bean:message key="as.perm.repData" /></td>
							<td><html:checkbox property="pullRepData"/></td>
							<td><html:checkbox property="updRepData"/></td>
							<td><html:checkbox property="subRepData"/></td>							
						</tr>
						<tr class="odd">
							<td><bean:message key="as.perm.impu" /></td>
							<td><html:checkbox property="pullImpu"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>							
						</tr>
						<tr class="even">
							<td><bean:message key="as.perm.impuUserState" /></td>
							<td><html:checkbox property="pullImpuUserState"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><html:checkbox property="subImpuUserState"/></td>							
						</tr>
						<tr class="odd">
							<td><bean:message key="as.perm.scscf" /></td>
							<td><html:checkbox property="pullScscfName"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><html:checkbox property="subScscfname"/></td>							
						</tr>
						<tr class="even">
							<td><bean:message key="as.perm.ifc" /></td>
							<td><html:checkbox property="pullIfc"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><html:checkbox property="subIfc"/></td>							
						</tr>
						<tr class="odd">
							<td><bean:message key="as.perm.loc" /></td>
							<td><html:checkbox property="pullLocInfo"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>							
						</tr>
						<tr class="even">
							<td><bean:message key="as.perm.userState" /></td>
							<td><html:checkbox property="pullUserState"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>					
						</tr>
						<tr class="odd">
							<td><bean:message key="as.perm.charging" /></td>
							<td><html:checkbox property="pullCharging"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>							
						</tr>
						<tr class="even">
							<td><bean:message key="as.perm.msisdn" /></td>
							<td><html:checkbox property="pullMsisdn"/></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>
							<td><input type="checkbox" disabled="disabled" readonly="readonly"></td>					
						</tr>
						<tr class="odd">
							<td><bean:message key="as.perm.psi" /></td>
							<td><html:checkbox property="pullPsi"/></td>
							<td><html:checkbox property="updPsi"/></td>
							<td><html:checkbox property="subPsi"/></td>								
						</tr>																								
					</table>
					<% if(request.isUserInRole(SecurityPermissions.SP_APSVR)) { %>
					<p style="text-align:right;"><input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save"></p>
					<% } %>
				</html:form> 
				</td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>

</body>
</html>
