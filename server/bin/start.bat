@echo off

set GEM_PATH=.game_machine/vendor/bundle/jruby/1.9
set GM_HOME=%~dp0..
set GM_CLASSPATH=%GM_HOME%\java\server\lib\*

set MAX_HEAP=300m
set NEW_SIZE=100m

set JAVA_OPTS=-Xmx%MAX_HEAP% -Xmn%NEW_SIZE% -XX:NewSize=%NEW_SIZE% -XX:MaxNewSize=%NEW_SIZE%^
 -XX:-UseAdaptiveSizePolicy -XX:+UseParNewGC  -XX:+UseCompressedOops^
 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=32768 -XX:MaxTenuringThreshold=1^
 -XX:SurvivorRatio=190 -XX:TargetSurvivorRatio=90

java -cp "%GM_HOME%/java/server/lib/*" %JAVA_OPTS% -jar jruby-complete-1.7.20.jar "%GM_HOME%/bin/game_machine" server