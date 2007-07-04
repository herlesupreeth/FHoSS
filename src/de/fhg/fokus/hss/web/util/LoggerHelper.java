/*
 * $Id: LoggerHelper.java 119 2007-02-06 16:48:35 +0000 (Tue, 06 Feb 2007) adp $
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
package de.fhg.fokus.hss.web.util;


import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.WriterAppender;

import java.io.StringWriter;


/**
 * @author Andre Charton (dev -at- open-ims dot org)
 */
public class LoggerHelper
{
    private static Logger LOGGER = Logger.getLogger(LoggerHelper.class);
    private static final String APPENDER_NAME = "WebAppender";
    public static StringWriter BUFFER=null;;
    public static boolean STATUS=false;
    public static boolean empty_buffer=true;
    public static String loglevel=null;
    private static WriterAppender appender;

    static
    {
        off();
    }

    public static void init()
    {
        if(!STATUS){
          BUFFER = new StringWriter();
          BUFFER.append("Please select a debug level!");
          return;
        }        
        STATUS = true;
        appender = new WriterAppender();
        BUFFER = new StringWriter();
        appender.setName(APPENDER_NAME);
        appender.setWriter(BUFFER);
        if(loglevel.equals("debug"))
        {
           loglevel = "debug";
           appender.setThreshold(Priority.DEBUG);
        }
        else
        {
           loglevel="info";
           appender.setThreshold(Priority.INFO);
        }
        Layout lt = new PatternLayout("%-5p %c- %M - %m%n");
        appender.setLayout(lt);
        Logger.getRootLogger().addAppender(appender);
        LOGGER.info("WebLoggingAppender added/cleared.");
        empty_buffer=false;
    }

    public static void clear()
    {

            if(!empty_buffer)
            {
	            BUFFER.flush();
	            init();
            }
 
    }

    public static void off()
    {
        if (STATUS)
        {
            STATUS = false;
            loglevel=null;
            Logger.getRootLogger().removeAppender(appender);
        }
        else
        {
            BUFFER = new StringWriter();
            BUFFER.append(
                  "WebLoggerAppender is offline. Use the [TURN ON] link above to start logging.");
            empty_buffer=false;                  
        }


    }
}
