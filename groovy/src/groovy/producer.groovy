@Grab(group='org.apache.kafka', module='kafka-clients', version='2.3.0')
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.Callback

@Grab(group = "com.github.daniel-shuy", module="kafka-protobuf-serde", version = "2.2.0")
import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufSerializer

import demo.ExampleMessage

Properties props = new Properties()
props.put('zk.connect', 'localhost:2181')
props.put('bootstrap.servers', 'localhost:9092')

def producer = new KafkaProducer<String, ExampleMessage>(props, new StringSerializer(), new KafkaProtobufSerializer<>())

args.each { arg ->
    println "sending message $arg"
    def message = ExampleMessage.newBuilder().setMessage(arg).build()

    producer.send(
            new ProducerRecord<String, ExampleMessage>("demo-topic", message),
            { RecordMetadata metadata, Exception e ->
                println "The offset of the record we just sent is: ${metadata.offset()}"
            } as Callback
    )
}

producer.close()