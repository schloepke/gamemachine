#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR/unity

./unityServer -batchmode -nographics -logFile ./logfile
