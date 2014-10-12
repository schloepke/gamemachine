#!/usr/bin/env bash

declare GM_HOME="$(cd "$(cd "$(dirname "$0")"; pwd -P)"/..; pwd)"

jruby -J-cp "$GM_HOME/java/project/lib/*" "$GM_HOME/bin/game_machine" "$@"