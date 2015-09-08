FROM java:8-jre

ENV HOME /root/
WORKDIR /app/

# dropwizard
ADD src/main/resources/expansible.yml /app/
ADD target/expansible.jar /app/

# run
EXPOSE 8080
ENTRYPOINT [ "java" ]
CMD ["-jar", "expansible.jar", "server", "expansible.yml"]
