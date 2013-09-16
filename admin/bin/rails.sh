#!/bin/bash
SCRIPT_PATH=`dirname "$0"`; SCRIPT_PATH=`eval "cd \"$SCRIPT_PATH\" && pwd"`
sleep 4
cd $SCRIPT_PATH/../
unset GEM_HOME
unset BUNDLE_GEMFILE
unset GEM_PATH
CWD=$(pwd)
APP_NAME=rails


PID_PATH=$SCRIPT_PATH/../tmp/pids
PID_FILE=$PID_PATH/server.pid
mkdir -p $PID_PATH
ARGV="$2 $3 $4"

publish_game_data() {
  rake game_machine:publish
}

update_game_server() {
  rake game_machine:server:update
}

migrate_db() {
  rake db:migrate
}

start_daemon() {
migrate_db
if [ -e "$PID_FILE" ]; then
  echo "PID file already exists."
  exit 1
else
  echo "Starting $APP_NAME with args $ARGV."
  jruby bin/rails server -e local &
  exit $?
fi
}

stop_daemon() {
if [ -e "$PID_FILE" ]; then
  echo "Stopping $APP_NAME."
  PID=`cat $PID_FILE`
  for i in {1..10} ; do
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
    else
      echo "$APP_NAME is not running (stale pidfile)"
      return 3
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
  migrate_db)
    migrate_db
  ;;
  publish_game_data)
    publish_game_data
  ;;
  update_game_server)
    update_game_server
  ;;
  *)
    echo "usage: $0 start|stop|restart|status|migrate_db|publish_game_data|update_game_server"
    exit 1
  ;;
esac

