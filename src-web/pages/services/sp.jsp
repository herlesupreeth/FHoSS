<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
	
<%@ page import="java.util.*, de.fhg.fokus.hss.db.model.*, de.fhg.fokus.hss.db.op.*, 
	de.fhg.fokus.hss.db.hibernate.*, org.hibernate.Session, de.fhg.fokus.hss.web.util.WebConstants " %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title> SP </title>
<link rel="stylesheet" type="text/css" href="/hss.web.console/style/fokus_ngni.css">
<jsp:useBean id="attached_ifc_list" type="java.util.List" scope="request"></jsp:useBean>
<jsp:useBean id="attached_shared_ifc_list" type="java.util.List" scope="request"></jsp:useBean>

<%
	int id = Integer.parseInt(request.getParameter("id"));
	Session hibSession = HibernateUtil.getCurrentSession();
	HibernateUtil.beginTransaction();
	
%>

<script type="text/javascript" language="JavaScript">

function add_action_for_form(action, associated_ID) {

	switch(action){
		case 1:
			document.SP_Form.nextAction.value="save";
			document.SP_Form.submit();
			break;
		case 2:
			document.SP_Form.nextAction.value="refresh";
			document.SP_Form.submit();			
			break;
		case 3:
			document.SP_Form.nextAction.value="reset";
			document.SP_Form.reset();
			break;
		case 4:
			document.SP_Form.nextAction.value="delete";
			document.SP_Form.submit();			
			break;
		case 5:
			document.SP_Form.nextAction.value="detach_ifc";
			document.SP_Form.associated_ID.value = associated_ID;			
			document.SP_Form.submit();			
			break;
		case 6:
			document.SP_Form.nextAction.value="detach_shared_ifc";
			document.SP_Form.associated_ID.value = associated_ID;						
			document.SP_Form.submit();			
			break;

		case 12:
			document.SP_Form.nextAction.value="attach_ifc";
			document.SP_Form.submit();			
			break;

		case 13:
			document.SP_Form.nextAction.value="attach_shared_ifc";
			document.SP_Form.submit();			
			break;			
	}
}

</script>
</head>

<body>

	<table id="title-table" align="center" weight="100%" >
	<tr>
		<td align="center">
			<h1> Service Profile -SP- </h1> 			
			<br/><br/>			
		</td>
	<tr>	
		<td align="left">
			<!-- Print errors, if any -->
			<jsp:include page="/pages/tiles/error.jsp"></jsp:include>
		</td>
	</tr>
	</table> <!-- title-table -->



<html:form action="/SP_Submit">
	<html:hidden property="nextAction" value=""/>
	<html:hidden property="associated_ID" value=""/>
		
	<table id="main-table" align="center" valign="middle">
	<tr>
		<td>
	 		<table id="top-side-table" border="0" align="center" width="400" >						
 			<tr>
 				<td>
					<table id="fields-table" class="as" border="0" cellspacing="1" align="center" width="100%" style="border:2px solid #FF6600;">						
					<tr bgcolor="#FFCC66">
						<td> ID </td>
						<td><html:text property="id" readonly="true" styleClass="inputtext_readonly" style="width:100px;"/> </td>
					</tr>
					<tr bgcolor="#FFCC66">
						<td>Name* </td>
						<td><html:text property="name" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>
					<tr bgcolor="#FFCC66">
						<td>Core Network Service Auth </td>
						<td><html:text property="cn_service_auth" styleClass="inputtext" style="width:200px;"/> </td>
					</tr>
					</table> <!-- fields-table -->
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
							<html:button property="save_button" value="Save" onclick="add_action_for_form(1, -1);"/>				
							<%
								}
							%>
							<html:button property="refresh_button" value="Refresh" onclick="add_action_for_form(2, -1);"/> 
							<% 
								if (request.isUserInRole(WebConstants.Security_Permission_ADMIN) && id == -1){ 
							%>
								<html:button property="reset_button" value="Reset" onclick="add_action_for_form(3, -1);"/> 
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
					</table> <!-- buttons-table -->			
				</td>
			</tr>
			</table> <!-- top-side-table -->		
		</td>
	</tr>
	<%
		if (id != -1){
	%>
	<tr>
		<td>
			<table id="bottom-side-table">
			<tr>
				<td>
					<table id="bottom-left-table">
					<% 
						if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){ 
					%>					
					<tr>
						<td>
							<h2>Attach IFC </h2>
							<table id="attach-ifc-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
							<tr class="even">
								<td>
									<html:select property="ifc_id" value="-1" name="SP_Form" styleClass="inputtext" size="1" style="width:150px;">
										<html:option value="-1">Select IFC...</html:option>
										<html:optionsCollection name="SP_Form" property="select_ifc" label="name" value="id"/>
									</html:select>	
								</td>

								<td>	
									<b> Priority </b>
								</td>

								<td>	
									<html:text property="sp_ifc_priority" value="0" styleClass="inputtext" style="width:50px;" />
								</td>
						
								<td>
									<html:button property="ifc_attach_button" value="Attach" onclick="add_action_for_form(12, -1);"/>
								</td>
							</tr>	
							</table> <!--attach-ifc-table -->
						</td>
					</tr>
					<%
						} //endif ADMIN
					%>
					<tr>
						<td>
							<br />		
							<h2> List of attached IFCs</h2>
							<table id="list-attached-ifc-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
							<tr class="header">
								<td class="header"> ID </td>			
								<td class="header"> IFC Name </td>
								<td class="header"> Priority </td>
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

										<td>  
											<%
												SP_IFC sp_ifc = SP_IFC_DAO.get_by_SP_and_IFC_ID(hibSession, id, ifc.getId());
												if (sp_ifc != null){
													out.println(sp_ifc.getPriority());
												}
												else{
													out.println("Error, SP_IFC is NULL!");
												}
											%>
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
							</table> <!-- list-attached-ifc-table -->
						</td>
					</tr>					
					</table> <!-- bottom-left-table-->
					</td>
					
					<td>
						<table id="bottom-right-table">
						<% 
							if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){ 
						%>						
						<tr>
							<td>	
								<h2>Attach Shared-IFC-Set </h2>
								<table id="shared-ifc-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
								<tr class="even">
									<td>
										<html:select property="shared_ifc_id" value="-1" name="SP_Form" styleClass="inputtext" size="1" style="width:150px;">
											<html:option value="-1">Select Shared-iFC...</html:option>
											<html:optionsCollection name="SP_Form" property="select_shared_ifc" label="name" value="id_set"/>
										</html:select>	
									</td>	
									<td>
										<html:button property="ifc_attach_button" value="Attach" onclick="add_action_for_form(13, -1);"/>
									</td>
								</tr>	
								</table> <!-- shared-ifc-table -->
							</td>
						</tr>
						<%
							}
						%>
						<tr>
							<td>		
								<br />
								<h2> List of attached Shared-IFC-Sets </h2>
								<table id="list-shared-ifc-table" class="as" border="0" cellspacing="1" align="center" width="400" style="border:2px solid #FF6600;">
								<tr class="header">
									<td class="header"> ID-Set </td>			
									<td class="header"> Name </td>
									<% 
										if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){ 
									%>									
									<td class="header"> Detach </td>
									<%
										}
									%>
								</tr>
								<%
									if (attached_shared_ifc_list != null){
										Iterator it = attached_shared_ifc_list.iterator();
										Shared_IFC_Set shared_ifc_set = null;
										int idx = 0;
										while (it.hasNext()){
											shared_ifc_set = (Shared_IFC_Set) it.next();
												
								%>
										<tr class="<%= idx % 2 == 0 ? "even" : "odd" %>">																			
											<td>  <%= shared_ifc_set.getId_set() %></td>																								
											<td>  
												<a href="/hss.web.console/S_IFC_Load.do?id_set=<%= shared_ifc_set.getId_set() %>" > 
													<%= shared_ifc_set.getName() %>
												</a>	
											</td>
		
											<% 
												if (request.isUserInRole(WebConstants.Security_Permission_ADMIN)){ 
											%>		
											<td> 
												<input type="button" name="detach_shared_ifc" 
													"value="Detach" onclick="add_action_for_form(6, <%= shared_ifc_set.getId_set() %>);"/>													
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
								</table> <!-- list-shared-ifc-table -->
							</td>				
						</tr>
						</table> <!--bottom-right-table>	
					</td>
				</tr>
				</table> <!-- bottom-side-table -->
			</td>
		</tr>
		<%
			}
		%>
		</table> <!-- main-table-->
	</html:form>
</body>

</html>
