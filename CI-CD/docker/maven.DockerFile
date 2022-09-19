FROM openjdk:11-jre-slim

ENV MODULE="AOMIF"
#BASH is set as default shell
SHELL ["/bin/bash", "-c"]
#ENV is available at image!
ENV FCBS_ROOT="/FCBS"
ENV aomifMinMemory="2048m"
ENV aomifMaxMemory="4096m"

RUN mkdir $FCBS_ROOT
WORKDIR $FCBS_ROOT

#https://towardsdatascience.com/fast-docker-builds-with-caching-for-python-533ddc3b0057
#order is very important and should be less changing to most changing and big size to little
#to use docker caching
COPY thirdparty  lib/thirdparty
COPY bin         bin
COPY config      config
COPY COMMON/internal/COMMON/ocs-schema-*.jar lib/internal/ocs-schema.jar
COPY internal    lib/internal

COPY profile.sh .
RUN echo -e '. $FCBS_ROOT/profile.sh' >> /root/.bashrc

#https://stackoverflow.com/questions/38024160/how-to-get-etc-profile-to-run-automatically-in-alpine-docker
#https://www.baeldung.com/ops/dockerfile-env-variable
EXPOSE 8181

WORKDIR $FCBS_ROOT/bin/$MODULE

#if BASH wasn't your default shell so
#CMD /bin/bash -c "source /root/.bashrc && /workspace/launch.sh"
CMD source /root/.bashrc && ./start_fcbs_aomif.sh
#ENTRYPOINT ["/bin/AOMIF/start_fcbs_aomif.sh"]

#-rw-rw-r-- 1 root root  222 Aug 23 19:01 stop_fcbs_aomif.sh
 #-rw-rw-r-- 1 root root 2568 Aug 23 19:01 start_fcbs_aomif.sh
#WORKDIR Container folder'i... ve dockerFile ust folder'lara erisemiyoruz
#docker build -f ../CI-CD/docker/DockerFile .
#image'in icine bakmak icin: docker run --rm -it --entrypoint=/bin/bash aom
#$ docker run --env-file .configProfile.sh ubuntu bash     --ip=ile docker network'unden ip verebiliyoruz
#RUN freeze the state entryPoint every time starts