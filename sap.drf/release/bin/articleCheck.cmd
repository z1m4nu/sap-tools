@echo off
SET mypath=%~dp0
set ARTCHECK_HOME=%mypath:~0,-1%\..\
set JAVA_HOME=D:\tools\Java\jre.1.8.0_172


rem set the classes
setlocal EnableDelayedExpansion
rem loop through the libs and add them to the class path
for /R %ARTCHECK_HOME%/lib %%a in (*.jar) do (
   set ARTCHECK_CLASS_PATH=!ARTCHECK_CLASS_PATH!;%%a
)
set ARTCHECK_CLASS_PATH=!ARTCHECK_CLASS_PATH!


rem ----- Execute The Requested Command ---------------------------------------
echo Using ARTCHECK_HOME:   %ARTCHECK_HOME%
echo Using JAVA_HOME:    %JAVA_HOME%
echo Using CLASSPATH: %ARTCHECK_CLASS_PATH%
set _RUNJAVA="%JAVA_HOME%\bin\java"

%_RUNJAVA% %JAVA_OPTS% -Dfile.encoding=UTF-8 -Dlogfilename=%ARTCHECK_HOME%\log\%COMPUTERNAME%.log -Dlog4j.configuration=file:///%ARTCHECK_HOME%/conf/log4j.properties -cp %ARTCHECK_CLASS_PATH% org.crossroad.sap.drf.DRFCheck -d %ARTCHECK_HOME% %*
endlocal
:end