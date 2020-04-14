package samzaapp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import joptsimple.OptionSet;
import org.apache.samza.application.StreamApplication;
import org.apache.samza.application.descriptors.StreamApplicationDescriptor;
import org.apache.samza.config.Config;
import org.apache.samza.operators.KV;
import org.apache.samza.operators.MessageStream;
import org.apache.samza.operators.OutputStream;
import org.apache.samza.operators.windows.WindowPane;
import org.apache.samza.operators.windows.Windows;
import org.apache.samza.runtime.ApplicationRunner;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.serializers.IntegerSerde;
import org.apache.samza.serializers.KVSerde;
import org.apache.samza.serializers.StringSerde;
import org.apache.samza.system.kafka.descriptors.KafkaInputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaOutputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaSystemDescriptor;
import org.apache.samza.util.CommandLine;

public class WordCount implements StreamApplication {
    private static final String KAFKA_SYSTEM_NAME = "kafka";
    private static final List<String> KAFKA_CONSUMER_ZK_CONNECT = ImmutableList.of("localhost:2181");
    private static final List<String> KAFKA_PRODUCER_BOOTSTRAP_SERVERS = ImmutableList.of("localhost:9092");
    private static final Map<String, String> KAFKA_DEFAULT_STREAM_CONFIGS = ImmutableMap.of("replication.factor", "1");

    /**
     * Consumer Configs
     */
    //final Map<String, String> CONSUMER_CONFIGS = ImmutableMap.of("socket.timeout.ms", "10000");

    private static final String INPUT_STREAM_ID = "sample-text";
    private static final String OUTPUT_STREAM_ID = "word-count-output-2";

    @Override
    public void describe(StreamApplicationDescriptor streamApplicationDescriptor) {

        /**
         * See
         * https://samza.apache.org/learn/documentation/latest/connectors/kafka
         */
        System.out.println("Kafka");
        KafkaSystemDescriptor kafkaSystemDescriptor = new KafkaSystemDescriptor(KAFKA_SYSTEM_NAME)
                .withConsumerZkConnect(KAFKA_CONSUMER_ZK_CONNECT)
                .withProducerBootstrapServers(KAFKA_PRODUCER_BOOTSTRAP_SERVERS)
                //.withConsumerConfigs(CONSUMER_CONFIGS)
                .withDefaultStreamConfigs(KAFKA_DEFAULT_STREAM_CONFIGS);
        streamApplicationDescriptor.withDefaultSystem(kafkaSystemDescriptor);

        System.out.println("Input Stream");
        KafkaInputDescriptor<KV<String, String>> inputDescriptor = kafkaSystemDescriptor.getInputDescriptor(
                INPUT_STREAM_ID,
                KVSerde.of(new StringSerde(), new StringSerde())

        );
        MessageStream<KV<String, String>> inputLines = streamApplicationDescriptor.getInputStream(inputDescriptor);

        System.out.println("Output Stream");
        KVSerde<String, String> of = KVSerde.of(new StringSerde(), new StringSerde());
        KafkaOutputDescriptor<KV<String, String>> outputDescriptor = kafkaSystemDescriptor
                .getOutputDescriptor(OUTPUT_STREAM_ID, of);
        OutputStream<KV<String, String>> counts = streamApplicationDescriptor.getOutputStream(outputDescriptor);

        System.out.println("Stream Processing");
        inputLines
                .map(kv -> {
                    System.out.println("Line: " + kv.value);
                    return kv.value;
                }) // return the lines
                .flatMap(s -> {
                    List<String> split = Arrays.asList(s.split("\\W+"));
                    System.out.println("Split: " + split);
                    return split;
                }) // Split by space
                .map(s -> {
                    System.out.println(s);
                    return s;
                })
                .window(
                        Windows.keyedSessionWindow(
                                w -> w, // The key
                                // Session Time Gap
                                // A session is considered complete when no new messages arrive within the sessionGap.
                                // All messages that arrive within the gap are grouped into the same session
                                Duration.ofSeconds(60),
                                () -> 0, // initial value
                                (m, prevCount) -> prevCount + 1, // agg function
                                new StringSerde(), // Key Serde Object
                                new IntegerSerde() // Value Serde Object
                        ),
                        "count" // unique id of the operator
                ).map(windowPane -> {
                            System.out.println("Key" + windowPane.getKey().getKey());
                            return KV.of(
                                    windowPane.getKey().getKey(), // Key
                                    windowPane.getKey().getKey() + ": " + windowPane.getMessage().toString() // Value
                            );
                        }
                )
//                .map(s -> {
//                            System.out.println("Key" + s);
//                            return KV.of(s,s);
//                        }
//                )
                .sendTo(counts);

    }

    /**
     * Executes the application using the local application runner.
     * It takes two required command line arguments
     * config-factory: a fully {@link org.apache.samza.config.factories.PropertiesConfigFactory} class name
     * config-path: path to application properties
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        String[] samzaArgs = {
                "--config-path", Paths.get("src/main/config/word-count.properties").toUri().toString(),
                "--config-factory", "org.apache.samza.config.factories.PropertiesConfigFactory"
        };
        CommandLine cmdLine = new CommandLine();
        OptionSet options = cmdLine.parser().parse(samzaArgs);
        Config config = cmdLine.loadConfig(options);
        WordCount app = new WordCount();
        LocalApplicationRunner runner = new LocalApplicationRunner(app, config);
        runner.run();
        runner.waitForFinish();
    }
}
