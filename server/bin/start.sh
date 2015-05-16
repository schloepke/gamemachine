#!/usr/bin/env bash

export GEM_PATH=.game_machine/vendor/bundle/jruby/1.9
declare GM_HOME="$(cd "$(cd "$(dirname "$0")"; pwd -P)"/..; pwd)"

MAX_HEAP="300m"
NEW_SIZE="100m"

JAVA_OPTS="-Xmx$MAX_HEAP -Xmn$NEW_SIZE -XX:NewSize=$NEW_SIZE -XX:MaxNewSize=$NEW_SIZE\
 -XX:-UseAdaptiveSizePolicy -XX:+UseParNewGC  -XX:+UseCompressedOops\
 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=32768 -XX:MaxTenuringThreshold=1\
 -XX:SurvivorRatio=190 -XX:TargetSurvivorRatio=90"

java -cp "$GM_HOME/java/server/lib/*" $JAVA_OPTS -jar jruby-complete-1.7.20.jar "$GM_HOME/bin/game_machine" server

