@echo off
call mvn package
copy /y "target\*.jar" "paperTestServer\plugins"
cd ./paperTestServer
java -Xmx1024m -Xms1024m -jar paper.jar nogui