<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>	
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
	<logic:messagesPresent>
	<tr>
		<td colspan="<%= request.getParameter("cols")%>" class="error">
			<font color="#FF0000">
				<html:errors property="<%= request.getParameter("fieldName")%>"/>
			</font>
		</td>
	</tr>
	</logic:messagesPresent>
	
	<!--  The Include  -->
	<jsp:include page="/pages/tiles/fieldError.jsp">
							<jsp:param name="fieldName" value="algorithm"/>
							<jsp:param name="cols" value="2"/>
						</jsp:include>