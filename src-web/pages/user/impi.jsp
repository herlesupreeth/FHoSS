<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.web.util.WebConstants " %>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>IMPI </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">

<jsp:useBean id="associated_IMSU" class="de.fhg.fokus.hss.db.model.IMSU" scope="request"></jsp:useBean>
<jsp:useBean id="associated_IMPUs" type="java.util.List" scope="request"></jsp:useBean>

<%
	int id = Integer.parseInt(request.getParameter("id"));
%>

<script type="text/javascript" language="JavaScript">
function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.IMPI_Form.nextAction.value="save";
			document.IMPI_Form.submit();
			break;
		case 2:
			document.IMPI_Form.nextAction.value="reset";
			document.IMPI_Form.reset();
			break;
		case 3:
			document.IMPI_Form.nextAction.value="refresh";
			document.IMPI_Form.submit();			
			break;
		case 4:
			document.IMPI_Form.nextAction.value="delete";
			document.IMPI_Form.submit();			
			break;		
		case 5:
			document.IMPI_Form.nextAction.value="delete_associated_IMPU";
		 	document.IMPI_Form.associated_ID.value=associated_ID;
		 	document.IMPI_Form.submit();
			break;
		case 6:
			document.IMPI_Form.nextAction.value="delete_associated_IMSU";
		 	document.IMPI_Form.submit();
			break;
		case 10:
			document.IMPI_Form.nextAction.value="ppr";
			document.IMPI_Form.submit();			
			break;
		case 11:	
			document.IMPI_Form.nextAction.value="rtr_select_identities";
			document.IMPI_Form.submit();			
			break;
		case 12:	
			document.IMPI_Form.nextAction.value="rtr_all";
			document.IMPI_Form.submit();			
			break;
		case 13:	
			document.IMPI_Form.nextAction.value="rtr_selected";
			document.IMPI_Form.submit();			
			break;
		case 14:
			document.IMPI_Form.nextAction.value="add_imsu";
			document.IMPI_Form.submit();			
			break;
		case 15:
			document.IMPI_Form.nextAction.value="add_impu";
			document.IMPI_Form.submit();			
	}

}

function disable_other_boxes(){
	if (document.IMPI_Form.all.checked){
		document.IMPI_Form.aka1.disabled=true;
		document.IMPI_Form.aka2.disabled=true;
		document.IMPI_Form.md5.disabled=true;
		document.IMPI_Form.digest.disabled=true;
		document.IMPI_Form.sip_digest.disabled=true;
		document.IMPI_Form.http_digest.disabled=true;		
		document.IMPI_Form.early.disabled=true;
		document.IMPI_Form.nass_bundle.disabled=true;		
		
	}
	else{
		document.IMPI_Form.aka1.disabled=false;
		document.IMPI_Form.aka2.disabled=false;
		document.IMPI_Form.md5.disabled=false;
		document.IMPI_Form.digest.disabled=false;
		document.IMPI_Form.sip_digest.disabled=false;
		document.IMPI_Form.http_digest.disabled=false;		
		document.IMPI_Form.early.disabled=false;
		document.IMPI_Form.nass_bundle.disabled=false;		
	}
}
</script>

</head>

<body>
	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Private User Identity -IMPI- </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="/IMPI_Submit">
		<html:hidden property="nextAction" value=""/>
		<html:hidden property="associated_ID" value=""/>			
		<html:hidden property="id_imsu" />			
		<html:hidden property="already_assigned_imsu_id" />						

		<table id="main-table" align="center" valign="middle" >
		<tr>
			<td>
		 		<table id="left-side-table">						
				<tr>
					<td>
				 		<table id="impi-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">						
					    <tr bgcolor="#FFCC66">
							<td>ID </td>
							<td>
								<html:text property="id" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> 
							</td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>
								Identity* 
							</td>
							<td>
								<html:text property="identity" styleClass="inputtext" style="width:200px;"/> 
							</td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>
								Secret Key*
							</td>
							<td>
								<html:text property="secretKey" styleClass="inputtext" style="width:200px;"/> 
							</td>
						</tr>
			
						<!-- The Authentication Schemes types -->
						<tr bgcolor="#FFCC66">
							<td> 
								<br/> <b>Authentication Schemes* </b>
							</td>
							<td></td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>
								Digest-AKAv1 (3GPP)
							</td>
							<td>
								<html:checkbox property="aka1" styleClass="inputbox"/> 
							</td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>
								Digest-AKAv2 (3GPP)
							</td>
							<td>
								<html:checkbox property="aka2" styleClass="inputbox"/>
							</td>
						</tr>
						<tr bgcolor="#FFCC66">
							<td>
								Digest-MD5 (FOKUS)
							</td>
							<td>
								<html:checkbox property="md5" styleClass="inputbox"/> 
							</td>
						</tr>

						<tr bgcolor="#FFCC66">
							<td>
								Digest (CableLabs)
							</td>
							<td>
								<html:checkbox property="digest" styleClass="inputbox"/> 
							</td>
						</tr>

						<tr bgcolor="#FFCC66">
							<td>
								SIP Digest (3GPP)
							</td>
							<td>
								<html:checkbox property="sip_digest" styleClass="inputbox"/> 
							</td>
						</tr>
									
						<tr bgcolor="#FFCC66">
							<td>
								HTTP Digest (ETSI)
							</td>
							<td>
								<html:checkbox property="http_digest" styleClass="inputbox"/> 
							</td>
						</tr>
									
						<tr bgcolor="#FFCC66">
							<td>
								Early-IMS (3GPP)
							</td>
							<td>
								<html:checkbox property="early" styleClass="inputbox"/> 
							</td>
						</tr>

						<tr bgcolor="#FFCC66">
							<td>
								NASS Bundled (ETSI)
							</td>
							<td>
								<html:checkbox property="nass_bundle" styleClass="inputbox"/> 
							</td>
							</tr>
									
							<tr bgcolor="#FFCC66">
								<td>
									All
								</td>
								<td>
									<html:checkbox property="all" styleClass="inputbox" onclick="disable_other_boxes();"/> 
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td>
									Default
								</td>
								<td>
									<html:select property="default_auth_scheme" name="IMPI_Form" styleClass="inputtext" size="1" style="width:200px;" > 
										<html:optionsCollection name="IMPI_Form" property="select_auth_scheme" label="name" value="code"/>
									</html:select>
								</td>	
							</tr>
									
							<tr bgcolor="#FFCC66">
								<td> <br/> </td>
								<td> </td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td>
									AMF*
								</td>
								<td>
									<html:text property="amf" styleClass="inputtext" style="width:100px;" /> 
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td>
									OP*
								</td>
								<td>
									<html:text property="op" styleClass="inputtext" style="width:300px;"/> 
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td> 
									SQN*
								</td>
								<td>
									<html:text property="sqn" styleClass="inputtext" style="width:100px;"/>
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td> <br/> </td>
								<td> </td>
							</tr>
									
							<tr bgcolor="#FFCC66">
								<td>
									Early IMS IP
								</td>
								<td>
									<html:text property="ip" styleClass="inputtext" style="width:200px;"/> 
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td>
									DSL Line Identifier
								</td>
								<td>
									<html:text property="line_identifier" styleClass="inputtext" style="width:200px;"/> 
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td>
									GUSS
								</td>
								<td>
								<%
									if (id != -1){
								%>
									<a href="/hss.web.console/GBA_USS_Load.do?id_impi=<%=id%>">
										Configure 	
									</a>
								<%
									}
								%>	
								</td>
							</tr>
							</table> <!-- impi-table-->		
						</td>
					</tr>
					<tr>
						<td align="center"> 
							<b> Mandatory fields were marked with "*".<br>
							The Secret Key in this form is considered in hex representation if its value is 16 bytes long or
							else in ASCII representation.</b>
						</td>
					</tr>	
					<tr>
						<td align=center> 
							<br />
						
							<%
								if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
							%>
									<html:button property="save_button" value="Save" onclick="add_action_for_form(1, -1);"/>				
							<%
								}
							%>	
							<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(3, -1);"/> 
							<% 
								if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id == -1){ 
							%>
									<html:button property="reset_button" value="Reset" onclick="add_action_for_form(2, -1);"/> 
							<%
								}
							%>
					
							<% 
								if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id != -1){ 
							%>
									<html:button property="delete_button" value="Delete" onclick="add_action_for_form(4, -1);" 
										disabled="<%=Boolean.parseBoolean((String)request.getAttribute("deleteDeactivation")) %>"/>				
							<%
								}
							%>												
						</td>
					</tr>
					</table> <!-- left-side-table -->
				</td>
			
				<%
					if (id != -1){
				%>	
				<td>
					<table id="right-side-table" align="center">										
					<%
						if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
					%>					
					<tr>					
						<td>
							<br />
							<h2> Associate an IMSU </h2>				
							<table id="associate-imsu-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
							<tr class="even">
								<td>
									IMSU Identity
								</td>
								<td>
									<html:text property="imsu_name" value="" styleClass="inputtext" />
								</td>
								<td>
									<html:button property="imsu_add_button" value="Add/Change" onclick="add_action_for_form(14, -1);"/>
								</td>
							</tr>	
							</table> <!-- associate-imsu-table -->
						</td>
					</tr>
					<%	
						}
					%>	
					<tr>
						<td>
							<br />
							<h2> Associated IMSU </h2>
							<table id="list-associate-imsu-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
								<tr class="header">
									<td class="header"> ID </td>			
									<td class="header"> IMSU Identity </td>
									<%
										if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
									%>
									<td class="header"> Delete </td>
									<%
										}
									%>
								</tr>
								<%
									if (associated_IMSU != null && associated_IMSU.getId() > 0){
								%>
										<tr class="even">																			
											<td>  <%= associated_IMSU.getId() %></td>
											<td>  
												<a href="/hss.web.console/IMSU_Load.do?id=<%= associated_IMSU.getId() %>" > 
													<%= associated_IMSU.getName() %>
												</a>	
											</td>
											<%
												if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
											%>
											<td> 
												<input type="button" name="delete_associated_imsu" 
													"value="Delete" onclick="add_action_for_form(6, <%= associated_IMSU.getId() %>);"/>													
											</td>
											<%
												}
											%>
										</tr>											
								<%			
									}
								%>
							</table> <!-- list-associate-imsu-table -->
						</td>
					</tr>		

					<%
						if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
					%>
					<tr>
						<td>
							<br />
							<h2>
								<b>Create & Bind new IMPU </b>
								<%
									out.println("<a href=\"/hss.web.console/IMPU_Load.do?id=-1&already_assigned_impi_id=" + id + "\" > ");
								%>
									<img src="/hss.web.console/images/add_obj.gif" /> 
								</a>
							</h2>		
						</td>
					</tr>	

					<tr>
						<td>
							<br/>
							<h2>Associate IMPU(s) </h2>
							<table id="associate-impu-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
							<tr class="even">
								<td>
									IMPU Identity
								</td>								
								<td>
									<html:text property="impu_identity" value="" styleClass="inputtext" />
								</td>
								<td>
									<html:button property="impu_add_button" value="Add" onclick="add_action_for_form(15);"/>
								</td>
							</tr>	
							</table> <!-- associate-impu-table -->
						</td>
					</tr>						
					<tr>
						<td>
							<table id="warning-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
							<tr>
								<td>
									<font color="#FF0000">
										Warning: The current IMPI will be associated with all the corresponding IMPUs (within the same implicit-set)!
									</font>						
								</td>
							</tr>									
							</table>
						</td>
					</tr>
					<%
						} // endif ADMIN
					%>
					<tr>
						<td>
							<br />
							<h2> List of associated IMPUs </h2>							
							<table id="list-associate-impu-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
								<tr class="header">
									<td class="header"> ID: </td>			
									<td class="header"> IMPU Identity: </td>
									<%
										if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
									%>									
									<td class="header"> Delete: </td>
									<%
										}
									%>
								</tr>
								<%
									if (associated_IMPUs != null){
										Iterator it = associated_IMPUs.iterator();
										IMPU impu = null;
										int idx = 0;
										while (it.hasNext()){
											impu = (IMPU) it.next();
												
								%>
											<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">																			
												<td>  <%= impu.getId() %></td>												
												
												<td>  
													<a href="/hss.web.console/IMPU_Load.do?id=<%= impu.getId() %>" > 
														<%= impu.getIdentity() %>
													</a>	
												</td>
												<%
													if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
												%>												
												<td> 
													<input type="button" name="delete_associated_impu" 
														"value="Delete" onclick="add_action_for_form(5, <%= impu.getId() %>);"/>													
												</td>
												<%
													}
												%>
											</tr>											
								<%			
											idx++;												
										}
									}
								%>
							</table> <!-- list-associate-impu-table -->
						</td>
					</tr>		

					<%
						if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
					%>
					<tr>
						<td>
							<br />
							<h2> Push Cx Operation </h2>
							<table class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
							<tr bgcolor="#FFCC66">
								<td>
									Apply for
								</td>
								<td align="center">
									<html:select property="ppr_apply_for" name="IMPI_Form" styleClass="inputtext" size="1" style="width:200px;">
										<html:optionsCollection name="IMPI_Form" property="select_ppr_apply_for" label="name" value="code"/>
									</html:select>
								</td>
							</tr>
							<tr bgcolor="#FFCC66">
								<td align="center">
									Execute
								</td>
															
								<td align="center">									
									<html:button property="ppr_button" value="PPR" onclick="add_action_for_form(10, -1);"/>
								</td>
							</tr>
							</table>
						</td>
					</tr>
					<tr>	
						<td>
							<br />
							<h2> RTR Operation </h2>
							<table class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">

							<tr bgcolor="#FFCC66">
								<td>Apply for</td>
								<td>
									<html:select property="rtr_apply_for" name="IMPI_Form" styleClass="inputtext" size="1" style="width:200px;"
										onchange="add_action_for_form(3, -1);" >
										<html:optionsCollection name="IMPI_Form" property="select_rtr_apply_for" label="name" value="code"/>
									</html:select>
								</td>
							</tr>
							
							<tr bgcolor="#FFCC66">
								<td>Select Identities</td>
								<td><html:select multiple="true" size="5" property="rtr_identities">
							    	<html:optionsCollection property="rtr_select_identities" label="identity" value="id"/>
							   	</html:select></td>
							</tr>
							
							<tr bgcolor="#FFCC66">
								<td>Reason</td>
								<td>
									<html:select property="rtr_reason" name="IMPI_Form" styleClass="inputtext" size="1" style="width:200px;">
										<html:option value="-1">Select Reason...</html:option>
										<html:optionsCollection name="IMPI_Form" property="select_rtr_reason" label="name" value="code"/>
									</html:select>
								</td>
								
							</tr>

							<tr bgcolor="#FFCC66">
								<td>Reason Info</td>
								<td>
									<html:text property="reasonInfo" styleClass="inputtext" style="width:200px;"/> 
								</td>
							</tr>

							<tr bgcolor="#FFCC66">
							
								<td align="center" >
									Execute
								</td>
								
								<td align="center" >									
									<html:button property="rtr_button" value="RTR-All" onclick="add_action_for_form(12, -1);"/>
									<html:button property="rtr_button" value="RTR-Selected" onclick="add_action_for_form(13, -1);"/>
								</td>
							</tr>	
							</table>				
						</td>
					</tr>
					<%
						} //endif ADMIN
					%>
					</table>
				</td>
				<%
					}
				%>
			</tr>					     		
			</table>	
		</html:form>					
</body>
</html>
