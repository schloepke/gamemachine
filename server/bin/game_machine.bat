@echo off

set GM_HOME=%~dp0..
set GM_CLASSPATH=%GM_HOME%\java\server\lib\*

jruby -J-cp "%GM_CLASSPATH%" bin/game_machine %*