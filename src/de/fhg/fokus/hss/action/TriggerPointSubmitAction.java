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
package de.fhg.fokus.hss.action;

import de.fhg.fokus.hss.form.SptForm;
import de.fhg.fokus.hss.form.TriggerPointForm;
import de.fhg.fokus.hss.model.Spt;
import de.fhg.fokus.hss.model.SptBO;
import de.fhg.fokus.hss.model.Trigpt;
import de.fhg.fokus.hss.model.TrigptBO;

import org.apache.log4j.Logger;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.hibernate.exception.ConstraintViolationException;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Stores a trigger point and his assigned service point trigger data set. TODO:
 * Validate XML Syntax, like TgpXMLShowAction.
 * 
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class TriggerPointSubmitAction extends HssAction
{
	private static final Logger LOGGER = Logger
			.getLogger(TriggerPointSubmitAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reponse)
			throws Exception
	{
		LOGGER.debug("entering");

		ActionForward forward = null;
		TriggerPointForm form = (TriggerPointForm) actionForm;
		LOGGER.debug(form);

		Integer primaryKey = form.getPrimaryKey();
		Trigpt trigpt = null;

		try
		{
			if (primaryKey.intValue() == -1)
			{
				trigpt = createTriggerPoint(form);
			}

			else
			{
				trigpt = updateTriggerPoint(form, primaryKey);
			}

			forward = mapping.findForward(FORWARD_SUCCESS);
			forward = new ActionForward(forward.getPath() + "?trigPtId="
					+ trigpt.getTrigptId(), true);
		} catch (ConstraintViolationException e)
		{
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(Globals.MESSAGE_KEY, new ActionMessage(
					"error.duplicate"));
			saveMessages(request, actionMessages);
			forward = mapping.findForward(FORWARD_FAILURE);
		}

		LOGGER.debug("exiting");

		return forward;
	}

	/**
	 * @param form
	 * @return
	 */
	private Trigpt createTriggerPoint(TriggerPointForm form)
			throws ConstraintViolationException
	{
		LOGGER.debug("entering");

		Trigpt trigpt = null;

		try
		{
			// Create a new TriggerPoint
			openSession();
			trigpt = new Trigpt();
			trigpt.setName(form.getTrigPtName());
			trigpt.setCnf(form.getCnf());

			Spt spt = new Spt();
			spt.setGroupId(0);
			spt.setType(TrigptBO.TYPE_URI);
			spt.setTrigpt(trigpt);
			spt.setNeg(false);
			beginnTx();
			getSession().save(trigpt);
			spt.setTrigpt(trigpt);
			getSession().save(spt);
			endTx();
		} catch (ConstraintViolationException e)
		{
			LOGGER.debug(this, e);
			throw e;
		} finally
		{
			closeSession();
		}

		LOGGER.debug("exiting");

		return trigpt;
	}

	/**
	 * updateTriggerPoint
	 * @param form
	 * @param primaryKey
	 * @return
	 */
	private Trigpt updateTriggerPoint(TriggerPointForm form, Integer primaryKey)
			throws ConstraintViolationException
	{
		LOGGER.debug("entering");

		// Update a Trigger Point
		TrigptBO trigptBO = null;
		Trigpt trigpt = null;

		try
		{
			trigptBO = new TrigptBO();
			trigpt = trigptBO.load(primaryKey);
			saveSpts(form, trigpt);
			trigpt.setName(form.getTrigPtName());
			trigpt.setCnf(form.getCnf());
			trigptBO.saveOrUpdate(trigpt);
		} catch (ConstraintViolationException e)
		{
			LOGGER.debug(this, e);
			throw e;
		} finally
		{
			trigptBO.closeSession();
		}

		LOGGER.debug("exiting");

		return trigpt;
	}

	/**
	 * Update, delete or save the nested service point trigger.
	 * @param form
	 * @param trigpt
	 */
	private void saveSpts(TriggerPointForm form, Trigpt trigpt)
	{
		Iterator itSptForms = form.getSpts().iterator();

		try
		{
			beginnTx();

			int newGroupId = -1;
			int formGroupId = -1;
			Spt spt = null;
			SptForm sptForm = null;
			Integer primaryKey = null;

			while (itSptForms.hasNext())
			{
				sptForm = (SptForm) itSptForms.next();
				primaryKey = sptForm.getPrimaryKey();
				spt = (Spt) getSession().load(Spt.class, primaryKey);
				spt.addPropertyChangeListener(trigpt);

				if (sptForm.isDelete() == true)
				{
					if (primaryKey.intValue() != -1)
					{
						getSession().delete(spt);
						trigpt.markChanged(true);
					}
				} else
				{
					getSptChoice(sptForm, spt);

					if (formGroupId != sptForm.getGroup())
					{
						formGroupId = sptForm.getGroup();
						newGroupId++;
					}

					spt.setGroupId(newGroupId);
					spt.setNeg(sptForm.isNeg());
					getSession().saveOrUpdate(spt);
				}
			}

			addSpt(form, trigpt);

			endTx();
		} finally
		{
			closeSession();
		}
	}

	/**
	 * getSptChoice
	 * @param sptForm
	 * @param spt
	 */
	private void getSptChoice(SptForm sptForm, Spt spt)
	{
		switch (spt.getType())
		{
		case TrigptBO.TYPE_URI:
			spt.setReqUri(sptForm.getRequestUri());

			break;

		case TrigptBO.TYPE_SIP_METHOD:
			spt.setSipMethod(sptForm.getSipMethod());

			break;

		case TrigptBO.TYPE_SESSION_CASE:
			spt.setSessionCase(Integer.parseInt(sptForm.getSessionCase()));

			break;

		case TrigptBO.TYPE_SESSION_DESC:
			spt.setSessionDescLine(sptForm.getSessionDescLine());
			spt.setSessionDescContent(sptForm.getSessionDescContent());

			break;

		case TrigptBO.TYPE_SIP_HEADER:
			spt.setSipHeader(sptForm.getSipHeader());
			spt.setSipHeaderContent(sptForm.getSipHeaderContent());

			break;
		}
	}

	/**
	 * Adds a new SPT to form and to session.
	 * 
	 * @param form
	 * @param trigpt
	 */
	private void addSpt(TriggerPointForm form, Trigpt trigpt)
	{
		// Check for new Selected SPT's
		if ((form.getGroup() != -1) && (form.getType() != -1))
		{
			Spt spt = new Spt();
			spt.setGroupId(form.getGroup());
			spt.setType(form.getType());
			spt.setTrigpt(trigpt);
			spt.setNeg(false);

			switch (form.getType())
			{
			case TrigptBO.TYPE_URI:
				spt.setReqUri("");

				break;

			case TrigptBO.TYPE_SESSION_CASE:
				spt.setSessionCase(0);

				break;

			case TrigptBO.TYPE_SESSION_DESC:
				spt.setSessionDescContent("");
				spt.setSessionDescLine("");

				break;

			case TrigptBO.TYPE_SIP_METHOD:
				spt.setSipMethod(SptBO.SIP_METHOD.INVITE);

				break;

			case TrigptBO.TYPE_SIP_HEADER:
				spt.setSipHeader("");
				spt.setSipHeaderContent("");

				break;
			}

			getSession().saveOrUpdate(spt);
		}
	}
}
