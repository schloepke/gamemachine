@ECHO OFF
IF NOT "%~f0" == "~f0" GOTO :WinNT
@"C:\Users\chris\Documents\GitHub\game_machine\server\jruby\bin\jruby.exe" "C:/Users/chris/Documents/GitHub/game_machine/server/jruby/bin/bundle" %1 %2 %3 %4 %5 %6 %7 %8 %9
GOTO :EOF
:WinNT
@"C:\Users\chris\Documents\GitHub\game_machine\server\jruby\bin\jruby.exe" "%~dpn0" %*
