<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>	
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
	
<logic:messagesPresent>
<font color="#FF0000">
	<br/>
	<bean:message key="error.header" /><br>
	<html:errors/>
</font>
</logic:messagesPresent>

<font color="#FF0000">
<html:messages id="message" message="true">
	<bean:message key="error.header" /><br>
	<li><bean:write name="message"/></li>
</html:messages>
</font>	