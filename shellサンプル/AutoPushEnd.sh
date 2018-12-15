#!/bin/bash

######################################################
# 自動PUSH 停止シェル                                #
#  @version 1.0                                      #
#  @author NSD 陳                                    #
######################################################

PUSH_HOME=/PXS/Willow/AutoPush/CE
CONF_ROOT=$PUSH_HOME/conf ; export CONF_ROOT
PROPERTY_FILE=AutoPush.conf ; export PROPERTY_FILE
EVENT_FILE_KEY=EndEventFilepath ; export EVENT_FILE_KEY

# シェル名
SHELLNAME=`basename $0`

# 開始ログ
echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # ${SHELLNAME}#処理を開始します"

# プロセスID取得
ps -ef | grep java | grep $PROPERTY_FILE | grep $PUSH_HOME | awk 'BEGIN {}\
   {\
     printf "PUSH_PROC_ID=%s; export PUSH_PROC_ID\n", $2;
     if ( NR > 1 ) {\
       printf "PUSH_PROC_ID=CAN_NOT_SPECIFY_PROCESS; export PUSH_PROC_ID\n";
     } \
   }' > /tmp/NSD/tinYou/temptemp_$$

# 停止指示ファイルパス取得
cat $CONF_ROOT/$PROPERTY_FILE | awk -F'=' 'BEGIN { KEYNAME=ENVIRON["EVENT_FILE_KEY"]; }\
   { len = length($1); \
     if ( len > 3 ) {\
        if ( $1 == KEYNAME ) {\
           printf "EVENT_FILE_PATH=%s; export EVENT_FILE_PATH\n", $2;
        }\
     }\
   }' >> tmptmptmp_$$

PUSH_PROC_ID=NO_PROCESS
EVENT_FILE_PATH=0

. ./tmptmptmp_$$
rm tmptmptmp_$$

# プロセス存在チェック
if [ $PUSH_PROC_ID = NO_PROCESS ]; then
   echo `date +"%Y/%m/%d %H:%M:%S"` "WARN # プロセスが存在しません。処理を終了します。"
   exit 1
fi

# プロセス特定チェック（対象プロセスが複数ある場合)
if [ $PUSH_PROC_ID = "CAN_NOT_SPECIFY_PROCESS" ]; then
   echo `date +"%Y/%m/%d %H:%M:%S"` "ERROR # 停止プロセスが特定できません。処理を終了します。"
   exit 1
fi

if [ $EVENT_FILE_PATH = 0 ]; then
   echo `date +"%Y/%m/%d %H:%M:%S"` "ERROR # $CONF_ROOT/$PROPERTY_FILE に $EVENT_FILE_KEY の定義がありません。処理を終了します。"
   exit 1
fi

# 停止待機時間を設定(停止指示ファイル監視間隔 + 自動PUSHタイムアウト時間 + a で 130秒程度待機する)
TIMEOUT_SEC=130
WAIT_TIME=5
WAIT_COUNT=0
WAIT_MAX=$(( TIMEOUT_SEC / WAIT_TIME ))


echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # プロセスID=$PUSH_PROC_ID を停止します。"
echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # $TIMEOUT_SEC 秒以内に停止しない場合は強制終了します。"

# 停止指示ファイル作成
touch $EVENT_FILE_PATH

echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # プロセスを停止中です・・・"

# プロセス終了チェック
forceShutDownFlag=0
ret=`ps x -p $PUSH_PROC_ID | grep java | grep $PUSH_HOME`
status=$?
while [ $status = 0 ]
do
   sleep $WAIT_TIME
   WAIT_COUNT=$(( WAIT_COUNT + 1 ))
   ret=`ps x -p $PUSH_PROC_ID | grep java | grep $PROPERTY_FILE | grep $PUSH_HOME`
   status=$?
   if [ $status = 0 ]; then
       if [ $WAIT_COUNT -gt $WAIT_MAX ]; then
	   forceShutDownFlag=1
	   break
       fi
   fi
done

# 停止指示ファイル削除
rm -f $EVENT_FILE_PATH

if [ $forceShutDownFlag = 0 ]; then
   echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # プロセスが正常に停止しました。"
else
   kill -9 $PUSH_PROC_ID
   echo `date +"%Y/%m/%d %H:%M:%S"` "WARN # プロセスを強制終了しました。"
fi

echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # ${SHELLNAME}#処理を終了します"
