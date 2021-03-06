@ECHO OFF

ECHO Create the container

SET SCRIPT_PATH=%~dp0
cd /D %SCRIPT_PATH%

REM Samza version
FOR /F "tokens=1,2* delims== eol=# " %%i in ('type "gradle.properties"') do (
	if "%%i" == "SAMZA_VERSION" set SAMZA_VERSION=%%j
	if "%%i" == "WORK_DIR" set WORK_DIR=%%j
)

REM hostname is for the application master of yarn (by default it's the container id)
docker run ^
    -d ^
    --name samza ^
    -p 22:22 ^
    -p 2181:2181 ^
    -p 9092:9092 ^
    -p 8032:8032 ^
    -p 8042:8042 ^
    -p 8088:8088 ^
    -v %cd%:%WORK_DIR% ^
    --hostname localhost ^
    gerardnico/samza-grid:%SAMZA_VERSION%


REM where:
REM   * ZOOKEEPER_PORT=2181
REM   * RESOURCEMANAGER_PORT=8032
REM   * NODEMANAGER_PORT=8042
REM   * YARNUI_PORT=8088
REM   * KAFKA_PORT=9092
REM   * SSHD=22
