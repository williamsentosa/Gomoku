build-server:
	javac src/server/Server.java -cp ./src:./lib -d ./build

run-server:
	java -cp ./build:./src server.Server 6066

build-client:
	javac src/gui/UserInterface.java -cp ./src:./lib/* -Xlint:unchecked

run-client:
	java -cp ./build:./src:./lib/* gui.UserInterface $(filter-out $@,$(MAKECMDGOALS)) 6066
