FROM docker.elastic.co/beats/filebeat:7.17.6

COPY --chown=root:filebeat filebeatTest.yml /usr/share/filebeat/filebeat.yml

USER root
RUN chmod go-w /usr/share/filebeat/filebeat.yml