FROM adoptopenjdk/openjdk11:alpine

COPY target/app.jar /app/
RUN chmod -R ag+w /app

EXPOSE 8080

CMD echo "${JAVA_OPTS}"
CMD java -jar ${JAVA_OPTS} /app/app.jar --spring.profiles.active=nonprod

USER 1001
