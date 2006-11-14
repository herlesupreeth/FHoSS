/*
 * $Id$
 *
 * Copyright (C) 2004-2006 FhG Fokus
 *
 * This file is part of Open IMS Core - an open source IMS CSCFs & HSS
 * implementation
 *
 * Open IMS Core is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * For a license to use the Open IMS Core software under conditions
 * other than those described here, or to purchase support for this
 * software, please contact Fraunhofer FOKUS by e-mail at the following
 * addresses:
 *     info@open-ims.org
 *
 * Open IMS Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * It has to be noted that this Open Source IMS Core System is not
 * intended to become or act as a product in a commercial context! Its
 * sole purpose is to provide an IMS core reference implementation for
 * IMS technology testing and IMS application prototyping for research
 * purposes, typically performed in IMS test-beds.
 *
 * Users of the Open Source IMS Core System have to be aware that IMS
 * technology may be subject of patents and licence terms, as being
 * specified within the various IMS-related IETF, ITU-T, ETSI, and 3GPP
 * standards. Thus all Open IMS Core users have to take notice of this
 * fact and have to agree to check out carefully before installing,
 * using and extending the Open Source IMS Core System, if related
 * patents and licenses may become applicable to the intended usage
 * context. 
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA  
 * 
 */
package de.fhg.fokus.hss.diam;

import de.fhg.fokus.diameter.DiameterPeer.EventListener;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.apache.log4j.Logger;


/**
 * A abstract class representing the command action. It implements the send, recieve and
 * execute methods which are helpful for sending and recieving diameter messages.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public abstract class CommandAction implements EventListener
{
    private static final Logger LOGGER = Logger.getLogger(CommandAction.class);

    /**
     * This method recieves the message from a peer
     * 
     * @param FQDN fully qualified domain name
     * @param msg the recieved diameter message
     */    
    public abstract void recvMessage(String FQDN, DiameterMessage msg);

    /**
     * This method sends the message to a peer. It is called by the HSS
     * if it,for example, wants to send a PPR request to the peer.
     * 
     * @param message
     * @param serverName
     * @param resolve indicates whether the server name needs to be resolved or not
     */
    protected void sendMessage(
        DiameterMessage message, String serverName, boolean resolve)
    {
        if (resolve == true)
        {
            serverName = OriginHostResolver.getOriginHost(serverName);
        }

        if (serverName != null)
        {
            HssDiameterStack.diameterPeer.sendMessage(serverName, message);
        }
        else
        {
            LOGGER.error(
                "HSS has tryed to send PPR to unknown Origin Host: "
                + serverName);
        }
    }

    /** 
     * abstract method which performs the operations triggered by
     * some commands.
     */
    public abstract void execute();

    /**
     *  builds a String representing object of this class
     *  @return the built string 
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(
            this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
