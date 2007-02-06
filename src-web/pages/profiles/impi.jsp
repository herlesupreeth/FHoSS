<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile Page</title>
<script language="JavaScript">
	function changeView()
	{
		document.forms.impiForm.action = "impiShow.do";
		document.forms.impiForm.submit();
	}

		function updateSqn(){
			newValue = prompt("<bean:message key="impi.head.sqn.reset" />" ,"000000000000");
			if(newValue.length == 12){
				document.forms.impiForm.sqn.value = newValue;
				document.forms.impiForm.sqnUpdate.value = "1";
			} else {
				alert("<bean:message key="impi.error.size.sqn" />");
			}
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
				<h1><bean:message key="impi.title" /></h1>
				<html:form action="/impiSubmit">
					<table border="0">
						<tr>
							<td></td>
							<td> <html:hidden
								property="impiId" /> <html:hidden property="imsuId" /></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.id" />*</td>
							<td><html:text styleClass="inputtext" property="impiString" style="width:325px;"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.imsi" /></td>
							<td><html:text styleClass="inputtext" property="imsi" style="width:325px;" /></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.sCscfName" /></td>
							<td><html:text styleClass="inputtext_readonly" property="scscfName" style="width:325px;" readonly="true"/></td>
						</tr>
						<tr>
							<td colspan="2">
								<b><bean:message key="impi.head.security" /></b>
							</td>
						</tr>
    					<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.authScheme" /></td>
							<td valign="top">
								<html:select property="authScheme"
									styleClass="inputtext" size="1" style="width:325px;">
									<!-- <html:option value="">Select ...</html:option> -->
									<html:optionsCollection property="authSchemes" label="label" value="value"/>
								</html:select>							
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.asBytes" /></td>
							<td>
								<html:radio styleClass="inputtext" property="asBytes" onchange="javascript:changeView();" value="false" /> yes
								<html:radio styleClass="inputtext" property="asBytes" onchange="javascript:changeView();" value="true" /> no
							</td>
						</tr>
						<logic:equal value="true" name="impiForm" property="asBytes">
							<tr>
								<td><bean:message key="impi.head.sKey" /></td>
								<td><html:text styleClass="inputtext" property="skey" style="width:325px;" maxlength="32"/></td>
							</tr>	
						</logic:equal>
						<logic:notEqual value="true" name="impiForm" property="asBytes">						
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.sKey" /></td>
							<td><html:text styleClass="inputtext" property="skeyChars" style="width:185px;" maxlength="16"/></td>
						</tr>	
						</logic:notEqual>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.amf" /></td>
							<td><html:text styleClass="inputtext" property="amf" size="4" maxlength="4"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.operatorId" /></td>
							<td><html:text styleClass="inputtext" property="operatorId" style="width:325px;" maxlength="32"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap"><bean:message key="impi.head.sqn" /></td>
							<td>
								<html:text styleClass="inputtext_readonly" property="sqn" size="12" maxlength="12" readonly="true"/>								
								<input type="button" onclick="javascript:updateSqn();" value="Reset">
								<logic:notEqual value="-1" property="impiId" name="impiForm">
									<html:hidden styleClass="inputtext" property="sqnUpdate"/>
								</logic:notEqual>
								<logic:equal value="-1" property="impiId" name="impiForm">
									<html:hidden styleClass="inputtext" property="sqnUpdate" value="1"/>
								</logic:equal>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<b><bean:message key="impi.head.chrginfo" /></b>
							</td>
						</tr>
						<tr>						
							<td nowrap="nowrap">
								<bean:message key="impi.head.chrginfo" />
							</td>
							<td>
								<html:select property="chrgInfoId" name="impiForm"
										styleClass="inputtext" size="1" style="width:225px;">
										<html:optionsCollection name="impiForm" property="chrgInfos" label="name" value="chrgId"/>
								</html:select>	
							</td>
						</tr>
					</table>
					
					<b><bean:message key="impi.head.guss" /> </b>
					<p style="text-align:left;">
					<logic:notEqual value="-1" property="impiId" name="impiForm">
						<a href="gussShow.do?impiId=<bean:write name="impiForm" property="impiId" />">
							-> Configure 
						</a>
					</logic:notEqual></p>
											
					<p style="text-align:right;">
					<% if(request.isUserInRole(SecurityPermissions.SP_IMPI)) { %>
					<input type="image"
						src="/hss.web.console/images/save_edit.gif" width="16" height="16"
						alt="Save">
					<% } %>

					<a href="impiShow.do?impiId=<bean:write name="impiForm" property="impiId" />">
						[Refresh]
					</a>
					</p>
						
				</html:form> 
				
				<logic:notEqual value="-1" property="impiId" name="impiForm">			
				<jsp:include page="/pages/tiles/impuSelect.jsp">
					<jsp:param name="formName" value="impiForm"/>
					<jsp:param name="thisTarget" value="/impiShow"/>
					<jsp:param name="connectTarget" value="/impu2impi.do"/>
					<jsp:param name="parentId" value="impiId"/>
					<jsp:param name="addAllow" value="<%= request.isUserInRole(SecurityPermissions.SP_IMPI) ? "true" : "false" %>"/>
				</jsp:include>
				<h1>
					<bean:message key="roam.title" />
					<a href="roamEdit.do?impiId=<bean:write name="impiForm" property="impiId" />">
						<img src="/hss.web.console/images/edit_small.gif" width="16"
							height="16">
					</a>
				</h1>
				<table class="as" width="100%" border=0>
					<tr class="header">
						<td class="header"><bean:message key="roam.head.id" />&nbsp;
							</td>
					</tr>
					<logic:iterate name="impiForm" property="roamNetworkIdentifiers"
						id="roam" type="java.lang.String" indexId="ix">
						<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
							<td><bean:write name="roam" /></td>
						</tr>
					</logic:iterate>
				</table>
			</logic:notEqual></td>
			</tr>
		</table>

		</td>
		<td id="bound_right"></td>
	</tr>
</table>

</body>
</html>
