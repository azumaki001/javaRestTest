#!/bin/bash

######################################################
# 自動PUSH 起動シェル                                #
#  @version 1.0                                      #
#  @author NSD 陳                                    #
######################################################

PUSH_HOME=/PXS/Willow/AutoPush/CE

# シェル名
SHELLNAME=`basename $0`

echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # ${SHELLNAME}#処理を開始します"

LANG=ja_JP.UTF-8        ; export LANG 
HEAP_SIZE=64M           ; export HEAP_SIZE
JAVA_HOME="C:/Program Files (x86)/Java/jdk1.7.0_45" ; export JAVA_HOME
CONF_ROOT=$PUSH_HOME/conf ; export CONF_ROOT
PROPERTY_FILE=AutoPush.conf ; export PROPERTY_FILE
PXS_HTTP_TIMEOUT=30 ; export PXS_HTTP_TIMEOUT

# ２重起動チェック
ret=`ps -ef | grep java | grep $PUSH_HOME/conf/$PROPERTY_FILE`
status=$?
if [ $status = 0 ]; then
   echo `date +"%Y/%m/%d %H:%M:%S"` "ERROR # 既にプロセスは起動しています。処理を終了します。"
   exit 1
fi

echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # プロセスを起動します。"

CLASSPATH=$PUSH_HOME/lib/pxs-autopush-test.jar:$PUSH_HOME/lib/sawara.jar:$PUSH_HOME/lib/log4j-1.2.17.jar:$PUSH_HOME/lib/pxs-mutual.jar:$PUSH_HOME/lib/commons-httpclient-2.0-rc2.jar:$PUSH_HOME/lib/commons-logging.jar:$PUSH_HOME/lib/commons-lang-2.0.jar

export CLASSPATH

	java \
	-server \
	-Xms${HEAP_SIZE} -Xmx${HEAP_SIZE} \
	-classpath $CLASSPATH \
	-DConfigRoot=$CONF_ROOT \
	-DAutoPushProperty=$CONF_ROOT/$PROPERTY_FILE \
	-DPXS_HTTP_TIMEOUT=$PXS_HTTP_TIMEOUT \
	jp.co.ana.pxs.autopush.AutoPush
exit 0
