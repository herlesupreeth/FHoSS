#!/bin/bash
#

FILE=hssdb.sql

mysqldump hssdb -d -B --add-drop-table --add-drop-database >$FILE
echo "# DB access rights" >>$FILE
echo "grant delete,insert,select,update on hssdb.* to hss@localhost identified by 'hss';" >>$FILE

FILE=userdata.sql
mysqldump hssdb -t -B  >>$FILE




