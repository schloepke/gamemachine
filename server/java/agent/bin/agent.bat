@echo off

set AKKA_HOME=%~dp0..
set JAVA_OPTS=-Xmx40M
set AKKA_CLASSPATH=%AKKA_HOME%\deploy\*;%AKKA_HOME%\config;%AKKA_HOME%\lib\akka\*

java %JAVA_OPTS% -cp "%AKKA_CLASSPATH%" -Dakka.home="%AKKA_HOME%" akka.kernel.Main %*