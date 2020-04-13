FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y default-jdk

ENV JAVA_HOME=/usr/lib/jvm/default-java
ENV SAMZA_HOME /usr/local/samza

RUN	echo "==> Install Samza Grid script dependency" && \
    apt-get install -y git && \
    apt-get install -y maven && \
    apt-get install -y curl

ADD scripts $SAMZA_HOME/scripts

RUN	echo "==> Make Script executable" && \
    chmod +x $SAMZA_HOME/scripts/grid && \
    chmod +x $SAMZA_HOME/scripts/produce-text-data.sh

RUN	echo "==> Install" && \
    $SAMZA_HOME/scripts/grid install zookeeper && \
    $SAMZA_HOME/scripts/grid install kafka


