#!/bin/bash -e

##
# Collection of command
##

# List
ZK_FQDN=localhost
TOPICS=$(kafka-topics.sh --zookeeper ${ZK_FQDN}:2181 --list|xargs)
EXIT_STATUS=$?
if [ $EXIT_STATUS -ne 0 ];then
    echo "kafka-topics.sh returns EXIT_STATUS:'${EXIT_STATUS}'"
    exit ${EXIT_STATUS}
fi

# Process
ps aux | grep kafka