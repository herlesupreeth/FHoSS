-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	4.1.13-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema hssdb
--


CREATE DATABASE /*!32312 IF NOT EXISTS*/ hssdb;
USE hssdb;

--
-- Table structure for table `hssdb`.`apsvr`
--

DROP TABLE IF EXISTS `apsvr`;
CREATE TABLE `apsvr` (
  `apsvr_id` int(10) unsigned NOT NULL auto_increment COMMENT 'internal server id',
  `name` varchar(255) NOT NULL default 'new server' COMMENT 'name of the server',
  `address` varchar(255) NOT NULL default '',
  `default_handling` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`as_perm_list`
--

DROP TABLE IF EXISTS `as_perm_list`;
CREATE TABLE `as_perm_list` (
  `apsvr_id` int(10) unsigned NOT NULL default '0',
  `PULL` char(1) NOT NULL default '0',
  `PULL_REP_DATA` char(1) NOT NULL default '0',
  `PULL_IMPU` char(1) NOT NULL default '0',
  `PULL_IMPU_USER_STATE` char(1) NOT NULL default '0',
  `PULL_SCSCF_NAME` char(1) NOT NULL default '0',
  `PULL_IFC` char(1) NOT NULL default '0',
  `PULL_LOC_INFO` char(1) NOT NULL default '0',
  `PULL_USER_STATE` char(1) NOT NULL default '0',
  `PULL_CHARGING` char(1) NOT NULL default '0',
  `PULL_PSI` char(1) NOT NULL default '0',
  `PULL_MSISDN` char(1) NOT NULL default '0',
  `UPD_REP_DATA` char(1) NOT NULL default '0',
  `UPD_PSI` char(1) NOT NULL default '0',
  `SUB_REP_DATA` char(1) NOT NULL default '0',
  `SUB_IMPU_USER_STATE` char(1) NOT NULL default '0',
  `SUB_SCSCFNAME` char(1) NOT NULL default '0',
  `SUB_IFC` char(1) NOT NULL default '0',
  `SUB_PSI` char(1) NOT NULL default '0',
  PRIMARY KEY  (`apsvr_id`),
  CONSTRAINT `FK_as_perm_list_1` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`chrginfo`
--

DROP TABLE IF EXISTS `chrginfo`;
CREATE TABLE `chrginfo` (
  `chrg_id` int(10) unsigned NOT NULL auto_increment,
  `PRI_CHRG_COLL_FN_NAME` varchar(255) NOT NULL default '',
  `NAME` varchar(45) NOT NULL default '',
  `SEC_CHRG_COLL_FN_NAME` varchar(255) default NULL,
  `PRI_EVENT_CHRG_FN_NAME` varchar(255) NOT NULL default '',
  `SEC_EVENT_CHRG_FN_NAME` varchar(255) default NULL,
  PRIMARY KEY  (`chrg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`diam_servers`
--

DROP TABLE IF EXISTS `diam_servers`;
CREATE TABLE `diam_servers` (
  `server` varchar(255) NOT NULL default '',
  `host` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`server`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`ifc`
--

DROP TABLE IF EXISTS `ifc`;
CREATE TABLE `ifc` (
  `ifc_id` int(10) unsigned NOT NULL auto_increment,
  `trigpt_id` int(10) unsigned NOT NULL default '0',
  `apsvr_id` int(10) unsigned NOT NULL default '0',
  `ifc_name` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`ifc_id`),
  KEY `ifctrigpt` USING BTREE (`trigpt_id`),
  KEY `FK_ifc_2` (`apsvr_id`),
  CONSTRAINT `FK_ifc_1` FOREIGN KEY (`trigpt_id`) REFERENCES `trigpt` (`trigpt_id`),
  CONSTRAINT `FK_ifc_2` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `hssdb`.`ifc2svp`
--

DROP TABLE IF EXISTS `ifc2svp`;
CREATE TABLE `ifc2svp` (
  `ifc_id` int(10) unsigned NOT NULL default '0',
  `svp_id` int(10) unsigned NOT NULL default '0',
  `priority` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ifc_id`,`svp_id`),
  KEY `FK_ifc2svpr_2` USING BTREE (`svp_id`),
  CONSTRAINT `FK_ifc2svp_1` FOREIGN KEY (`ifc_id`) REFERENCES `ifc` (`ifc_id`),
  CONSTRAINT `FK_ifc2svp_2` FOREIGN KEY (`svp_id`) REFERENCES `svp` (`svp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 11264 kB; (`ifc_id`) REFER `hssdb/ifc`(`ifc_id`';

--
-- Table structure for table `hssdb`.`impi`
--

DROP TABLE IF EXISTS `impi`;
CREATE TABLE `impi` (
  `impi_string` varchar(255) NOT NULL default '' COMMENT 'private id',
  `imsu_id` int(10) unsigned NOT NULL default '0' COMMENT 'user id',
  `impi_id` int(10) unsigned NOT NULL auto_increment,
  `imsi` varchar(255) NOT NULL default '',
  `scscf_name` varchar(255) default NULL,
  `s_key` varchar(32) default '00000000000000000000000000000000',
  `auth_scheme` varchar(45) default 'Digest-AKAv1-MD5',
  `amf` varchar(4) default '0000',
  `algorithm` varchar(45) default 'AKAv1',
  `operator_id` varchar(32) default '00000000000000000000000000000000',
  `sqn` varchar(12) default NULL,
  `chrg_id` int(10) unsigned default NULL,
  `uiccType` int(10) unsigned NOT NULL default '0',
  `keyLifeTime` int(10) unsigned default '3600',
  PRIMARY KEY  (`impi_id`),
  UNIQUE KEY `Index_3` USING HASH (`impi_string`),
  KEY `userid` (`imsu_id`),
  KEY `FK_impi_2` (`chrg_id`),
  CONSTRAINT `FK_impi_1` FOREIGN KEY (`imsu_id`) REFERENCES `imsu` (`imsu_id`),
  CONSTRAINT `FK_impi_2` FOREIGN KEY (`chrg_id`) REFERENCES `chrginfo` (`chrg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`impu`
--

DROP TABLE IF EXISTS `impu`;
CREATE TABLE `impu` (
  `impu_id` int(10) unsigned NOT NULL auto_increment COMMENT 'public id',
  `user_status` char(1) NOT NULL default '0',
  `sip_url` varchar(255) NOT NULL default '',
  `tel_url` varchar(255) NOT NULL default '',
  `barring_indication` char(1) NOT NULL default '0',
  `psi` char(1) NOT NULL default '0',
  `psi_id` int(10) unsigned default NULL,
  `svp_id` int(10) unsigned default NULL,
  PRIMARY KEY  (`impu_id`),
  KEY `FK_impu_1` (`psi_id`),
  KEY `FK_impu_2` (`svp_id`),
  CONSTRAINT `FK_impu_1` FOREIGN KEY (`psi_id`) REFERENCES `psi` (`psi_id`),
  CONSTRAINT `FK_impu_2` FOREIGN KEY (`svp_id`) REFERENCES `svp` (`svp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`impu2impi`
--

DROP TABLE IF EXISTS `impu2impi`;
CREATE TABLE `impu2impi` (
  `impi_id` int(10) unsigned NOT NULL default '0',
  `impu_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`impi_id`,`impu_id`),
  KEY `FK_impi2impu_2` (`impu_id`),
  CONSTRAINT `FK_impi2impu_2` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`),
  CONSTRAINT `FK_impu2impi_2` FOREIGN KEY (`impi_id`) REFERENCES `impi` (`impi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 11264 kB; (`svpr_id`) REFER `hssdb/svpr`(`svpr_';

--
-- Table structure for table `hssdb`.`imsu`
--

DROP TABLE IF EXISTS `imsu`;
CREATE TABLE `imsu` (
  `imsu_id` int(10) unsigned NOT NULL auto_increment COMMENT 'users pk',
  `name` varchar(255) NOT NULL default 'new user' COMMENT 'users last name',
  PRIMARY KEY  (`imsu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='contains basic user data';

--
-- Table structure for table `hssdb`.`networks`
--

DROP TABLE IF EXISTS `networks`;
CREATE TABLE `networks` (
  `nw_id` int(10) unsigned NOT NULL auto_increment,
  `network_string` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`nw_id`),
  UNIQUE KEY `Index_2` (`network_string`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`notify_ifc`
--

DROP TABLE IF EXISTS `notify_ifc`;
CREATE TABLE `notify_ifc` (
  `impu_id` int(10) unsigned NOT NULL default '0',
  `ifcApsvr_id` int(10) unsigned NOT NULL default '0',
  `apsvr_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`impu_id`,`ifcApsvr_id`,`apsvr_id`),
  KEY `FK_notify_ifc_2` (`apsvr_id`),
  KEY `FK_notify_ifc_3` (`ifcApsvr_id`),
  CONSTRAINT `FK_notify_ifc_3` FOREIGN KEY (`ifcApsvr_id`) REFERENCES `apsvr` (`apsvr_id`),
  CONSTRAINT `FK_notify_ifc_1` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`),
  CONSTRAINT `FK_notify_ifc_2` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 11264 kB; (`impu_id`) REFER `hssdb/impu`(`impu_';

--
-- Table structure for table `hssdb`.`notify_ims_user_state`
--

DROP TABLE IF EXISTS `notify_ims_user_state`;
CREATE TABLE `notify_ims_user_state` (
  `impu_id` int(10) unsigned NOT NULL default '0',
  `apsvr_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`impu_id`,`apsvr_id`),
  KEY `FK_notify_ims_user_state_2` (`apsvr_id`),
  CONSTRAINT `FK_notify_ims_user_state_1` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`),
  CONSTRAINT `FK_notify_ims_user_state_2` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`notify_rep_data`
--

DROP TABLE IF EXISTS `notify_rep_data`;
CREATE TABLE `notify_rep_data` (
  `impu_id` int(10) unsigned NOT NULL default '0',
  `svc_ind` char(45) NOT NULL default '0',
  `apsvr_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`impu_id`,`svc_ind`,`apsvr_id`),
  KEY `FK_notify_rep_data_2` (`apsvr_id`),
  CONSTRAINT `FK_notify_rep_data_1` FOREIGN KEY (`impu_id`, `svc_ind`) REFERENCES `rep_data` (`impu_id`, `svc_ind`),
  CONSTRAINT `FK_notify_rep_data_2` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;

--
-- Table structure for table `hssdb`.`notify_scscfname`
--

DROP TABLE IF EXISTS `notify_scscfname`;
CREATE TABLE `notify_scscfname` (
  `impu_id` int(10) unsigned NOT NULL default '0',
  `apsvr_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`impu_id`,`apsvr_id`),
  KEY `FK_notify_scscfname_2` (`apsvr_id`),
  CONSTRAINT `FK_notify_scscfname_1` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`),
  CONSTRAINT `FK_notify_scscfname_2` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Table structure for table `hssdb`.`psi`
--

DROP TABLE IF EXISTS `psi`;
CREATE TABLE `psi` (
  `psi_id` int(10) unsigned NOT NULL auto_increment,
  `templ_id` int(10) unsigned NOT NULL default '0',
  `name` varchar(45) NOT NULL default '',
  `wildcard` varchar(255) NOT NULL default '',
  `impu_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`psi_id`),
  KEY `FK_psi_1` (`templ_id`),
  KEY `FK_psi_2` (`impu_id`),
  CONSTRAINT `FK_psi_1` FOREIGN KEY (`templ_id`) REFERENCES `psi_templ` (`templ_id`),
  CONSTRAINT `FK_psi_2` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `hssdb`.`psi2impu`
--

DROP TABLE IF EXISTS `psi2impu`;
CREATE TABLE `psi2impu` (
  `psi_id` int(10) unsigned NOT NULL default '0',
  `impu_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`psi_id`,`impu_id`),
  KEY `FK_psi2impu_2` (`impu_id`),
  CONSTRAINT `FK_psi2impu_1` FOREIGN KEY (`psi_id`) REFERENCES `psi` (`psi_id`),
  CONSTRAINT `FK_psi2impu_2` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `hssdb`.`psi_templ`
--

DROP TABLE IF EXISTS `psi_templ`;
CREATE TABLE `psi_templ` (
  `templ_id` int(10) unsigned NOT NULL auto_increment,
  `apsvr_id` int(10) unsigned default NULL,
  `templ_name` varchar(45) NOT NULL default '',
  `username` varchar(255) NOT NULL default '',
  `hostname` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`templ_id`),
  KEY `FK_psi_templ_1` (`apsvr_id`),
  CONSTRAINT `FK_psi_templ_1` FOREIGN KEY (`apsvr_id`) REFERENCES `apsvr` (`apsvr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `hssdb`.`rep_data`
--

DROP TABLE IF EXISTS `rep_data`;
CREATE TABLE `rep_data` (
  `impu_id` int(10) unsigned NOT NULL default '0',
  `svc_ind` varchar(45) NOT NULL default '',
  `sqn` int(10) unsigned NOT NULL default '0',
  `svc_data` blob NOT NULL,
  PRIMARY KEY  (`impu_id`,`svc_ind`),
  CONSTRAINT `FK_rep_data_1` FOREIGN KEY (`impu_id`) REFERENCES `impu` (`impu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `hssdb`.`roam`
--

DROP TABLE IF EXISTS `roam`;
CREATE TABLE `roam` (
  `impi_id` int(10) unsigned NOT NULL default '0',
  `nw_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`impi_id`,`nw_id`),
  KEY `FK__1` (`nw_id`),
  CONSTRAINT `FK__1` FOREIGN KEY (`nw_id`) REFERENCES `networks` (`nw_id`),
  CONSTRAINT `FK__2` FOREIGN KEY (`impi_id`) REFERENCES `impi` (`impi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hssdb`.`spt`
--

DROP TABLE IF EXISTS `spt`;
CREATE TABLE `spt` (
  `spt_id` int(10) unsigned NOT NULL auto_increment,
  `trigpt_id` int(10) unsigned NOT NULL default '0',
  `sip_method` varchar(45) default NULL,
  `sip_header` varchar(45) default NULL,
  `req_uri` varchar(45) default NULL,
  `session_case` int(10) unsigned default '0',
  `session_desc_line` varchar(45) default NULL,
  `sip_header_content` varchar(45) default NULL,
  `session_desc_content` varchar(45) default NULL,
  `neg` int(10) unsigned NOT NULL default '0',
  `groupId` int(10) unsigned NOT NULL default '0',
  `type` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`spt_id`),
  KEY `FK_spt_1` (`trigpt_id`),
  CONSTRAINT `FK_spt_1` FOREIGN KEY (`trigpt_id`) REFERENCES `trigpt` (`trigpt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Service Point Trigger';

--
-- Table structure for table `hssdb`.`svp`
--

DROP TABLE IF EXISTS `svp`;
CREATE TABLE `svp` (
  `svp_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`svp_id`),
  UNIQUE KEY `Index_2` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `hssdb`.`trigpt`
--

DROP TABLE IF EXISTS `trigpt`;
CREATE TABLE `trigpt` (
  `trigpt_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default 'new trigger point',
  `cnf` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`trigpt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Trigger Points';

--
-- Table structure for table `hssdb`.`uss`
--

DROP TABLE IF EXISTS `uss`;
CREATE TABLE `uss` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `ussType` int(10) unsigned NOT NULL default '0',
  `flag` int(10) unsigned default NULL,
  `impi_id` int(10) unsigned NOT NULL default '0',
  `nafGroup` varchar(45) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK_uss_1` (`impi_id`),
  CONSTRAINT `FK_uss_1` FOREIGN KEY (`impi_id`) REFERENCES `impi` (`impi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

grant all privileges on hssdb.* to hss@localhost identified by 'hss';
