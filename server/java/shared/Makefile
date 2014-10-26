messages:
	cp ../../config/combined_messages.proto messages.proto
	sed -i 's/GameMachine.Messages/Client.Messages/' messages.proto
	java -jar ../project/local_lib/protostuff-compiler-1.0.8-jarjar.jar protostuff.properties
	rm messages.proto