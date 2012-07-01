rm -f gm/game_socket/*.class
rm -f gm/game_socket/GameSocketProtocol.java
protoc gm/game_socket/GameSocketProtocol.proto --java_out=./
javac -classpath netty-3.5.1.Final.jar:protobuf-java-2.4.1.jar gm/game_socket/*.java
jar cf gm.jar gm
ls -l gm/game_socket
java -classpath .:gm.jar:netty-3.5.1.Final.jar:protobuf-java-2.4.1.jar gm.game_socket.GameSocketServer
