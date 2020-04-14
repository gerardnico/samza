# Samza Demo Code

## About

This directory contains
  * a samza grid docker image (to work also on windows)
  * samza demo code

## Version

Version of the components (Yarn, Zookeeper, )can be seen in the [gradle.properties](gradle.properties) 

## Docker Samza Grid

A Samza grid comprises three different systems: 
  * [YARN](http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/YARN.html), 
  * [Kafka](http://kafka.apache.org/), 
  * and [ZooKeeper](http://zookeeper.apache.org/). 

### Build and create the container
Steps:
  * Build 
```dos
docker build -t "gerardnico/samza-grid:1.0" .
```
  * Create the container
```dos
docker-samza-run.bat
```

### TCP Connection verification

From your laptop, with [ncat](https://gerardnico.com/network/netcat), you can verify that you have access to the services from your laptop
```dos
ncat -z localhost 2181 && echo tcp connection to zookeeper succeeded
ncat -z localhost 9092 && echo tcp connection to kafka succeeded
ncat -z localhost 22 && echo tcp connection to sshd succeeded
```
You should see
```text
tcp connection to zookeeper succeeded
tcp connection to kafka succeeded
tcp connection to sshd succeeded
```

### Connection with bash

  * Connect in the container wit bash
```bash
docker-samza-bash.bat
```

### Connection with ssh

You can connect via ssh with the user `root` and the password `welcome`.

### Start and stop service

The software are installed in `/usr/local/samza/deploy/`

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
kafka-topics.sh --create --zookeeper localhost:2181 --topic sample-text --partition 1 --replication-factor 1
kafka-console-producer.sh --topic sample-text --broker localhost:9092 < ./data/sample-text.txt
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

Not yet done

  * [Doc](http://samza.apache.org/startup/code-examples/latest/samza.html)
  * [Hello Samza](http://samza.apache.org/startup/hello-samza/0.10/)
  



  