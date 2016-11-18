#!/usr/bin/env bash

JAVA_HOME=/home/work/sun-java7
echo ${JAVA_HOME}

BIN_DIR=/home/work/demo

JAVA_OPTS=" -server
    -Xms2048m -Xmx2048m -Xmn1524m
    -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled
    -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75
    -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
    -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:${BIN_DIR}/logs/gc/gc.log
    -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M
    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BIN_DIR}/logs/gc/heapdump "

## jmx for jconsole and jvisual
JMX_OPTS=" -Dcom.sun.management.jmxremote
    -Dcom.sun.management.jmxremote.port=8910
    -Dcom.sun.management.jmxremote.authenticate=false
    -Dcom.sun.management.jmxremote.ssl=false "


JAVA_OPTS=" ${JAVA_OPTS} ${JMX_OPTS} ";

nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} \
-jar ${BIN_DIR}/springboot_demo-1.0-SNAPSHOT.zip --spring.profiles.active=online >>${BIN_DIR}/startup.log 2>&1 &

echo $! > ${BIN_DIR}/pid
echo "write pid to ${BIN_DIR}/pid"