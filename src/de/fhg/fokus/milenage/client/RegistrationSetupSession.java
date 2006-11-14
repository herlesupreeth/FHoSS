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
package de.fhg.fokus.milenage.client;

/**
 * @author Sebastian Linkiewicz, dev -at- open - ims dot org
 */
public class RegistrationSetupSession
{
    private RegistrationSetupSessionState state;

    protected RegistrationSetupSession()
    {
        state = RegistrationSetupSessionState.REGSESSIONSTATE_ENDED;
    }

    public void updateStateToInit()
            throws InvalidRegistrationSessionStateException
    {
        if (state == RegistrationSetupSessionState.REGSESSIONSTATE_ENDED
                || state == RegistrationSetupSessionState.REGSESSIONSTATE_FAILED)
        {
            state = RegistrationSetupSessionState.REGSESSIONSTATE_INIT;
        }
        else
        {
            throw new InvalidRegistrationSessionStateException(
                    "error in updateStateToInit: " + state.getAsString());
        }
    }

    public void updateStateToAuthentication()
            throws InvalidRegistrationSessionStateException
    {
        if (state == RegistrationSetupSessionState.REGSESSIONSTATE_INIT
                || state == RegistrationSetupSessionState.REGSESSIONSTATE_SYNCH)
        {
            state = RegistrationSetupSessionState.REGSESSIONSTATE_AUTH;
        }
        else
        {
            throw new InvalidRegistrationSessionStateException(
                    "error in updateStateToAuthentication: "
                            + state.getAsString());
        }
    }

    public void updateStateToSynchronization()
            throws InvalidRegistrationSessionStateException
    {

        if (state != RegistrationSetupSessionState.REGSESSIONSTATE_INIT)
        {
            throw new InvalidRegistrationSessionStateException(
                    "error in updateStateToSynchronization: "
                            + state.getAsString());
        }

        state = RegistrationSetupSessionState.REGSESSIONSTATE_SYNCH;
    }

    public void updateStateToEnded()
            throws InvalidRegistrationSessionStateException
    {
        state = RegistrationSetupSessionState.REGSESSIONSTATE_ENDED;
    }

    protected void updateStateToFailed()
    {
        state = RegistrationSetupSessionState.REGSESSIONSTATE_FAILED;
    }

    public void testSynchronizationState()
            throws InvalidRegistrationSessionStateException
    {
        if (state != RegistrationSetupSessionState.REGSESSIONSTATE_SYNCH)
        {
            throw new InvalidRegistrationSessionStateException(
                    "error in updateStateToSynchronization: "
                            + state.getAsString());
        }
    }

    public boolean isStateSynchronization()
    {
        return state == RegistrationSetupSessionState.REGSESSIONSTATE_SYNCH;
    }
}
