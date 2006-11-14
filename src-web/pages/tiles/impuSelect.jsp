<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<h1><bean:message key="impu.title" /></h1>
<table class="as" width="100%" border=0>
	<tr class="header">
		<td class="header"><bean:message key="impu.head.id" /></td>
		<td class="header" width="50"><bean:message key="form.head.action" /></td>
	</tr>
	<logic:iterate name="<%= request.getParameter("formName")%>" property="impus" id="impu"
		type="de.fhg.fokus.hss.model.Impu" indexId="ix">
		<tr class="<%= ix.intValue()%2 == 0 ? "even" : "odd" %>">
			<td><bean:write name="impu" property="sipUrl" /></td>
			<td><a
				href="/hss.web.console/impuShow.do?impuId=<%=impu.getImpuId()%>"> <img
				src="/hss.web.console/images/edit_small.gif" width="16" height="16">
			</a> <a
				href="/hss.web.console<%= request.getParameter("connectTarget")%>?impuSelectId=<%=impu.getImpuId()%>&<%= request.getParameter("parentId")%>=<bean:write name="<%= request.getParameter("formName")%>" property="<%= request.getParameter("parentId")%>" />&impuSelect=remove">
			<img src="/hss.web.console/images/progress_rem.gif" width="16"
				height="16" alt="Delete"> </a></td>
		</tr>
	</logic:iterate>
	<% if(request.getParameter("addAllow").equals("true")) { %>
	<tr class="header">
		<td class="header"></td>
		<td class="header"><html:form action="<%= request.getParameter("thisTarget")%>">
			<logic:equal value="true" property="impuSelect" name="<%= request.getParameter("formName")%>">
				<html:hidden property="impuSelect" value="false" />
			</logic:equal>
			<logic:notEqual value="true" property="impuSelect" name="<%= request.getParameter("formName")%>">
				<html:hidden property="impuSelect" value="true" />
			</logic:notEqual>
			<html:hidden property="<%= request.getParameter("parentId")%>" />
			<html:image src="/hss.web.console/images/add_obj.gif" />
		</html:form></td>
	</tr>
	<% } %>
	<logic:equal value="true" property="impuSelect" name="<%= request.getParameter("formName")%>">
		<tr class="formular">
			<html:form action="<%= request.getParameter("thisTarget")%>">
				<td align="left" class="formular"><html:text property="impuUrl"
					size="40" /> <html:hidden property="impuSelect" value="true" /> <html:hidden
					property="<%= request.getParameter("parentId")%>" /></td>
				<td class="formular"><html:submit value="GET" /></td>
			</html:form>
			<logic:iterate name="<%= request.getParameter("formName")%>" property="impusSelected" id="impu"
				type="de.fhg.fokus.hss.model.Impu" indexId="ix">
				<tr class="<%= ix.intValue()%2 == 0 ? "iEven" : "iOdd" %>">
					<td><bean:write name="impu" property="sipUrl" /></td>
					<td><html:form action="<%= request.getParameter("connectTarget")%>">
						<html:hidden property="impuUrl" />
						<html:hidden property="impuSelect" value="true" />
						<html:hidden property="<%= request.getParameter("parentId")%>" />
						<input type="hidden" name="impuSelectId"
							value="<bean:write name="impu" property="impuId" />">
						<html:submit titleKey="form.submit.add" altKey="form.submit.add"
							value="Add" />
					</html:form></td>
				</tr>
			</logic:iterate>

		</tr>
	</logic:equal>
</table>
