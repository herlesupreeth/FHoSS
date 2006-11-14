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
<%@ page import="de.fhg.fokus.hss.model.SptBO.SIP_METHOD"%>
<%@ page import="de.fhg.fokus.hss.model.TrigptBO"%>
<%@ page import="de.fhg.fokus.hss.util.SecurityPermissions" %>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/hss.web.console/style/fokus_ngni.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trigger Point Page</title>
<script language="JavaScript" type="text/javascript">
	function addSpt(groupId){
		document.triggerPointForm.type.value = document.triggerPointForm.typeSelect[groupId].value;
		document.triggerPointForm.group.value = groupId;
		document.triggerPointForm.submit();
	}
	function showXML(){
		document.triggerPointForm.action = "triggerPointShowXML.do";
		document.triggerPointForm.submit();
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
				<h1><bean:message key="triggerPoint.title" /></h1>
				<html:form action="/triggerPointSubmit">
					<html:hidden property="group" />
					<html:hidden property="type" />
					<html:hidden property="trigPtId" />
					<table border="0">
						<tr>
							<td><bean:message key="triggerPoint.head.name" />*</td>
							<td><html:text styleClass="inputtext" property="trigPtName"
								style="width:270px;" /></td>
						</tr>
						<tr>
							<td><bean:message key="triggerPoint.head.cnf" />*</td>
							<td>CNF<html:radio styleClass="inputtext" property="cnf"
								value="1" /> DNF<html:radio styleClass="inputtext"
								property="cnf" value="0" /></td>
						</tr>
					</table>
					<logic:notEqual value="-1" property="trigPtId"
						name="triggerPointForm">
						<br>
						<table width="730" class="as" cellspacing="0" cellpadding="0">
							<tr class="header">
								<td width="15"></td>
								<td><br>
								<table class="as" cellpadding="0" cellspacing="0" border="0"
									width="700">
									<%int lastGroupId = 0;
			%>
									<nested:iterate property="spts" name="triggerPointForm"
										id="spt" type="de.fhg.fokus.hss.form.SptForm" indexId="ix">
										<%if (lastGroupId != spt.getGroup()) {

				%>
										<tr class="<%= lastGroupId%2 == 0 ? "even" : "odd" %>">
											<td style="text-align:center;" colspan="6"><select name="typeSelect">
												<option value="<%=TrigptBO.TYPE_URI%>">Request URI</option>
												<option value="<%=TrigptBO.TYPE_SIP_METHOD%>">Sip Method</option>
												<option value="<%=TrigptBO.TYPE_SIP_HEADER%>">Sip Header</option>
												<option value="<%=TrigptBO.TYPE_SESSION_DESC%>">Session Desc</option>
												<option value="<%=TrigptBO.TYPE_SESSION_CASE%>">Session Case</option>
											</select> <a href="javascript:addSpt(<%=lastGroupId%>)"><img
												src="/hss.web.console/images/add_obj.gif" width="16"
												height="16" alt="Add" /></a></td>
										</tr>
									<tr class="header">
										<td style="text-align:center;" colspan="6"><logic:equal
											name="triggerPointForm" property="cnf" value="1">AND</logic:equal>
										<logic:equal name="triggerPointForm" property="cnf" value="0">OR</logic:equal>
										</td>
									</tr>
									<%lastGroupId = spt.getGroup();
			}

			%>
									<tr class="<%= lastGroupId%2 == 0 ? "even" : "odd" %>">
										<td nowrap="nowrap" width="120"><nested:hidden
											property="sptId"></nested:hidden> <nested:hidden
											property="group"></nested:hidden> <nested:hidden
											property="type"></nested:hidden> <%if (spt.getType() == TrigptBO.TYPE_URI) {

			%> <bean:message key="spt.head.requestUri" /></td>
										<td class="tgpFormular"><nested:text property="requestUri"
											styleClass="inputtext" style="width:280px;" maxlength="255" />
										</td>
										<td nowrap="nowrap"><bean:message key="spt.head.neg" /></td>
										<td class="tgpFormular"><nested:checkbox property="neg"
											styleClass="inputtext" /></td>
										<%} else if (spt.getType() == TrigptBO.TYPE_SIP_METHOD) {

			%>
										<bean:message key="spt.head.sipMethod" />
										</td>
										<td class="tgpFormular"><nested:select property="sipMethod"
											styleClass="inputtext" style="width:280px;">
											<nested:optionsCollection property="sipMethodList" />
										</nested:select></td>
										<td nowrap="nowrap"><bean:message key="spt.head.neg" /></td>
										<td class="tgpFormular"><nested:checkbox property="neg"
											styleClass="inputtext" /></td>
										<%} else if (spt.getType() == TrigptBO.TYPE_SESSION_CASE) {

				%>
										<bean:message key="spt.head.sessionCase" />
										</td>
										<td class="tgpFormular"><nested:select property="sessionCase"
											styleClass="inputtext" style="width:280px;">
											<option value="" /></option>
											<option value="0"
												<%= ((spt.getSessionCase() != null)&&(spt.getSessionCase().equals("0"))) ? "selected" : ""  %> />ORIGINATING</option>
											<option value="1"
												<%= ((spt.getSessionCase() != null)&&(spt.getSessionCase().equals("1"))) ? "selected" : ""  %> />TERMINATING</option>
											<option value="2"
												<%= ((spt.getSessionCase() != null)&&(spt.getSessionCase().equals("2"))) ? "selected" : ""  %> />UNREGISTER</option>
										</nested:select></td>
										<td nowrap="nowrap"><bean:message key="spt.head.neg" /></td>
										<td class="tgpFormular"><nested:checkbox property="neg"
											styleClass="inputtext" /></td>
										<%} else if (spt.getType() == TrigptBO.TYPE_SESSION_DESC) {

			%>
										<bean:message key="spt.head.sessionDesc" />
										</td>
										<td>
										<table>
											<tr>
												<td class="tgpFormular"><bean:message
													key="spt.head.sessionDescLine" /></td>
												<td class="tgpFormular" nowrap="nowrap" colspan="3"><nested:text
													property="sessionDescLine" size="10" styleClass="inputtext"
													style="width:200px;" /></td>
											</tr>
											<tr>
												<td class="tgpFormular" nowrap="nowrap"><bean:message
													key="spt.head.sessionDescContent" /></td>
												<td class="tgpFormular" colspan="2"><nested:text
													property="sessionDescContent" size="10"
													styleClass="inputtext" style="width:200px;" /></td>
											</tr>
										</table>
										</td>
										<td nowrap="nowrap"><bean:message key="spt.head.neg" /></td>
										<td class="tgpFormular"><nested:checkbox property="neg"
											styleClass="inputtext" /></td>
										<%} else if (spt.getType() == TrigptBO.TYPE_SIP_HEADER) {

			%>
										<bean:message key="spt.head.sipHeader" />
										</td>
										<td>
										<table>
											<tr>
												<td class="tgpFormular"><bean:message
													key="spt.head.sipHeader" /></td>
												<td class="tgpFormular" colspan="2"><nested:text
													property="sipHeader" size="10" styleClass="inputtext"
													style="width:200px;" /></td>
											</tr>
											<tr>
												<td class="tgpFormular" nowrap="nowrap"><bean:message
													key="spt.head.sipHeaderContent" /></td>
												<td class="tgpFormular" colspan="2"><nested:text
													property="sipHeaderContent" size="10"
													styleClass="inputtext" style="width:200px;" /></td>
											</tr>
										</table>
										</td>
										<td nowrap="nowrap"><bean:message key="spt.head.neg" /></td>
										<td class="tgpFormular"><nested:checkbox property="neg"
											styleClass="inputtext" /></td>
										<%}

			%>
										<td nowrap="nowrap"><bean:message key="form.head.delete" /></td>
										<td class="tgpFormular"><nested:checkbox property="delete"
											styleClass="inputtext" /></td>
									</tr>
									<tr class="<%= lastGroupId%2 == 1 ? "even" : "odd" %>">
										<td style="text-align:center;" colspan="6"><logic:equal
											name="triggerPointForm" property="cnf" value="0">AND</logic:equal>
										<logic:equal name="triggerPointForm" property="cnf" value="1">OR</logic:equal>
										</td>
									</tr>
									</nested:iterate>
									<tr class="<%= lastGroupId%2 == 0 ? "even" : "odd" %>">
										<td style="text-align:center;" colspan="6"><select
											name="typeSelect">
											<option value="<%=TrigptBO.TYPE_URI%>">Request URI</option>
											<option value="<%=TrigptBO.TYPE_SIP_METHOD%>">Sip Method</option>
											<option value="<%=TrigptBO.TYPE_SIP_HEADER%>">Sip Header</option>
											<option value="<%=TrigptBO.TYPE_SESSION_DESC%>">Session Desc</option>
											<option value="<%=TrigptBO.TYPE_SESSION_CASE%>">Session Case</option>
										</select> <a href="javascript:addSpt(<%=lastGroupId%>)"><img
											src="/hss.web.console/images/add_obj.gif" width="16"
											height="16" alt="Add" /></a></td>
									</tr>
								<logic:notEmpty property="spts" name="triggerPointForm">
									<tr class="header">
										<td style="text-align:center;" colspan="6"><logic:equal
											name="triggerPointForm" property="cnf" value="1">AND</logic:equal>
										<logic:equal name="triggerPointForm" property="cnf" value="0">OR</logic:equal>
										</td>
									</tr>
									<%lastGroupId++;%>
									<tr class="<%= lastGroupId%2 == 0 ? "even" : "odd" %>">
										<td style="text-align:center;" colspan="6"><select name="typeSelect">
											<option value="<%=TrigptBO.TYPE_URI%>">Request URI</option>
											<option value="<%=TrigptBO.TYPE_SIP_METHOD%>">Sip Method</option>
											<option value="<%=TrigptBO.TYPE_SIP_HEADER%>">Sip Header</option>
											<option value="<%=TrigptBO.TYPE_SESSION_DESC%>">Session Desc</option>
											<option value="<%=TrigptBO.TYPE_SESSION_CASE%>">Session Case</option>
										</select> <a href="javascript:addSpt(<%=(lastGroupId)%>)"><img
											src="/hss.web.console/images/add_obj.gif" width="16"
											height="16" alt="Add" /></a></td>
									</tr>
								</logic:notEmpty>
								</table>
								<br>
								</td>
								<td width="15"></td>
							</tr>
						</table>
						<p style="text-align:right;">
							<a href="javascript:showXML();">[Show XML]</a>
							<input type="image"
							src="/hss.web.console/images/save_edit.gif" width="16"
							height="16" alt="Save">
						</p>
					</logic:notEqual>
					<% if(request.isUserInRole(SecurityPermissions.SP_IFC)) { %>
					<logic:equal value="-1" property="trigPtId" name="triggerPointForm">
						<p style="text-align:right;"><input type="image"
							src="/hss.web.console/images/save_edit.gif" width="16"
							height="16" alt="Save"></p>
					</logic:equal>
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
