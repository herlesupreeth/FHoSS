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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.fhg.fokus.hss.db.hibernate.HibernateUtil;
import de.fhg.fokus.hss.db.model.IMPI;
import de.fhg.fokus.hss.db.model.IMPU;
import de.fhg.fokus.hss.db.model.CxEvents;
import de.fhg.fokus.hss.db.op.IMPI_DAO;
import de.fhg.fokus.hss.db.op.IMPU_DAO;
import de.fhg.fokus.hss.db.op.CxEvents_DAO;
import de.fhg.fokus.hss.diam.DiameterConstants;
import de.fhg.fokus.hss.diam.DiameterStack;

/**
 * @author adp dot fokus dot fraunhofer dot de 
 * Adrian Popescu / FOKUS Fraunhofer Institute
 */

public class CxEventsWorker extends Thread{

	// timeout in seconds
	private int timeout;
	private DiameterStack diameterStack;
	
	public CxEventsWorker(DiameterStack diameterStack, int timeout){
		this.diameterStack = diameterStack;
		this.timeout = timeout; 
	}
	
	public void run(){
		try{
			while (true){
				Thread.sleep(timeout * 1000);
				
				boolean dbException = false;
				try{
					Session session = HibernateUtil.getCurrentSession();
					HibernateUtil.beginTransaction();
					
					CxEvents rtr_ppr = CxEvents_DAO.get_next_available(session);
					while (rtr_ppr != null){
						
						// mark row(s) that it was already taken into consideration
						CxEvents_DAO.mark_all_from_grp(session, rtr_ppr.getGrp());
						
						if (rtr_ppr.getType() == 1){
							//	we have RTR
							List all_rows_from_grp = CxEvents_DAO.get_all_from_grp(session, rtr_ppr.getGrp());
							List<IMPI> impiList = null;
							List<IMPU> impuList = null;
							if (all_rows_from_grp != null && all_rows_from_grp.size() > 0){
								CxEvents row = null;
								IMPI impi = null;
								IMPU impu = null;
								for (int i = 0; i < all_rows_from_grp.size(); i++){
									row = (CxEvents)all_rows_from_grp.get(i);
									if (i == 0){
										impi = IMPI_DAO.get_by_ID(session, row.getId_impi());
										if (impiList == null){
											impiList = new ArrayList<IMPI>();
										}
										impiList.add(impi);
									}
									
									if (i != 0 && row.getId_impu() == -1){
										impi = IMPI_DAO.get_by_ID(session, row.getId_impi());
										impiList.add(impi);
									}
									if (row.getId_impu() != -1){
										impu = IMPU_DAO.get_by_ID(session, row.getId_impu());
										if (impuList == null){
											impuList = new ArrayList<IMPU>();
										}
										impuList.add(impu);
									}
								}
								// prepare the RTR request
								Task task = new Task (diameterStack, 1, DiameterConstants.Command.RTR, DiameterConstants.Application.Cx);
								task.grp = row.getGrp();
								task.impiList = impiList;
								task.impuList = impuList;
								task.reasonCode = row.getSubtype();
								task.reasonInfo = row.getReason_info();
								task.diameter_name = row.getDiameter_name();
								diameterStack.hssContainer.tasksQueue.add(task);	
							}
						}
						else if (rtr_ppr.getType() == 2){
							// prepare the PPR request
							Task task = new Task (diameterStack, 1, DiameterConstants.Command.PPR, DiameterConstants.Application.Cx);
							task.type = rtr_ppr.getSubtype();
							task.grp = rtr_ppr.getGrp();
							task.id_impi = rtr_ppr.getId_impi();
							task.id_implicit_set = rtr_ppr.getId_implicit_set();
							diameterStack.hssContainer.tasksQueue.add(task);
						}
						rtr_ppr = CxEvents_DAO.get_next_available(session);
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
		catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}
