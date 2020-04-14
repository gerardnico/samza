@ECHO OFF
REM Dev
REM Samza version
FOR /F "tokens=1,2* delims== eol=# " %%i in ('type "gradle.properties"') do (
	if "%%i" == "SAMZA_VERSION" set SAMZA_VERSION=%%j
)
docker build -t gerardnico/samza-grid:%SAMZA_VERSION% .
