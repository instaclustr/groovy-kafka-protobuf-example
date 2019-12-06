
@Grab(group='org.apache.kafka', module='kafka-clients', version='2.3.0')
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer

@Grab(group = "com.github.daniel-shuy", module="kafka-protobuf-serde", version = "2.2.0")
import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufDeserializer

import demo.ExampleMessage

import java.time.Duration

Properties props = new Properties()
props.put('zk.connect', 'localhost:2181')
props.put('bootstrap.servers', 'localhost:9092')
props.put('group.id', 'groovy-consumer')

def consumer = new KafkaConsumer<String, ExampleMessage>(props, new StringDeserializer(), new KafkaProtobufDeserializer<>(ExampleMessage.parser()))

consumer.subscribe(Collections.singleton("demo-topic"));

while (true) {
    def records = consumer.poll(Duration.ofMillis(100));

    records.each{ record ->
        println "got message: " + record.value().message
    }

    consumer.commitAsync();
}

consumer.close();