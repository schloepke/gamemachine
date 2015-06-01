
@echo off
SET UNITY_HOME=%~dp0

"%UNITY_HOME%/game.exe" -batchmode -nographcis -logFile ./logfile
