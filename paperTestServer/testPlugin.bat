@echo off
call mvn clean compile assembly:single
rem call mvn install
copy /y "target\*.jar" "paperTestServer\plugins"
cd ./paperTestServer
java -Xmx1024m -Xms1024m -jar paper.jar nogui