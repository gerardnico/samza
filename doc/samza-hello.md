# Hello Samza


## Check out

```bash
git clone https://gitbox.apache.org/repos/asf/samza-hello-samza.git hello-samza
cd hello-samza
git checkout latest
```


## Samza Client / Job Package 

By default, `Hello Samza` uses the snapshot version of Samza in its [gradle.properties](../gradle.properties)
 

If you want to use:
   * a stable version: change the version in [gradle.properties](../gradle.properties)
   * it, you need  to publish it to your local Maven repository.

with the [grid](../scripts/grid)
```bash
grid install samza
```
or 
```bash
git clone https://gitbox.apache.org/repos/asf/samza.git
cd samza
./gradlew -PscalaSuffix=${SCALA_VERSION} clean publishToMavenLocal
```


## Doc

  * [Samza](http://samza.apache.org/learn/tutorials/latest/hello-samza-high-level-yarn.html)
  * [Doc](http://samza.apache.org/startup/code-examples/latest/samza.html)
  * [Hello Samza](http://samza.apache.org/startup/hello-samza/0.10/)
