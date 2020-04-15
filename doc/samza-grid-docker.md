# Samza Grid Container


## About

The docker container has the samza grid installed.

A Samza grid comprises three different systems: 
  * [YARN](http://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-site/YARN.html), 
  * [Kafka](http://kafka.apache.org/), 
  * and [ZooKeeper](http://zookeeper.apache.org/). 

## Build and create the container

Steps:
  * Build with [Dockerbuild.bat](Dockerbuild.bat)
```dos
Dockerbuild.bat
```
  * Create the container with [docker-samza-run.bat](docker-samza-run.bat)
```dos
docker-samza-run.bat
```

## Connection
### Yarn UI 

Yarn UI should be available at [http://localhost:8088/cluster](http://localhost:8088/cluster)
 
where you can view the Samza jobs running at [http://localhost:8088/cluster/apps](http://localhost:8088/cluster/apps)
 

### TCP Connection 

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

### Bash

  * Connect in the container wit bash and [docker-samza-bash.bat](docker-samza-bash.bat)
```bash
docker-samza-bash.bat
```

### Sftp (ssh)

You can connect via ssh with the user `root` and the password `welcome`.

## Start and stop service

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

