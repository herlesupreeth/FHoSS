/*
  *  Copyright (C) 2004-2007 FhG Fokus
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

package de.fhg.fokus.hss.main;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.ShNotification;
import de.fhg.fokus.hss.db.op.ShNotification_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.DiameterStack;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class ShEventsWorker extends Thread{
	private DiameterStack diameterStack;
	int timeout;
	
	public ShEventsWorker(DiameterStack diameterStack, int timeout){
		this.diameterStack = diameterStack;
		this.timeout = timeout;
	}
	
	public void run(){
		try{
			
			while (true){
				Thread.sleep(1000 * timeout);
				
				boolean dbException = false;
				try{
					Session session = HibernateUtil.getCurrentSession();
					HibernateUtil.beginTransaction();
					ShNotification sh_notification = ShNotification_DAO.get_next_available(session);
					
					while (sh_notification != null){
						// mark row(s) that was/were already taken into consideration
						ShNotification_DAO.mark_all_from_grp(session, sh_notification.getGrp());
						
						Task task = new Task (diameterStack, 1, DiameterConstants.Command.PNR, DiameterConstants.Application.Sh);
						task.id_application_server = sh_notification.getId_application_server();
						task.id_impu = sh_notification.getId_impu();
						task.grp = sh_notification.getGrp();
						diameterStack.hssContainer.tasksQueue.add(task);
						
						sh_notification = ShNotification_DAO.get_next_available(session);	
					}					
					
				}
				catch (HibernateException e){
					//logger.error("Hibernate Exception occured!\nReason:" + e.getMessage());
					e.printStackTrace();
					dbException = true;
				}
				finally{
					if (!dbException){
						HibernateUtil.commitTransaction();
					}
					HibernateUtil.closeSession();
				}
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
