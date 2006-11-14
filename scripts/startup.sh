#!/bin/bash
# --------------------------------------------------------------
# Include JAR Files
# --------------------------------------------------------------
echo "Building Classpath"
for i in lib/*.jar; do CLASSPATH="$i":"$CLASSPATH"; done
echo "Classpath is $CLASSPATH."
# --------------------------------------------------------------
# Start-up
# --------------------------------------------------------------
$JAVA_HOME/bin/java -cp $CLASSPATH de.fhg.fokus.hss.main.HssServer $1 $2 $3 $4 $5 $6 $7 $8 $9
