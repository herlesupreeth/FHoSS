/**
 * 
 */
package de.fhg.fokus.hss.sh.data;

import java.util.Vector;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */
public class SPTElement {
	private int conditionNegated = -1;
	private int groupID = -1;
	private String requestURI = null;
	private String method = null;
	private String sipHeader = null;
	private String sipHeaderContent = null;
	private int sessionCase = -1;
	private String sessionDescLine = null;
	private String sessionDescContent = null;
	private Vector<Integer> registrationTypeList = null; 
	
	public SPTElement(){}

	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ShDataTags.SPT_s);
		
		if (conditionNegated != -1){
			sBuffer.append(ShDataTags.ConditionNegated_s);
			sBuffer.append(conditionNegated);
			sBuffer.append(ShDataTags.ConditionNegated_e);
		}
		
		if (groupID != -1){
			sBuffer.append(ShDataTags.Group_s);
			sBuffer.append(groupID);
			sBuffer.append(ShDataTags.Group_e);
		}
		
		if (requestURI != null){
			sBuffer.append(ShDataTags.RequestURI_s);
			sBuffer.append(requestURI);
			sBuffer.append(ShDataTags.RequestURI_e);
		}
		else if (method != null){
			sBuffer.append(ShDataTags.Method_s);
			sBuffer.append(method);
			sBuffer.append(ShDataTags.Method_e);
			
		}
		else if (sipHeader != null){
			sBuffer.append(ShDataTags.SIPHeader_s);
			
			sBuffer.append(ShDataTags.Header_s);
			sBuffer.append(sipHeader);
			sBuffer.append(ShDataTags.Header_e);

			if (sipHeaderContent != null){
				sBuffer.append(ShDataTags.Content_s);
				sBuffer.append(sipHeaderContent);
				sBuffer.append(ShDataTags.Content_e);
			}
			
			sBuffer.append(ShDataTags.SIPHeader_e);
		}
		else if (sessionCase != -1){
			sBuffer.append(ShDataTags.SessionCase_s);
			sBuffer.append(sessionCase);
			sBuffer.append(ShDataTags.SessionCase_e);
		}
		else if (sessionDescLine != null){
			sBuffer.append(ShDataTags.SessionDescription_s);
			
			sBuffer.append(ShDataTags.SDPLine_s);
			sBuffer.append(sessionDescLine);
			sBuffer.append(ShDataTags.SDPLine_e);

			if (sessionDescContent != null){
				sBuffer.append(ShDataTags.Content_s);
				sBuffer.append(sessionDescContent);
				sBuffer.append(ShDataTags.Content_e);
			}
			sBuffer.append(ShDataTags.SessionDescription_e);
		}
		
		if (registrationTypeList != null){
			sBuffer.append(ShDataTags.Extension_s);
			for (int i = 0; i < registrationTypeList.size(); i++){
				sBuffer.append(ShDataTags.RegistrationType_s);
				sBuffer.append(registrationTypeList.get(i));
				sBuffer.append(ShDataTags.RegistrationType_e);
			}
			sBuffer.append(ShDataTags.Extension_e);
		}

		sBuffer.append(ShDataTags.SPT_e);
		
		return sBuffer.toString();
	}
	
	public void addRegistrationType(int regType){
		if (registrationTypeList == null){
			registrationTypeList = new Vector<Integer>();
		}
		registrationTypeList.add(regType);
	}
	
	public int getConditionNegated() {
		return conditionNegated;
	}

	public void setConditionNegated(int conditionNegated) {
		this.conditionNegated = conditionNegated;
	}

	public int getSessionCase() {
		return sessionCase;
	}

	public void setSessionCase(int sessionCase) {
		this.sessionCase = sessionCase;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		
		this.requestURI = requestURI;
	}

	public String getSessionDescContent() {
		return sessionDescContent;
	}

	public void setSessionDescContent(String sessionDescContent) {
		this.sessionDescContent = sessionDescContent;
	}

	public String getSessionDescLine() {
		return sessionDescLine;
	}

	public void setSessionDescLine(String sessionDescLine) {
		this.sessionDescLine = sessionDescLine;
	}

	public String getSipHeaderContent() {
		return sipHeaderContent;
	}

	public void setSipHeaderContent(String sipHeaderContent) {
		this.sipHeaderContent = sipHeaderContent;
	}

	public String getSipHeader() {
		return sipHeader;
	}

	public void setSipHeader(String sipHeader) {
		this.sipHeader = sipHeader;
	}

	public Vector<Integer> getRegistrationTypeExtension() {
		return registrationTypeList;
	}

	public void setRegistrationTypeExtension(
			Vector<Integer> registrationTypeExtension) {
		this.registrationTypeList = registrationTypeExtension;
	}

}
