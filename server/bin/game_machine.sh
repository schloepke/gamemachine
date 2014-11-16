#!/usr/bin/env bash

declare GM_HOME="$(cd "$(cd "$(dirname "$0")"; pwd -P)"/..; pwd)"

MAX_HEAP="500m"
NEW_SIZE="300m"

JAVA_OPTS="-J-Xmx$MAX_HEAP -J-Xmn$NEW_SIZE -J-XX:NewSize=$NEW_SIZE -J-XX:MaxNewSize=$NEW_SIZE\
 -J-XX:-UseAdaptiveSizePolicy -J-XX:+UseParNewGC  -J-XX:+UseCompressedOops\
 -J-XX:+UnlockDiagnosticVMOptions -J-XX:ParGCCardsPerStrideChunk=32768 -J-XX:MaxTenuringThreshold=1\
 -J-XX:SurvivorRatio=190 -J-XX:TargetSurvivorRatio=90"

jruby -J-cp "$GM_HOME/java/server/lib/*" $JAVA_OPTS "$GM_HOME/bin/game_machine" "$@"