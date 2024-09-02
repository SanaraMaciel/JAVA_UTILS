FROM maven:3.6.3-openjdk-11-slim AS MAVEN_BUILD 

COPY pom.xml /build/ 
COPY src /build/src/ 

WORKDIR /build/ 
RUN mvn -q package -DskipTests

FROM openjdk:11-jdk-slim 

# Configura o locale para pt_BR.UTF-8
RUN apt-get update && apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
	&& localedef -i pt_BR -c -f UTF-8 -A /usr/share/locale/locale.alias pt_BR.UTF-8
ENV LANG pt_BR.UTF-8
ENV LC_ALL pt_BR.UTF-8
ENV LANGUAGE pt_BR.UTF-8

WORKDIR /app 

COPY --from=MAVEN_BUILD /build/target/Integration-viacep*.jar /app/ 

ENTRYPOINT [ "sh", "-c", "java -jar -DINTEGRATION_DB_HOME=/opt/pcri -DAPP_LOG_ROOT=/opt/pcri/viacep/logs -Dserver.port=8080 -Xms512m -Xmx2048m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Djava.net.preferIPv4Stack=true -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -XX:+UseG1GC -Duser.Timezone=GMT-3 -Duser.language=pt -Duser.country=BR -Xrunjdwp:transport=dt_socket,address=8793,server=y,suspend=n Integration-viacep*.jar" ]