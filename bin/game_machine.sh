#!/bin/bash

CWD=$(pwd)
APP_NAME=game_machine

ERR_FILE=$CWD/log/game_machine.stderr
OUT_FILE=$CWD/log/game_machine.stdout

PID_PATH=$CWD/tmp
PID_FILE=$PID_PATH/game_machine.pid
mkdir -p $PID_PATH
ARGV="$2 $3 $4"

start_daemon() {
if [ -e "$PID_FILE" ]; then
  echo "PID file already exists."
  exit 1
else
  echo "Starting $APP_NAME with args $ARGV."
  sh -c "( ( nohup jruby bin/game_machine \"$ARGV\" 2>> \"$ERR_FILE\" >> \"$OUT_FILE\" < /dev/null) & echo \$! > \"$PID_FILE\")"
  exit $?
fi
}

stop_daemon() {
if [ -e "$PID_FILE" ]; then
  echo "Stopping $APP_NAME."
  PID=`cat $PID_FILE`
  while [ 1 ]; do
    kill -TERM $PID 2> /dev/null
    sleep 1
    [ `ps $PID 2> /dev/null | grep $PID | wc -l` -eq 0 ] && break
  done
  rm $PID_FILE
else
  echo "PID file not found."
  return 1
fi
}

check_status() {
  if [ -e "$PID_FILE" ]; then
    PID=`cat $PID_FILE`
    if [ `ps $PID 2> /dev/null | grep $PID | wc -l` -eq 1 ]; then
      echo "$APP_NAME is running"
      return 0
    fi
  fi

  echo "$APP_NAME is not running"
  return 3
}


case $1 in
  start)
    start_daemon
  ;;
  stop)
    stop_daemon
  ;;
  status)
    check_status
  ;;
  restart)
    stop_daemon
    sleep 1
    start_daemon
  ;;
  *)
    echo "usage: $0 start|stop|restart|status"
    exit 1
  ;;
esac

