cls
del server.exe
mcs -pkg:dotnet -r:NLog.dll -r:protobuf-net.dll -out:server.exe *.cs
