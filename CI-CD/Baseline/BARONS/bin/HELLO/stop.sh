#!/bin/sh
USER=`whoami`
PID=`ps -edf |egrep "^$USER"|  grep BARONS_ | grep -v grep | awk '{ print $2 }'`



if [ -n "$PID" ]
then
	kill $PID
	echo $MODULE" shutdown! Check with 'psf'"
else
	echo $MODULE is not running.
fi

