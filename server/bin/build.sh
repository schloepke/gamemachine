#!/usr/bin/env bash

export GEM_PATH=.game_machine/vendor/bundle/jruby/1.9
declare GM_HOME="$(cd "$(cd "$(dirname "$0")"; pwd -P)"/..; pwd)"

java -cp "$GM_HOME/java/server/lib/*" -jar bin/jruby-complete-1.7.20.jar "$GM_HOME/bin/game_machine" build clean
