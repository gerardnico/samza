# Word Count Demo (Quick Start)


## About

The `word count demo` is based on the [Quick Start Word Count Project](http://samza.apache.org/startup/quick-start/latest/samza.html)

## Prerequisites

[A running grid in a docker container](samza-grid-docker.md)

## Steps

  * Connect to the container with [docker-samza-bash.bat](../docker-samza-bash.bat)

```bash
# from dos
docker-samza-bash.bat
# fro Git Bash
docker-samza-bash.sh
```

  * Create the topic, add data and verify
```bash
kafka-topics.sh --zookeeper localhost:2181 --list
kafka-topics.sh --create --zookeeper localhost:2181 --topic sample-text --partitions 1 --replication-factor 1
kafka-console-producer.sh --topic sample-text --broker-list localhost:9092 < ./data/sample-text.txt
kafka-console-consumer.sh --topic sample-text --bootstrap-server localhost:9092 --from-beginning
```
  * Start the `main` method of [WordCount.java](../src/main/java/samzaapp/WordCount.java). You should get the word count as output
```txt
Verus: 1
gentle: 1
refrain: 1
fame: 1
memory: 1
begot: 1
shamefastne
....
```
  * Verify the stream output in Kafka
```bash
kafka-console-consumer.sh --topic word-count-output --bootstrap-server localhost:9092 --from-beginning
```
