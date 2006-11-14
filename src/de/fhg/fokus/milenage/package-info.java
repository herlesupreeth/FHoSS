/** 
 * \package de.fhg.fokus.milenage
 * Provides DigestAKA utility classes for 3GPP Milenage alghoritm specification.
 * 
 * Provides DigestAKA</a> utility classes for <a href="http://www.3gpp.org/ftp/Specs/html-info/35205.htm">3GPP Milenage alghoritm specification</a>
 * code sniplet for UA processing
 * <code>
 * 	<pre>
 * 		//initialisation for client processing
 * 		ClientProcessing cp = ClientProcessing.initFromProperties(properties);
 * 		// send initial REGITER
 * 		// if the answer is 200, you are registered, stop processing
 * 		// if the anser is 401
 * 		// UA Autentication part
 * 		// init
 * 		try
 * 		{
 * 			// initialisation part for first 401 answer
 *   			Nonce nonce = new Nonce("xFp+dnVaBFsdf0yGxoDDvhAAAAAACnJcSPg95qZ4uws=");
 * 			Res xRes= null;
 * 			Auts auts= null;
 * 			// authentication
 * 			try
 * 			{
 * 			    xRes = cp.processAuthentication(nonce);
 * 			    logger.debug("response is: " + xRes);
 * 			}
 * 			catch(SynchronizationException e)
 * 			{
 * 				logger.error(e);
 * 				// UA synchronization failure handling
 * 				Auts auts = cp.handleSynchronizationFailure(nonce);
 * 			}
 * 			// send Register now (with auts if not equal null)
 * 			// if the answer is 200, you are registered, stop processing
 * 			// if you have 401 answer 
 * 			//init
 * 			nonce = new Nonce(IP0uI+8nSh+hIbTcqLvaqxAAAAAABHJcRsjgvfRqlhU=);
 * 			// process authentication
 * 			xRes = cp.processAuthentication(nonce);
 * 			// send REGISTER
 * 			// if the answer is still 401
 * 			// the authentication failed 
 * 		}
 * 		catch(Exception e)
 * 		{
 * 			// authentication failed
 * 			e.printStackTrace();
 * 		}
 * 	</pre>
 * </code>
 */
package de.fhg.fokus.milenage;