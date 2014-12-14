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
<title> AS </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">
<jsp:useBean id="attached_ifc_list" type="java.util.List" scope="request"></jsp:useBean>

<%
	int id = Integer.parseInt(request.getParameter("id"));
%>

<script type="text/javascript" language="JavaScript">
function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.AS_Form.nextAction.value="save";
			document.AS_Form.submit();
			break;
		case 2:
			document.AS_Form.nextAction.value="refresh";
			document.AS_Form.submit();			
			break;
		case 3:
			document.AS_Form.nextAction.value="reset";
			document.AS_Form.reset();
			break;
		case 4:
			document.AS_Form.nextAction.value="delete";
			document.AS_Form.submit();
			break;
		case 5:
			document.AS_Form.nextAction.value="detach_ifc";
			document.AS_Form.associated_ID.value = associated_ID;			
			document.AS_Form.submit();			
			break;
		case 12:
			document.AS_Form.nextAction.value="attach_ifc";
			document.AS_Form.submit();			
			break;
	}
}
</script>
</head>

<body>
	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Application Server -AS- </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

<html:form action="/AS_Submit">
	<html:hidden property="nextAction" value=""/>
	<html:hidden property="associated_ID" value=""/>
		
	<table id="main-table" align="center" valign="middle">
	<tr>
		<td>
	 		<table id="top-side-table" border="0" align="center">						
 			<tr>
 				<td>
					<table id="as-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">						
					<tr bgcolor="#FFCC66">
						<td> ID </td>
						<td><html:text property="id" readonly="true" styleClass="inputtext_readonly" style="width:200px;"/> </td>
					</tr>
					<tr bgcolor="#FFCC66">
						<td>Name* </td>
						<td><html:text property="name" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>

					<tr bgcolor="#FFCC66">
						<td>Server Name* </td>
						<td><html:text property="server_name" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>
					
					<tr bgcolor="#FFCC66">
						<td>Diameter FQDN*</td>
						<td><html:text property="diameter_address" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>

					<tr bgcolor="#FFCC66">
						<td>Default Handling*</td>
						<td>
							<html:select property="default_handling" name="AS_Form" styleClass="inputtext" size="1" style="width:200px;">
								<html:optionsCollection name="AS_Form" property="select_default_handling" label="name" value="code"/>
							</html:select>
						</td>
					</tr>
					<tr bgcolor="#FFCC66">
						<td>Service Info</td>
						<td><html:text property="service_info" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>

					<tr bgcolor="#FFCC66">
						<td> Rep-Data Limit </td>				
						<td><html:text property="rep_data_size_limit" styleClass="inputtext" style="width:100px;"/> </td>
					</tr>
					<tr bgcolor="#FFCC66">
						<td>Include original REGISTER request?</td>				
						<td><html:checkbox property="include_register_request" styleClass="inputbox" /> </td>										
					</tr>					
					<tr bgcolor="#FFCC66">
						<td>Include REGISTER Response?</td>				
						<td><html:checkbox property="include_register_response" styleClass="inputbox" /> </td>										
					</tr>
					</table> <!-- fields-table-->		
				</td>
				<td>
					<b>Sh Interface - Permissions</b>	
					<table id="sec-perm" class="as" border="0" cellspacing="1" width="200" align="center" style="border:2px solid #FF6600;">								
					<tr class="header">
						<td width="60%"> Permission for </td>
						<td class="header"> UDR </td>
						<td class="header"> PUR </td>
						<td class="header"> SNR </td>
					</tr>

					<tr bgcolor="#FFCC66">
						<td> Allowed Request </td>				
						<td> <html:checkbox property="udr" styleClass="inputbox" />	 </td>										
						<td> <html:checkbox property="pur" styleClass="inputbox" />	 </td>										
						<td> <html:checkbox property="snr" styleClass="inputbox" />	 </td>										
					</tr>
					
					<tr bgcolor="#FFCC66">
						<td> Repository-Data </td>				
						<td><html:checkbox property="udr_rep_data" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="pur_rep_data" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="snr_rep_data" styleClass="inputbox" />	</td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> IMPU </td>				
						<td><html:checkbox property="udr_impu" styleClass="inputbox" />	</td>										
						<td></td>										
						<td><html:checkbox property="snr_impu" styleClass="inputbox" />	</td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> IMS User State </td>				
						<td><html:checkbox property="udr_ims_user_state" styleClass="inputbox" />	</td>										
						<td> </td>										
						<td><html:checkbox property="snr_ims_user_state" styleClass="inputbox" />	</td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> S-CSCF Name </td>				
						<td><html:checkbox property="udr_scscf_name" styleClass="inputbox" />	</td>										
						<td></td>										
						<td><html:checkbox property="snr_scscf_name" styleClass="inputbox" />	</td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> iFC </td>				
						<td><html:checkbox property="udr_ifc" styleClass="inputbox" />	</td>										
						<td></td>										
						<td><html:checkbox property="snr_ifc" styleClass="inputbox" />	</td>										
					</tr>
					
					<tr bgcolor="#FFCC66">
						<td> Location </td>				
						<td><html:checkbox property="udr_location" styleClass="inputbox" />	</td>										
						<td></td>										
						<td></td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> User-State </td>				
						<td><html:checkbox property="udr_user_state" styleClass="inputbox" /> </td>										
						<td></td>										
						<td></td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> Charging-Info </td>				
						<td><html:checkbox property="udr_charging_info" styleClass="inputbox" /> </td>										
						<td></td>										
						<td></td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> MS-ISDN </td>				
						<td><html:checkbox property="udr_msisdn" styleClass="inputbox" /> </td>										
						<td></td>										
						<td></td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> PSI Activation </td>				
						<td><html:checkbox property="udr_psi_activation" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="pur_psi_activation" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="snr_psi_activation" styleClass="inputbox" />	</td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> DSAI </td>				
						<td><html:checkbox property="udr_dsai" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="pur_dsai" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="snr_dsai" styleClass="inputbox" />	</td>										
					</tr>
					<tr bgcolor="#FFCC66">
						<td> Aliases Rep Data </td>				
						<td><html:checkbox property="udr_aliases_rep_data" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="pur_aliases_rep_data" styleClass="inputbox" />	</td>										
						<td><html:checkbox property="snr_aliases_rep_data" styleClass="inputbox" />	</td>										
					</tr>
					</table> <!-- sec-perm-table -->		
				</td>					
			</tr>		
			</table> <!-- top-side-table-->		
		</td>
	</tr>	
	<tr>
		<td>
			<table id="buttons-table" align="center">			
			<tr>
				<td align="center"> 
				<b> Mandatory fields were marked with "*" </b>
				</td>
			</tr>	
 			<tr>
				<td align=center> <br/>
					<%
						 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
					%>				
					<html:button property="save_button" value="Save" onclick="add_action_for_form(1);"/>				
					<%
						}
					%>
					<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(2);"/> 
					<% if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id == -1){ %>
						<html:button property="reset_button" value="Reset" onclick="add_action_for_form(3);"/> 
					<%}%>
					<% if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id != -1){ %>
						<html:button property="delete_button" value="Delete" onclick="add_action_for_form(4);" 
							disabled="<%=Boolean.parseBoolean((String)request.getAttribute("deleteDeactivation")) %>"/>				
					<%}%>									
				</td>
			</tr>
			</table> <!-- buttons-table -->
		</td>
	</tr>
	<%
		if (id != -1){
	%>
	<tr>
		<td>
			<table id="bottom-side-table" class="as" align="center">
			<%
				 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
			%>							
			<tr>
				<td>
					<br />
					<h2>Attach IFC(s) </h2>
					<table id="ifc-table" class="as" border="0" width="400" cellspacing="1" align="center" style="border:2px solid #FF6600;">
					<tr class="even">
						<td>
							<html:select property="ifc_id" value="-1" name="AS_Form" styleClass="inputtext" size="1" style="width:250px;">
								<html:option value="-1">Select IFC...</html:option>
								<html:optionsCollection name="AS_Form" property="select_ifc" label="name" value="id"/>
							</html:select>	
						</td>

						<td>
							<html:button property="ifc_attach_button" value="Attach" onclick="add_action_for_form(12, -1);"/>
							<br />
						</td>
					</tr>	
					</table> <!-- ifc-table -->
					
				</td>
			</tr>
			<%
				} //endif ADMIN
			%>
			<tr>
				<td>
					<br />		
					<h2> List of attached IFCs </h2>
					<table id="list-ifc-table" class="as" width="400" border="0" cellspacing="1" align="center" width="100%" style="border:2px solid #FF6600;">
					<tr class="header">
						<td class="header"> ID </td>			
						<td class="header"> IFC Name </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>										
						<td class="header"> Detach </td>
						<%
							}
						%>
					</tr>
					<%
						if (attached_ifc_list != null){
							Iterator it = attached_ifc_list.iterator();
							IFC ifc = null;
							int idx = 0;
							while (it.hasNext()){
								ifc = (IFC) it.next();
												
					%>
							<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">																			
								<td>  <%= ifc.getId() %></td>												
										
								<td>  
									<a href="/hss.web.console/IFC_Load.do?id=<%= ifc.getId() %>" > 
										<%= ifc.getName() %>
									</a>	
								</td>
								
								<%
									 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
								%>				
								<td> 
									<input type="button" name="detach_ifc" 
										"value="Detach" onclick="add_action_for_form(5, <%= ifc.getId() %>);"/>													
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
					</table> <!-- list-ifc-table -->
				</td>
			</tr> <!-- bottom-side-table -->
		</td>
	</tr>
	<%
		}
	%>				
	</table><!-- main-table -->		
</html:form>
</body>
</html>
