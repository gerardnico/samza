# Samza Demo Code

## About

This directory contains
  * a samza grid docker image (to work with Samza also on windows)
  * and some samza demo code

## Version

Actual version is Samza 1.4.0

The version of the components (Yarn, Zookeeper, Samza, Kafka) can be seen in the [gradle.properties](gradle.properties) 

## Docker Samza Grid

A Samza grid comprises three different systems: 
  * [YARN](http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/YARN.html), 
  * [Kafka](http://kafka.apache.org/), 
  * and [ZooKeeper](http://zookeeper.apache.org/). 

### Build and create the container
Steps:
  * Build with [Dockerbuild.bat](Dockerbuild.bat)
```dos
Dockerbuild.bat
```
  * Create the container with [docker-samza-run.bat](docker-samza-run.bat)
```dos
docker-samza-run.bat
```

### TCP Connection verification

From your laptop, with [ncat](https://gerardnico.com/network/netcat), you can verify that you have access to the services from your laptop
```dos
ncat -z localhost 22 && echo tcp connection to sshd succeeded
ncat -z localhost 2181 && echo tcp connection to zookeeper succeeded
ncat -z localhost 9092 && echo tcp connection to kafka succeeded
ncat -z localhost 8032 && echo tcp connection to resource manager succeeded
ncat -z localhost 8042 && echo tcp connection to node manager succeeded
```
You should see:
```text
tcp connection to sshd succeeded
tcp connection to zookeeper succeeded
tcp connection to kafka succeeded
tcp connection to resource manager succeeded
tcp connection to node manager succeeded
```

### Connection with bash

  * Connect in the container wit bash and [docker-samza-bash.bat](docker-samza-bash.bat)
```bash
docker-samza-bash.bat
```

### Connection with ssh

You can connect via ssh with the user `root` and the password `welcome`.

### Start and stop service

The software are installed in `/opt/samza/deploy/`

In the container, the [grid](./scripts/grid) script permits to start and stop services. 
```bash
# in order first zookeeper then kafka
grid start zookeeper
grid start kafka
# or all
grid start all
grid
```
```text
 Usage..
  $ grid
  $ grid standalone
  $ grid start [yarn|kafka|zookeeper|all]
  $ grid stop [yarn|kafka|zookeeper|all]
```



## Code

### Word Count
From [Quick Start Word Count Project](http://samza.apache.org/startup/quick-start/latest/samza.html)

  * Create the topic, add data and verify
```bash
kafka-topics.sh --zookeeper localhost:2181 --list
kafka-topics.sh --create --zookeeper localhost:2181 --topic sample-text --partitions 1 --replication-factor 1
kafka-console-producer.sh --topic sample-text --broker-list localhost:9092 < ./data/sample-text.txt
kafka-console-consumer.sh --topic sample-text --bootstrap-server localhost:9092 --from-beginning
```
  * Start the main with the following args:
```bash
--config-path file://D:/code/samza-gh/src/main/config/word-count.properties
```
  * Verify the stream output
```bash
kafka-console-consumer.sh --topic word-count-output --bootstrap-server localhost:9092 --from-beginning
kafka-console-consumer.sh --topic word-count-1-window-count --bootstrap-server localhost:9092 --from-beginning
```

### Hello Samza

```bash
git clone https://gitbox.apache.org/repos/asf/samza-hello-samza.git hello-samza
cd hello-samza
git checkout latest
```

Not yet done
  * [Samza](http://samza.apache.org/learn/tutorials/latest/hello-samza-high-level-yarn.html)
  * [Doc](http://samza.apache.org/startup/code-examples/latest/samza.html)
  * [Hello Samza](http://samza.apache.org/startup/hello-samza/0.10/)
  

### Tutorial

http://samza.apache.org/learn/tutorials/latest/

  