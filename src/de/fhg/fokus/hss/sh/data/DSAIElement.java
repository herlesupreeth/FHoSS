/**
 * 
 */
package de.fhg.fokus.hss.sh.data;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class DSAIElement {
	private String tag = null;
	private int value = -1;
	
	public DSAIElement(){

	}
	
	public String toString(){
		StringBuffer sBuffer = new StringBuffer();
		
		sBuffer.append(ShDataTags.DSAI_s);
		if (tag != null){
			sBuffer.append(ShDataTags.DSAI_Tag_s);
			sBuffer.append(tag);
			sBuffer.append(ShDataTags.DSAI_Tag_e);
		}
		
		if (value != -1){
			sBuffer.append(ShDataTags.DSAI_Value_s);
			sBuffer.append(value);
			sBuffer.append(ShDataTags.DSAI_Value_e);
		}
		sBuffer.append(ShDataTags.DSAI_e);
		
		return sBuffer.toString();
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
