#!/bin/bash -e

zk_stat=$(echo stat | nc 127.0.0.1 2181)
if [ $? -ne 0 ]; then
  echo "No connection to localhost:2181"
  exit 2
fi
mode=$(echo "${zk_stat}" | grep Mode | awk -F\: '{print $2}')
if [ "X${mode}" == "" ]; then
  echo "Checked status endpoint; couldn't find mode"
  echo "${zk_stat}"
  exit 2
else
  set_zk_mode $(echo ${mode})
  echo "${zk_stat}"
  exit 0
fi
