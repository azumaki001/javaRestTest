#!/bin/sh

usage()
{
    echo >&2 ""
    echo >&2 " Usage: $0 {start | stop} "
    echo >&2 ""
}

# Main
if [ $# != 1 ]; then
    usage
    exit 1
fi

EXEC_UNIX_USER=wlsadmin

username=`id -u -n`
if [ ! $username = $EXEC_UNIX_USER ]; then
    echo >&2 ""
    echo >&2 " Executed it by user $EXEC_UNIX_USER."
    echo >&2 " aborting..."
    echo >&2 ""
    exit 1
fi

CHECK_INTERVAL_SEC=10
CHECK_RETRY_TIMES=2
AUTOVOID=/M14/PXS/Serow/AutoVoid/shell/AutoVoid
LOG_FILE=/M14/PXS_Log/Serow/AutoVoid/log/PXSAUTOVOID.log

get_log_line()
{
   if [ -s $LOG_FILE ]; then
      wc -l $LOG_FILE | awk 'BEGIN {}\
         {\
            len = length($0); \
            if ( len > 3 ) {\
            printf "LOG_FILE_LINES=%s ; export LOG_FILE_LINES\n", $1;
         }\
      }' >> tmptmptmp_$$

      . ./tmptmptmp_$$
      rm tmptmptmp_$$
   else
      LOG_FILE_LINES=0 ; export LOG_FILE_LINES
   fi
}

start_check()
{
    sleep 3
    LOG_LINES_CHECK=$LOG_FILE_LINES
    get_log_line
    if [ $LOG_LINES_CHECK -gt $LOG_FILE_LINES ]
    then
      LOG_FILE_LINES=0
    else
      LOG_FILE_LINES=$LOG_LINES_CHECK
    fi
    
    ###### Check if AUTOVOID has exactly started.
    CHECK_CMD="tail -n +${LOG_FILE_LINES} ${LOG_FILE} | grep \"started normally\""
    count=$CHECK_RETRY_TIMES
    while [ $count -gt 0 ]
    do
      sleep $CHECK_INTERVAL_SEC
      eval $CHECK_CMD > /dev/null 2>&1
      if [ "$?" -eq 0 ]; then
        break
      fi
        echo $count
        count=`expr $count - 1`
    done
    ###### "$count==0" means AUTOVOID starting-up failed.
    if [ $count -eq 0 ]; then
      echo "Starting AUTOVOID failed."
      exit 1
    fi
    ########
    /bin/echo
    /bin/echo "AUTOVOID has started!"
}

case $1 in
'start_msg')
        print "Starting AUTOVOID"
        ;;

'stop_msg')
        print "Stopping AUTOVOID"
        ;;

'start')
        get_log_line
        /usr/bin/nohup ${AUTOVOID}Start.sh > /dev/null 2>&1 &
	;;

'stop')
        ${AUTOVOID}End.sh
	;;

*)
	usage
	exit 1
	;;
esac

case $1 in
'start')
        start_check
        ;;
esac

exit 0
