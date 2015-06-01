@echo off
SET UNITY_HOME=%~dp0

"%UNITY_HOME%/game.x86_64" -batchmode -nographcis -logFile ./logfile

