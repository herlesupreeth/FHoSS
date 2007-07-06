<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ page import="java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.web.util.WebConstants  " %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>IMPU </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">

<jsp:useBean id="associated_IMPIs" class="java.util.ArrayList" scope="request"></jsp:useBean>
<jsp:useBean id="implicitset_IMPUs" class="java.util.ArrayList" scope="request"></jsp:useBean>
<jsp:useBean id="visitedNetworks" class="java.util.ArrayList" scope="request"></jsp:useBean>

<%
	int id = Integer.parseInt(request.getParameter("id"));
%>

<script type="text/javascript" language="JavaScript">

function add_action_for_form(action, associated_ID) {
	switch(action){
		case 1:
			document.IMPU_Form.nextAction.value="save";
			document.IMPU_Form.submit();
			break;
		case 2:
			document.IMPU_Form.nextAction.value="reset";
			document.IMPU_Form.reset();
			break;
		case 3:
			document.IMPU_Form.nextAction.value="refresh";
			document.IMPU_Form.submit();			
			break;
		case 4:
			document.IMPU_Form.nextAction.value="delete";
			document.IMPU_Form.submit();			
			break;
		case 5:
			document.IMPU_Form.nextAction.value="delete_associated_impi";
			document.IMPU_Form.associated_ID.value = associated_ID;
			document.IMPU_Form.submit();			
			break;
		case 6:
			document.IMPU_Form.nextAction.value="delete_impu_from_implicitset";
			document.IMPU_Form.associated_ID.value = associated_ID;			
			document.IMPU_Form.submit();			
			break;
		case 7:
			document.IMPU_Form.nextAction.value="delete_visited_network";
			document.IMPU_Form.associated_ID.value = associated_ID;			
			document.IMPU_Form.submit();			
			break;
								
		case 10:
			document.IMPU_Form.nextAction.value="ppr";
			document.IMPU_Form.submit();			
			break;
			
		case 12:
			document.IMPU_Form.nextAction.value="add_impi";
			document.IMPU_Form.submit();			
			break;
		case 13:
			document.IMPU_Form.nextAction.value="add_impu_to_implicitset";
			document.IMPU_Form.submit();			
			break;
		case 14:
			document.IMPU_Form.nextAction.value="add_vn";
			document.IMPU_Form.submit();			
			break;
	}
}
</script>
</head>

<body>

	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Public User Identity -IMPU- </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->

	<html:form action="/IMPU_Submit">
		<html:hidden property="nextAction" value=""/>
		<html:hidden property="associated_ID" />
		<html:hidden property="already_assigned_impi_id" />			

		<table id="main-table" align="center" valign="middle">
			<tr>
				<td>
		 			<table id="left-side-table" border="0" align="center" width="400" >						
		 			<tr>
 						<td>
			 				<table id="impu-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">										
							<tr bgcolor="#FFCC66">
								<td> ID </td>
								<td><html:text property="id" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> </td>
							</tr>
		
							<tr bgcolor="#FFCC66">
								<td>Identity* </td>
								<td><html:text property="identity" styleClass="inputtext" style="width:250px;"/> </td>
							</tr>
							
							<tr bgcolor="#FFCC66">
								<td>Barring</td>
								<td><html:checkbox property="barring" styleClass="inputbox" /></td>
							</tr>
							
							<tr bgcolor="#FFCC66">
								<td>Service Profile*</td>
								<td>
									<html:select property="id_sp" name="IMPU_Form" styleClass="inputtext" size="1" style="width:250px;">
										<html:option value="-1">Select Profile...</html:option>
										<html:optionsCollection name="IMPU_Form" property="select_sp" label="name" value="id"/>
									</html:select>
								</td>	
							</tr>
							
							<tr bgcolor="#FFCC66">
								<td>Implicit Set</td>
								<td>
									<html:text property="id_impu_implicitset" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/>
								</td>
							</tr>			
							
							<tr bgcolor="#FFCC66">
								<td>Charging-Info Set</td>
								<td>
									<html:select property="id_charging_info" name="IMPU_Form" styleClass="inputtext" size="1" style="width:250px;">
										<html:option value="-1">Select Charging-Info...</html:option>
										<html:optionsCollection name="IMPU_Form" property="select_charging_info" label="name" value="id"/>
									</html:select>	
								</td>
							</tr>
							
							<tr bgcolor="#FFCC66">
								<td> Can Register </td>
								<td><html:checkbox property="can_register" styleClass="inputbox"/> </td>
							</tr>			

							<tr bgcolor="#FFCC66">
								<td> IMPU Type* </td>
								<td>
									<html:select property="type" name="IMPU_Form" styleClass="inputtext" size="1" style="width:250px;" > 
										<html:option value="-1">Select type...</html:option>
										<html:optionsCollection name="IMPU_Form" property="select_identity_type" label="name" value="code"/>
									</html:select>
								</td>	
							</tr>			
			
							<tr bgcolor="#FFCC66">
								<td> Wildcard PSI </td>
								<td><html:text property="wildcard_psi" styleClass="inputtext" style="width:250px;"/> </td>				
							</tr>			
			
							<tr bgcolor="#FFCC66">
								<td> PSI Activation </td>
								<td><html:checkbox property="psi_activation" styleClass="inputbox"/> </td>				
							</tr>			
			
							<tr bgcolor="#FFCC66">
								<td> Display Name </td>
								<td><html:text property="display_name" styleClass="inputtext" styleClass="inputtext" style="width:250px;"/> </td>							
							</tr>			
			
							<tr bgcolor="#FFCC66">
								<td> User-Status </td>
								<html:hidden property="user_state" />

								<logic:equal value="0" property="user_state" name="IMPU_Form">
									<td bgcolor="#FF6666"> NOT-REGISTERED </td>
								</logic:equal>

								<logic:equal value="1" property="user_state" name="IMPU_Form">
									<td bgcolor="#33CC66"> REGISTERED </td>
								</logic:equal>
				
								<logic:equal value="2" property="user_state" name="IMPU_Form">
									<td bgcolor="#33CCFF"> UN-REGISTERED </td>
								</logic:equal>
				
								<logic:equal value="3" property="user_state" name="IMPU_Form">
									<td bgcolor="#FFCC66"> AUTH-PENDING </td>
								</logic:equal>
							</tr>
							</table> <!-- impu-table -->		
					</td>
				</tr>
				<tr>
					<td align="center"> 
					<b> Mandatory fields were marked with "*" </b>
					</td>
				</tr>	
				<tr>
					<td align="center">
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
			<%
				if (id != -1){
			%>			
				<%
					 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
				%>					
				<tr>
					<td>
						<br/>
						<h2>Add IMPU(s) to Implicit-Set </h2>
				 		<table id="implicit-set-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">										
						<tr class="even">
							<td>
								IMPU Identity
							</td>
							<td>
								<html:text property="impu_implicitset_identity" value="" styleClass="inputtext" />
							</td>
							<td>
								<html:button property="add_impu_to_implicitset" value="Add" onclick="add_action_for_form(13, -1);"/>
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
						<br/>
						<h2>List IMPUs from Implicit-Set </h2>
						<table id="list-implicit-set-table" width="400" class="as" border="0" cellspacing="1" align="center" width="100%" style="border:2px solid #FF6600;">	
						<tr class="header">
							<td class="header"> ID </td>			
							<td class="header"> IMPU Identity </td>
							<%
								 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
							%>												
							<td class="header"> Delete </td>
							<%
								}
							%>
						</tr>
						<%
							if (implicitset_IMPUs != null && implicitset_IMPUs.size() > 0){
								Iterator it = implicitset_IMPUs.iterator();
								IMPU crt_IMPU = null;
								
								while (it.hasNext()){									
									crt_IMPU = (IMPU) it.next();
						%>
									<tr class="even">																			
										<td>  <%= crt_IMPU.getId() %></td>
										
										<td>  
											<a href="/hss.web.console/IMPU_Load.do?id=<%= crt_IMPU.getId() %>" > 
												<%= crt_IMPU.getIdentity() %>
											</a>	
										</td>
											
						<%							
								 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
										if (crt_IMPU.getId() != id){
						%>				
											<td> 
												<input type="button" name="delete_impu_from_implicitset" 
													"value="Delete" onclick="add_action_for_form(6, <%= crt_IMPU.getId() %>);" />													
											</td>
						<%
										} else {
						%>				
											<td> </td> 
						<%
										}
								}
						%>											
									</tr>											
						<%			
								}
							}
						%>
						</table> <!-- list-implicit-set-table -->		  
					</td>
				</tr>
			</table> <!-- left-side-table -->		
		</td>	
		
		<td>	
			<table id="right-side-table" align="center" valign="middle">										
			<%
				 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
			%>								
			<tr>
				<td>
					<h2>Add Visited-Networks </h2>
					<table id="visited-networks-table" width="400" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">	
					<tr class="even">
						<td>
							<html:select property="vn_id" name="IMPU_Form" styleClass="inputtext" size="1" style="width:250px;">
								<html:option value="-1">Select Visited-Network...</html:option>
								<html:optionsCollection name="IMPU_Form" property="select_vn" label="identity" value="id"/>
							</html:select>	
						</td>
						
						<td>
							<html:button property="add_vn" value="Add" onclick="add_action_for_form(14, -1);"/>
						</td>
					</tr>	
					</table> <!-- visited-networks-table -->
				</td>
			</tr>
			<%
				} //endif ADMIN
			%>	
			<tr>
				<td>
					<br />
					<h2> List of Visited Networks </h2>
					<table id="list-visited-networks-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
					<tr class="header">
						<td class="header"> ID </td>			
						<td class="header"> Identity </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>											
						<td class="header"> Delete </td>
						<%
							}
						%>
					</tr>
					<%
						if (visitedNetworks != null && visitedNetworks.size() > 0){
							Iterator it = visitedNetworks.iterator();
							VisitedNetwork crt_VisitedNetwork = null;
										
							while (it.hasNext()){									
								crt_VisitedNetwork = (VisitedNetwork) it.next();
					%>
								<tr class="even">																			
									<td>  <%= crt_VisitedNetwork.getId() %></td>

									<td> 
								    	<a href="/hss.web.console/VN_Load.do?id=<%= crt_VisitedNetwork.getId() %>" > 
											 <%= crt_VisitedNetwork.getIdentity() %></td>
										</a>
									</td>	
									<%
										 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
									%>														
									<td> 
										<input type="button" name="delete_visited_network" 
											"value="Delete" onclick="add_action_for_form(7, <%= crt_VisitedNetwork.getId() %>);"/>													
									</td>
									<%
										}
									%>
								</tr>											
					<%			
							}
						}
					%>
					</table> <!-- list-visited-networks-table -->
				</td>
			</tr>		
			<%
				 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
			%>								
			<tr>
				<td>
					<br />
					<h2> Associate IMPI(s) to IMPU
					<table id="associate-impi-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
					<tr class="even">																			
						<td>
							IMPI Identity
						</td>	
						<td>
							<html:text property="impi_identity" value="" styleClass="inputtext" />
						</td>
						<td>
							<html:button property="add_impi" value="Add" onclick="add_action_for_form(12, -1);"/>
						</td>
					</tr>	
					</table> <!-- associate-impi-table -->
				</td>
			</tr>

			<tr>
				<td>
					<br />
					<table id="warning-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">				
					<tr>
						<td>
							<font color="#FF0000">
								Warning: This IMPI will be associated with all the corresponding IMPUs (within the same implicit-set)!
							</font>						
						</td>
					</tr>
					</table> <!-- warning-table -->		
				</td>
			</tr>
			<%	
				}
			%>	
			
			<tr>
				<td>
					<br />
					<h2> List of associated IMPIs </h2>
					<table id="list-associate-impi-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">									
					<tr class="header">
						<td class="header"> ID </td>			
						<td class="header"> IMPI Identity </td>
						<%
							 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
						%>														
						<td class="header"> Delete </td>
						<%
							}
						%>
					</tr>
					<%
						if (associated_IMPIs != null && associated_IMPIs.size() > 0){
							Iterator it = associated_IMPIs.iterator();
							IMPI crt_IMPI = null;
										
							while (it.hasNext()){									
								crt_IMPI = (IMPI) it.next();
					%>
								<tr class="even">																			
									<td>  <%= crt_IMPI.getId() %></td>
									<td>
								    	<a href="/hss.web.console/IMPI_Load.do?id=<%= crt_IMPI.getId() %>" > 
  											<%= crt_IMPI.getIdentity() %>
  										</a>  
									</td>

									<%
										 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
									%>								
									<td> 
										<input type="button" name="delete_associated_impi" 
											"value="Delete" onclick="add_action_for_form(5, <%= crt_IMPI.getId() %>);"/>													
									</td>
									<%
										}
									%>
								</tr>											
					<%			
							}
						}
					%>
					</table> <!-- list-associate-impi-table -->
				</td>
			</tr>		

			<%
				 if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){
			%>								
			<tr>
				<td>
					<br />
					<b> Push Cx Operation </b>
					<table id="ppr-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
					<tr bgcolor="#FFCC66">
						<td>
							Apply for
						</td>
						<td align="center">
							<html:select property="ppr_apply_for" name="IMPU_Form" styleClass="inputtext" size="1" style="width:200px;">
								<html:optionsCollection name="IMPU_Form" property="select_ppr_apply_for" label="name" value="code"/>
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
					</table> <!-- ppr-table -->
				</td>
			</tr>
			<%
				} // endif ADMIN
			%>
			</table>	<!-- right-side-table -->					
		</td>
	</tr>
	<%
		} // endif id != -1
	%>
	</table>		
	</html:form> <!-- main-table -->
</body>
</html>
