#!/bin/sh

########################################################################
# 0. Check if JAVA_HOME is set

if [ x"$JAVA_HOME" = x ]
then
  echo "JAVA_HOME must be set...!!!"
  exit 1
fi
if [ ! -x $JAVA_HOME/bin/java ]
then
  echo "$JAVA_HOME/bin/java must exist and be executable...!!!"
  exit 1
fi
START_JAVA=$JAVA_HOME/bin/java

########################################################################
# 1a. First check whether FCBS_ROOT is set
if [ x"$BARONS_ROOT" = x ]
then
  echo "BARONS_ROOT must be set...!!!"
  exit 1
fi
if [ ! -d $BARONS_ROOT ]
then
  echo "$BARONS_ROOT directory must exist...!!!"
  exit 1
fi


########################################################################
# 2. Set BARONS_LIB to default if NOT explicitly set

if [ x"$BARONS_LIB" = x ]
then
  echo "INFO>> BARONS_LIB set to $BARONS_ROOT/lib...!!!"
  export BARONS_LIB=$BARONS_ROOT/lib
fi

if [ ! -d $BARONS_LIB ]
then
  echo "$BARONS_LIB directory must exist...!!!"
  exit 1
fi

BARONS_LIB=$BARONS_LIB/
BARONS_LIB_INTERNAL=$BARONS_LIB/internal/$MODULE
echo "INFO>> BARONS_LIB_INTERNAL set to $BARONS_LIB_INTERNAL...!!!"

export MODULE_CONFIG_ROOT=$CONFIG_ROOT/$MODULE

if [ ! -d $BARONS_LIB ]
then
  echo " $BARONS_LIB missing..."
  exit 1
fi

if [ ! -d $BARONS_LIB_INTERNAL ]
then
  echo " $BARONS_LIB_INTERNAL missing..."
  exit 1
fi


mkdir -p $BARONS_ROOT/bin/$MODULE/log/gc
echo "INFO>> $MODULE  starting.."

if [ -f $MODULE.pid ] && ps -p "$(cat $MODULE.pid)" > /dev/null 2>&1
then
  echo "$MODULE is already running with PID $(cat $MODULE.pid)"
  exit 1
else
  if [ -f nohup.out ]; then
    echo "Archiving nohup.out"
    mkdir -p log/nohup
    gzip -S "-$(date +%Y-%m-%d_%H-%M-%S).log.gz" nohup.out
    mv nohup*.gz log/nohup
    echo "Archiving nohup.out finished"
  fi
fi

$START_JAVA -DBARONS_$MODULE -Xms$minMemory -Xmx$maxMemory -jar $BARONS_LIB_INTERNAL/*.jar
#PID=$!
#echo $PID > $MODULE.pid
#echo "pid:$PID"
#sleep 2
#tail -n +0 -F nohup.out | sed '/Logger system successfully initialized/q'
#tail -1000f log/*-Main.log;