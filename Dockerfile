FROM ubuntu:16.04

#################################
# Sys Install
#################################
RUN	echo "==> Update" && \
    apt-get update

#################################
# Env
#################################
ENV JAVA_HOME=/usr/lib/jvm/default-java
ENV SAMZA_HOME /usr/local/samza
ENV WORKDIR /samza
ENV PATH $SAMZA_HOME/deploy/kafka/bin:$SAMZA_HOME/deploy/zookeeper/bin:$SAMZA_HOME/scripts:$WORKDIR/scripts:$PATH
ENV FQDN localhost

#################################
# Sshd
# https://docs.docker.com/engine/examples/running_ssh_service/
#################################
ENV NOTVISIBLE "in users profile"
RUN	echo "==> Samza Grid Script: Install script dependency" && \
    apt-get install -y openssh-server && \
    mkdir /var/run/sshd && \
    echo 'root:welcome' | chpasswd && \
    sed -i 's/PermitRootLogin without-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    # SSH login fix. Otherwise user is kicked off after login &&
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd && \
    echo "export VISIBLE=now" >> /etc/profile


#################################
# Samza Grid Script Install Script
#################################
ADD scripts $SAMZA_HOME/scripts
ADD gradle.properties $SAMZA_HOME
RUN	echo "==> Samza Grid Script: Install script dependency" && \
    apt-get install -y default-jdk && \
    apt-get install -y git && \
    apt-get install -y maven && \
    apt-get install -y curl && \
    chmod +x $SAMZA_HOME/scripts/grid

#################################
# Working Directory
#################################
RUN echo "==> Creating the working directory" && \
    mkdir $WORKDIR
WORKDIR $WORKDIR

#################################
# Port exposed for --net host
#################################
EXPOSE 22
EXPOSE 2181
EXPOSE 9092
EXPOSE 8032
EXPOSE 8042

##################################
## Install Software
## listener kafka property is changed to 0.0.0.0 to answer on all interface and localhost for the client
##################################
#RUN	echo "==> Install Zookeeper and Kafka into ${FQDN}" && \
#    $SAMZA_HOME/scripts/grid install all && \
#    sed -i "s/\#listeners=PLAINTEXT:\/\/:9092/listeners=PLAINTEXT:\/\/0.0.0.0:9092/" $SAMZA_HOME/deploy/kafka/config/server.properties && \
#    sed -i "s/\#advertised.listeners=PLAINTEXT:\/\/your.host.name:9092/advertised.listeners=PLAINTEXT:\/\/localhost:9092/" $SAMZA_HOME/deploy/kafka/config/server.properties

#################################
# Start Container Script
#################################
#ENTRYPOINT /usr/sbin/sshd -D & ; $SAMZA_HOME/scripts/grid start all ; sleep infinity

ENTRYPOINT /usr/sbin/sshd -D

