# Samza Demo Code (Windows)

## About

This directory contains
  * a samza grid docker image (to work with Samza also on windows)
  * and some samza demo code

## Version

Actual version is Samza 1.4.0

The version of the components (Yarn, Zookeeper, Samza, Kafka) can be seen in the [gradle.properties](gradle.properties) 

## Steps

  * Build and create the [docker Samza Grid container](./doc/samza-grid-docker.md)
  * Yarn ui should be available at [http://localhost:8088](http://localhost:8088)
  * Demo:
     * Local: Run the [Word count demo](./doc/demo_word_count.md)
     * Yarn: Run the [Web Session demo](./doc/demo_session_window.md)


## TODO:

  * [Hello Samza](doc/samza-hello.md)
  * [Tutorial](http://samza.apache.org/learn/tutorials/latest/)

  