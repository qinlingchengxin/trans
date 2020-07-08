FROM tomcat:7.0.90
ENV TZ='Asia/Shanghai'
RUN rm -rf /usr/local/tomcat/webapps/*
ADD trans.war /usr/local/tomcat/webapps/trans.war
