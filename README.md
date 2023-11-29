# Cronology
A Java Cron Parser
___
## Build:
Cronology is simple to build and run with either gradle or docker
### Gradle:
To create `CronParser/builds/libs/CronParser.jar` run the following from the root of the project

`./gradlew :CronParser:clean :CronParser:build`

### Docker:
To build the docker image run the following from the root of the project

`docker build .`
## Usage:

Cronology takes a single parameter. The first part of the parameter should be your space separated cron. The second part is an optional command. 

To run the jar directly after building, use the following: 

`java -jar Cronparser/build/libs/Cronparser.jar "* * * * * /some/command.sh"`

To run in docker run: 

`docker run YOUR_IMAGE_ID "* * * * * /some/command.sh"`
