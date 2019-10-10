package com.tian.dw.gmallcanal.util

import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

/**
 * @author tian
 * @date 2019-10-9 16:35:06
 */
object MyKafkaSender {
    val props = new Properties()
    // Kafka服务端的主机名和端口号
    props.put("bootstrap.servers", "hadoop102:9092,hadoop103:9092,hadoop104:9092")
    // key序列化
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    // value序列化
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)

    def sendToKafka(topic: String, content: String): Future[RecordMetadata] = {
        producer.send(new ProducerRecord[String, String](topic, content))
    }
}

