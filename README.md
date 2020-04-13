# Samza Demo Code


## Docker

  * Build 
```dos
docker build -t "gerardnico/samza-grid:1.0" .
```
  * Create the container
```dos
docker-samza-run.bat
```

## Management

### Bash

* Connect in the container wit bash
```bash
docker-samza-bash.bat
```

### Ssh

You can connect via ssh with the user `root` and the password `welcome`.

### Installation Directory

The software are installed in `/usr/local/samza/deploy/`

### Grid 
  
  * In the container (in order first zookeeper then kafka)
```bash
grid
```
```text
 Usage..

  $ grid
  $ grid bootstrap
  $ grid standalone
  $ grid install [yarn|kafka|zookeeper|samza|all]
  $ grid start [yarn|kafka|zookeeper|all]
  $ grid stop [yarn|kafka|zookeeper|all]
```

### TCP Connection verification

With ncat, you can verify that you have access to the services from your laptop
```dos
c:\nmap\ncat -z localhost 2181 && echo tcp connection to zookeeper succeeded
c:\nmap\ncat -z localhost 9092 && echo tcp connection to kafka succeeded
```
You should see
```text
tcp connection to zookeeper succeeded
tcp connection to kafka succeeded
```

## Code

### Word Count
From [Quick Start Word Count Project](http://samza.apache.org/startup/quick-start/latest/samza.html)

  * Create the topic, add data and verify
```bash
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
```

### Hello Samza

Not yet done

  * [Hello Samza](http://samza.apache.org/startup/hello-samza/0.10/)
  



  