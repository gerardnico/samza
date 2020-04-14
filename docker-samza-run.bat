@ECHO OFF

ECHO Create the container

SET SCRIPT_PATH=%~dp0
cd /D %SCRIPT_PATH%

docker run ^
    -d ^
    --name samza ^
    -p 22:22 ^
    -p 2181:2181 ^
    -p 9092:9092 ^
    -p 8032:8032 ^
    -p 8042:8042 ^
    -v %cd%:/workdir ^
    gerardnico/samza-grid:1.0


REM where:
REM   * ZOOKEEPER_PORT=2181
REM   * RESOURCEMANAGER_PORT=8032
REM   * NODEMANAGER_PORT=8042
REM   * KAFKA_PORT=9092
REM   * SSHD=22
