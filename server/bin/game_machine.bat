@echo off

set GM_HOME=%~dp0..
set GM_CLASSPATH=%GM_HOME%\java\server\lib\*

set MAX_HEAP=500m
set NEW_SIZE=300m

set JAVA_OPTS=-Xmx%MAX_HEAP% -Xmn%NEW_SIZE% -XX:NewSize=%NEW_SIZE% -XX:MaxNewSize=%NEW_SIZE%^
 -XX:-UseAdaptiveSizePolicy -XX:+UseParNewGC  -XX:+UseCompressedOops^
 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=32768 -XX:MaxTenuringThreshold=1^
 -XX:SurvivorRatio=190 -XX:TargetSurvivorRatio=90

jruby -J-cp "%GM_CLASSPATH%" bin\game_machine %*