package de.fhg.fokus.sh;

/**
 * @author Andre Charton, (dev -at- open-ims dot org)
 */
public class MissingAVP extends PermanentFailures implements DiameterBaseException {


  /**
   * the code of the Diameter error
   */
  public final static int ERRORCODE = 5005;
	
	public MissingAVP() {
		super(ERRORCODE);
	}

}
