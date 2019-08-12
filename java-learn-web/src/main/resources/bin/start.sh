#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

SERVER_NAME='data-analysis-web'
LOGS_FILE='/data/app/logs/biz/'

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`ps -f | grep java | grep "$SERVER_NAME" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

LOGS_DIR=$LOGS_FILE
#if [ -n "$LOGS_FILE" ]; then
#    LOGS_DIR=`dirname $LOGS_FILE`
#else
#    LOGS_DIR=$DEPLOY_DIR/logs
#fi
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/urandom"
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi

JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx16g -Xms8g -Xmn2048m -Xss256k -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=512M -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
    JAVA_MEM_OPTS=" -server -Xms16g -Xmx8g -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=512M -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

IP=`/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"`

echo -e "Starting the $SERVER_NAME ...\c"
nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -classpath $CONF_DIR:$LIB_JARS com.example.learn.DataAnalysisWebApplication > $STDOUT_FILE 2>&1 &

# 不输出stdout.log 全量日志,使用log4j
# nohup java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -classpath $CONF_DIR:$LIB_JARS DataAnalysisWebApplication > /dev/null 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=`ps -f | grep java | grep "$SERVER_NAME" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PIDS=`ps -f | grep java | grep "$SERVER_NAME" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"