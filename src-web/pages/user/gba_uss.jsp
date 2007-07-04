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

<%@ page import="java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.cx.CxConstants, de.fhg.fokus.hss.zh.ZhConstants,
	de.fhg.fokus.hss.web.form.*, de.fhg.fokus.hss.web.util.WebConstants " %>
<html>
<head>
<%
	int id_impi = Integer.parseInt(request.getParameter("id_impi"));
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> GBA-USS </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">

<script type="text/javascript" language="JavaScript">
function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.GBA_USS_Form.nextAction.value="save";
			document.GBA_USS_Form.submit();
			break;
		case 2:
			document.GBA_USS_Form.nextAction.value="refresh";
			document.GBA_USS_Form.submit();			
			break;
		case 3:
			document.GBA_USS_Form.nextAction.value="reset";
			document.GBA_USS_Form.reset();
			break;
		case 6:
			document.GBA_USS_Form.nextAction.value="delete_uss";
			document.GBA_USS_Form.associated_ID.value = associated_ID;			
			document.GBA_USS_Form.submit();			
			break;

		case 13:
			document.GBA_USS_Form.nextAction.value="save_uss";
			document.GBA_USS_Form.submit();
			break;
	}
}

function addUSS(id_impi){
	document.GBA_USS_Form.nextAction.value = "add_uss";
	document.GBA_USS_Form.id_impi.value = id_impi;
	document.GBA_USS_Form.submit();
}
</script>
</head>

<body>
	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> GBA-USS Settings </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="/GBA_USS_Submit">
		<html:hidden property="nextAction" value=""/>
		<html:hidden property="associated_ID" value=""/>
		<html:hidden property="id_impi" />
	
		<table id="main-table" align="center" valign="middle">
		<tr>
			<td>
	 			<table id="impi-table" border="0" align="center" width="350" >						
	 			<tr>
 					<td>
						<table id="fields-table" class="as" border="0" cellspacing="1" align="center" width="100%" style="border:2px solid #FF6600;">						
						
						<!-- Private Identity -->
						<tr bgcolor="#FFCC66">
							<td>Private Identity </td>
							<td><html:text property="identity" readonly="true" styleClass="inputtext_readonly"/> </td>
						</tr>
						
						<!-- UICC Type -->						
						<tr bgcolor="#FFCC66">
							<td>UICC Type </td>
							<td>								
								<html:select property="uicc_type" name="GBA_USS_Form" styleClass="inputtext" size="1" style="width:250px;">
									<html:optionsCollection name="GBA_USS_Form" property="select_uicc_type" label="name" value="code"/>
								</html:select>
							</td>
						</tr>
						
						<!-- Key Life Time -->
						<tr bgcolor="#FFCC66">
							<td>Key Life Time</td>
							<td>
								<html:text property="key_life_time" styleClass="inputtext"/> 
							</td>
						</tr>

						<!-- Default -->
						<tr bgcolor="#FFCC66">
							<td>Zh Default Auth-Scheme </td>
							<td>
								<html:select property="default_auth_scheme" name="GBA_USS_Form" styleClass="inputtext" size="1" style="width:250px;">
									<html:optionsCollection name="GBA_USS_Form" property="select_zh_auth_scheme" label="name" value="code"/>
								</html:select>
							</td>
						</tr>
						</table> <!-- fields-table -->
					</td>
				</tr>	
 				<tr>
 					<td>
						<table id="buttons-table" class="as" align="center">			
						<tr>
							<td>
								<a href="/hss.web.console/IMPI_Load.do?id=<%=id_impi%>">
									<= Back to IMPI 
								</a>
							</td>
						</tr>
						<tr>
							<td align=center> <br/>
								<%
									 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
								%>							
										<html:button property="save_button" value="Save" onclick="add_action_for_form(1, -1);"/>				
								<%
									}
								%>	
								<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(2, -1);"/> 
							</td>
						</tr>
						</table> <!-- buttons-table -->			 
					</td>
				</tr>	
				</table> <!-- impi-table -->		
			</td>
			<td> <br/> </td>
		</tr>
		<tr>		
			<td>
				<table id="uss-main-table">
				<tr>
					<td> <b> User Security Settings List </b> </td>
				</tr>
				<tr>
					<td>		
						<table id="uss-fields-table" class="as" border="0" cellspacing="1" align="center" width="600" style="border:2px solid #FF6600;">							
						<tr class="header">
							<td class="header"> Type </td>			
							<td class="header"> Flags </td>
							<td class="header"> NAF Group </td>
							<%
								 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
							%>							
									<td class="header"> Delete </td>
							<%
								}
							%>	
						</tr>
						<%
							int idx = 0;
						%>
						<nested:iterate property="ussList" name="GBA_USS_Form"
								id="uss" type="USS_Form" indexId="ix">
							
							<tr class="<%= (idx % 2 == 0 ? "even" : "odd") %>">
								<nested:hidden property="id_uss"> </nested:hidden> 
								<td>								
									<nested:select property="type" onchange="add_action_for_form(13, -1);">
										<nested:optionsCollection property="select_uss_type" label="name" value="code"/>
									</nested:select>								
								</td>
								
								<td>
									<%
										if (uss.getType() == ZhConstants.GAA_Service_Type_PKI_Portal){
									%>
									Authentication Allowed
									<nested:checkbox property="auth_allowed" styleClass="inputtext" onclick="add_action_for_form(13, -1);"/>
									<br />
									Non-Repudiation Allowed
									<nested:checkbox property="non_repudiation_allowed" styleClass="inputtext" onclick="add_action_for_form(13, -1);"/>
									<%
										}
										else{
											out.println("Not yet standardized");
										}
									%>
								</td>
								
								<td>
									<nested:text property="nafGroup" size="10" styleClass="inputtext"
										style="width:200px;" onchange="add_action_for_form(13, -1);"/>
								</td>
								
								<%
									 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
								%>								
								<!-- Add Delete button -->
								<td>
									<input type="button" name="delete_button" value="Delete" 
										onclick="add_action_for_form(6, <%= uss.getId_uss() %>);" />		
								</td>	
								<%
									}
								%>
							</tr>
								
							<%	
								idx++;
							%>
						</nested:iterate>
						</table> <!-- uss-fields-table -->
						
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>						
						<a href="javascript:addUSS(<%= id_impi %>);">
							<img src="/hss.web.console/images/add_obj.gif" width="16"
								height="16" alt="Add" />
						</a>
						<%
							}
						%>						
					</td>
				</tr>
				</table> <!-- uss-main-table -->
			</td>
		</tr>
		</table> <!-- main-table -->
	</html:form>
</body>
</html>
