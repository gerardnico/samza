/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package samzaapp;

import java.io.Serializable;

import joptsimple.OptionSet;
import org.apache.samza.application.StreamApplication;
import org.apache.samza.application.descriptors.StreamApplicationDescriptor;
import org.apache.samza.config.Config;
import org.apache.samza.operators.KV;
import org.apache.samza.operators.MessageStream;
import org.apache.samza.operators.OutputStream;
import org.apache.samza.operators.windows.Windows;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.serializers.JsonSerdeV2;
import org.apache.samza.serializers.KVSerde;
import org.apache.samza.serializers.Serde;
import org.apache.samza.serializers.StringSerde;
import org.apache.samza.system.kafka.descriptors.KafkaInputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaOutputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaSystemDescriptor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.samza.util.CommandLine;


import samzaapp.data.PageView;
import samzaapp.data.UserPageViews;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * In this example, we group page views by userId into sessions, and compute the number of page views for each user
 * session. A session is considered closed when there is no user activity for a 10 second duration.
 *
 * <p>Concepts covered: Using session windows to group data in a stream, Re-partitioning a stream.
 * <p>
 * To run the below example:
 *
 * <ol>
 *   <li>
 *     Ensure that the topic "pageview-session-input" is created  <br/>
 *     kafka-topics.sh --zookeeper localhost:2181 --create --topic pageview-session-input --partitions 2 --replication-factor 1
 *   </li>
 *   <li>
 *     Run the application using the run-app.sh script <br/>
 *     ./deploy/samza/bin/run-app.sh --config-factory=org.apache.samza.config.factories.PropertiesConfigFactory --config-path=file://$PWD/deploy/samza/config/yarn-session-window-example.properties
 *   </li>
 *   <li>
 *     Produce some messages to the "pageview-session-input" topic <br/>
 *     kafka-console-producer.sh --topic pageview-session-input --broker-list localhost:9092 < ./data/pageview-session-input.jsonl
 *
 *   </li>
 *   <li>
 *     Consume messages from the "pageview-session-output" topic (e.g. bin/kafka-console-consumer.sh)
 *     kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic pageview-session-output --property print.key=true
 *   </li>
 * </ol>
 */
public class SessionWindowExample implements StreamApplication, Serializable {
    private static final String KAFKA_SYSTEM_NAME = "kafka";
    private static final List<String> KAFKA_CONSUMER_ZK_CONNECT = ImmutableList.of("localhost:2181");
    private static final List<String> KAFKA_PRODUCER_BOOTSTRAP_SERVERS = ImmutableList.of("localhost:9092");
    private static final Map<String, String> KAFKA_DEFAULT_STREAM_CONFIGS = ImmutableMap.of("replication.factor", "1");

    private static final String INPUT_STREAM_ID = "pageview-session-input";
    private static final String OUTPUT_STREAM_ID = "pageview-session-output";

    @Override
    public void describe(StreamApplicationDescriptor streamApplicationDescriptor) {

        System.out.println("Debug Trace: The session window example has started");

        Serde<String> stringSerde = new StringSerde();
        KVSerde<String, PageView> pageViewKVSerde = KVSerde.of(stringSerde, new JsonSerdeV2<>(PageView.class));
        KVSerde<String, UserPageViews> userPageViewSerde = KVSerde.of(stringSerde, new JsonSerdeV2<>(UserPageViews.class));

        KafkaSystemDescriptor kafkaSystemDescriptor = new KafkaSystemDescriptor(KAFKA_SYSTEM_NAME)
                .withConsumerZkConnect(KAFKA_CONSUMER_ZK_CONNECT)
                .withProducerBootstrapServers(KAFKA_PRODUCER_BOOTSTRAP_SERVERS)
                .withDefaultStreamConfigs(KAFKA_DEFAULT_STREAM_CONFIGS);

        KafkaInputDescriptor<KV<String, PageView>> pageViewInputDescriptor =
                kafkaSystemDescriptor.getInputDescriptor(INPUT_STREAM_ID, pageViewKVSerde);
        KafkaOutputDescriptor<KV<String, UserPageViews>> userPageViewsOutputDescriptor =
                kafkaSystemDescriptor.getOutputDescriptor(OUTPUT_STREAM_ID, userPageViewSerde);

        streamApplicationDescriptor.withDefaultSystem(kafkaSystemDescriptor);

        MessageStream<KV<String, PageView>> pageViews = streamApplicationDescriptor.getInputStream(pageViewInputDescriptor);
        OutputStream<KV<String, UserPageViews>> userPageViews = streamApplicationDescriptor.getOutputStream(userPageViewsOutputDescriptor);

        pageViews
                .partitionBy(
                        kv -> kv.value.userId,
                        kv -> kv.value,
                        pageViewKVSerde,
                        "pageview")
                .window(
                        Windows.keyedSessionWindow(
                                kv -> kv.value.userId,
                                Duration.ofSeconds(10),
                                stringSerde,
                                pageViewKVSerde
                        ),
                        "usersession")
                .map(windowPane -> {
                    String userId = windowPane.getKey().getKey();
                    int views = windowPane.getMessage().size();
                    System.out.println("UserId/Views: " + userId + "/" + views);
                    return KV.of(userId, new UserPageViews(userId, views));
                })
                .sendTo(userPageViews);
    }

    public static void main(String[] args) {

        String[] samzaArg = {
                "--config-path", Paths.get("src/main/config/local-session-window-example.properties").toUri().toString(),
                "--config-factory", "org.apache.samza.config.factories.PropertiesConfigFactory"
        };
        CommandLine cmdLine = new CommandLine();
        OptionSet options = cmdLine.parser().parse(samzaArg);
        Config config = cmdLine.loadConfig(options);
        SessionWindowExample app = new SessionWindowExample();
        LocalApplicationRunner runner = new LocalApplicationRunner(app, config);
        runner.run();
        runner.waitForFinish();

    }
}
