/**
 * \package de.fhg.fokus.hss.diam
 * Base classes for the HSS - Diameter connection
 * 
 * The HssDiameterStack is the main class in the FHoSS 
 * implementation. It starts the DiameterPeer and
 * registers all CommandListener at the stack.
 * 
 * The CommandAction class is the base class for all 
 * calls to the diameter peer.
 *  
 * The CommandListener is the base class for retrieving 
 * requests from the diameter peer.
 *
 * Every listener or command maps directly to one 
 * Diameter request or response as defined in the related
 * 3GPP specifikations.
 */
package de.fhg.fokus.hss.diam;