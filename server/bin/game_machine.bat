@echo off
title "Game Machine"
set GM_HOME=%~dp0..
set GM_CLASSPATH=%GM_HOME%\java\server\lib\*

set MAX_HEAP=800m

set JAVA_OPTS=-Xmx%MAX_HEAP%

"%GM_HOME%/jruby/bin/jruby" -J-cp "%GM_CLASSPATH%" "%JAVA_OPTS%"  "%GM_HOME%/bin/game_machine" %*

PAUSE
