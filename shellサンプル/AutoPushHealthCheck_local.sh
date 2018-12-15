#!/bin/bash

######################################################
# 自動PUSH ヘルスチェック監視シェル                  #
#  @version 1.0                                      #
#  @author NSD 陳                                    #
######################################################

#ローカル用
PUSH_HOME=/d/soft/autopush/rel00/ap-serv/PXS/Willow/AutoPush/CE
CONF_ROOT=$PUSH_HOME/conf ; export CONF_ROOT
TMP_PATH=$PUSH_HOME/tmp ; export TMP_PATH
PROPERTY_FILE=AutoPush.conf ; export PROPERTY_FILE
FOR_HEALTH_CHECK_FILE_KEY=ForHealthCheckFilePath ; export FOR_HEALTH_CHECK_FILE_KEY
#前回チェック日時退避ファイル
LAST_TIME_FILE=$TMP_PATH/LastTimeFile
NORMAL=0
ERR=1
export LANG=C
CHECK_INT=50

# シェル起動時間秒を取得（システム時刻、死活判断処理にも使用される）
START_HI=`date +"%H"`
START_MI=`date +"%M"`
START_SS=`date +"%S"`

# シェル名
SHELLNAME=`basename $0`

# 開始ログ
echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # ${SHELLNAME}#処理を開始します。"

# ヘルスチェックファイルパス取得
cat $CONF_ROOT/$PROPERTY_FILE | awk -F'=' 'BEGIN { KEYNAME=ENVIRON["FOR_HEALTH_CHECK_FILE_KEY"]; }\
   { len = length($1); \
     if ( len > 3 ) {\
        if ( $1 == KEYNAME ) {\
           printf "FOR_HEALTH_CHECK_FILE_PATH=%s; export FOR_HEALTH_CHECK_FILE_PATH\n", $2;
        }\
     }\
   }' > tmptmptmp_$$

FOR_HEALTH_CHECK_FILE_PATH=NOT_FOUND

#ローカル用
. ./tmptmptmp_$$
rm tmptmptmp_$$

if [ $FOR_HEALTH_CHECK_FILE_PATH = NOT_FOUND ]
then
    #ヘルスチェックファイルパス取得できない場合、
    echo `date +"%Y/%m/%d %H:%M:%S"` "ヘルスチェックファイルパスが取得できません"
    exit $ERR
fi

#ヘルスチェックファイルが存在するかチェック
if [ ! -f $FOR_HEALTH_CHECK_FILE_PATH ]
then
    echo `date +"%Y/%m/%d %H:%M:%S"` "ヘルスチェックファイルが存在しません"
    exit $ERR
fi

#ヘルスチェックファイルから日時を取り出す
NOW_TIME=NOT_FOUND
read NOW_TIME < $FOR_HEALTH_CHECK_FILE_PATH

#返却初期値
RETURN_CODE=$NORMAL
SAVE_FLG=ON

#前回チェック日時退避ファイルから前回日時を取り出す
if [ -f $LAST_TIME_FILE ]
then
    #前回チェック日付退避ファイルあり、日時を比較する
    #前回チェック日時取得
    LAST_TIME=NOT_FOUND
    read LAST_TIME < $LAST_TIME_FILE

    #前回と今回日時の年月日を取得し、秒数に変換して差を取得する
    NOW_YYYYMMDD="`echo $NOW_TIME |awk '{printf("%s\n",substr($0,1,8))}'`"
    LAST_YYYYMMDD="`echo $LAST_TIME |awk '{printf("%s\n",substr($0,1,8))}'`"
    YMD_TO_S_VAL=`expr \( \`date -d "$NOW_YYYYMMDD" +%s\` - \`date -d "$LAST_YYYYMMDD" +%s\` \)`

    #前回チェック日時の時刻をと取得する
    LAST_HI="`echo $LAST_TIME |awk '{printf("%s\n",substr($0,9,2))}'`"
    LAST_MI="`echo $LAST_TIME |awk '{printf("%s\n",substr($0,11,2))}'`"
    LAST_SS="`echo $LAST_TIME |awk '{printf("%s\n",substr($0,13,2))}'`"

    #システム・前回日時の時刻を秒数に変換する
    START_VAL=`echo $START_HI |awk '{printf("%s\n",$1 * 60 * 60)}'`
    START_VAL=`echo $START_MI $START_VAL |awk '{printf("%s\n",$1 * 60 + $2)}'`
    START_VAL=`echo $START_SS $START_VAL |awk '{printf("%s\n",$1 + $2)}'`
    LAST_VAL=`echo $LAST_HI |awk '{printf("%s\n",$1 * 60 * 60)}'`
    LAST_VAL=`echo $LAST_MI $LAST_VAL |awk '{printf("%s\n",$1 * 60 + $2)}'`
    LAST_VAL=`echo $LAST_SS $LAST_VAL |awk '{printf("%s\n",$1 + $2)}'`

    #システム時刻と差を求める
    DIF_VAL=`echo $YMD_TO_S_VAL $START_VAL $LAST_VAL |awk '{printf("%s\n", $2 - $3 + $1)}'`
    if [ $DIF_VAL -le $CHECK_INT ]
    then
        # 差が50秒以内の場合は、連続実行された場合と判定してヘルスチェック正常とする
        echo `date +"%Y/%m/%d %H:%M:%S"` "ヘルスチェックシェルが連続実行されました。正常終了します。"
        RETURN_CODE=$NORMAL
        SAVE_FLG=OFF
    else
        #日時を比較する
        #echo "前回チェック日時=$LAST_TIME 今回日時=$NOW_TIME"
        if [ $NOW_TIME -le $LAST_TIME ]
        then
            #日時が前回日時より同じか以前の場合、自動PUSHが動作していないと判断する
            echo `date +"%Y/%m/%d %H:%M:%S"` "自動PUSHのスレッドは動作していません。"
            RETURN_CODE=$ERR
        fi
    fi
fi

if [ $SAVE_FLG = "ON" ]
then
    #前回チェック日時ファイルに日時を退避する
    echo $NOW_TIME > $LAST_TIME_FILE
fi

echo `date +"%Y/%m/%d %H:%M:%S"` "INFO # ${SHELLNAME}#処理を終了しました。"
exit $RETURN_CODE
