
@echo off

set UNITY_HOME=%~dp0

echo %UNITY_HOME%

"%UNITY_HOME%..\unity\unityServer.exe" -batchmode -nographics -logFile ./logfile 
