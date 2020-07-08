#!/bin/sh
# Created Time : 2020-07-03 09:11:56

docker build -t zl/trans:1.0 .

docker run -d -p 10003:8080 -m 700m -e JAVA_OPTS='-Xmx650m -Xms650m' -v /data/project/logs/trans:/usr/local/tomcat/logs --name trans zl/trans:1.0
