@echo off

set PLAYER_ID=controller4
set CLOUD_HOST=cloud-dev.gamemachine.io
set API_KEY=1a77d75e32767f89b0710311be1fd8a5
set AKKA_HOME=%~dp0..
set JAVA_OPTS=-Xmx40M
set AKKA_CLASSPATH=%AKKA_HOME%\deploy\*;%AKKA_HOME%\config;%AKKA_HOME%\lib\akka\*

java %JAVA_OPTS% -cp "%AKKA_CLASSPATH%" -Dakka.home="%AKKA_HOME%" -Djava.security.policy="%AKKA_HOME%\config\agent.policy" akka.kernel.Main io.gamemachine.client.agent.Runner