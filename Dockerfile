FROM adoptopenjdk/openjdk11

ARG USER_ID
ARG GROUP_ID

ENV TZ=Asia/Jakarta
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#RUN addgroup --gid ${USER_ID} user
#RUN adduser --uid ${USER_ID} --gid ${GROUP_ID} user
#USER user
VOLUME /shg-crm/log

# ADD ./logback.xml /logback.xml
# ADD ./application.yml /application.yml
# ADD *jar dbs-module-rbimanagement.jar

ENV JAVA_OPTS=""
COPY target/dbs-module-account-management-1.0.0.jar /dbs-module-account.jar

ENTRYPOINT ["java","-jar","-Xms64M", "-Xmx2G", "/dbs-module-account.jar"]
EXPOSE 8903