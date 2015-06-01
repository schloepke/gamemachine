#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR

./game.x86_64 -batchmode -nographcis -logFile ./logfile
