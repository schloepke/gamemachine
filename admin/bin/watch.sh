#!/bin/bash
while true
do
   ATIME=`stat -c %Z /tmp/restart.txt`
   if [[ "$ATIME" != "$LTIME" ]]
   then
       bin/rails.sh restart
       LTIME=$ATIME
   fi
   sleep 1
done
