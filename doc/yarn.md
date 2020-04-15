# Yarn

## About

[Doc](https://samza.apache.org/learn/documentation/latest/deployment/yarn.html)

## Packaging 

Samza jobs should be packaged with the following structure.

```txt
samza-job-name-folder
├── bin
│   ├── run-app.sh
│   ├── run-class.sh
│   └── ...
├── config
│   └── application.properties
└── lib
    ├── samza-api-0.14.0.jar
    ├── samza-core_2.11-0.14.0.jar
    ├── samza-kafka_2.11-0.14.0.jar
    ├── samza-yarn_2.11-0.14.0.jar
    └── ...
```

You do it in this project with:

```bash
./gradlew distTar
```
