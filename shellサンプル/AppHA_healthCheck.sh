#!/bin/bash

LOOP_FLG=true

while $LOOP_FLG
do
  ret=`sh ./AutoPushHealthCheck_local.sh`
  status=$?
  if [ $status = 1 ]
  then
    LOOP_FLG=false
  fi
  sleep 50
done

exit 0