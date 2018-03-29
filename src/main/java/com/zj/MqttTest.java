package com.zj;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/3/29
 * Time: 15:35
 * Description: MAIN
 */
public class MqttTest {
    public static void main(String[] args) throws URISyntaxException {
        MQTT mqtt = new MQTT();
        mqtt.setHost("tcp://localhost:5672");
        final CallbackConnection connection = mqtt.callbackConnection();
        connection.listener(new Listener() {

            @Override
            public void onDisconnected() {
                System.out.println("listen disConnect");
            }

            @Override
            public void onConnected() {
                System.out.println("listent connect");
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
                System.out.println("listen publish");
                System.out.println(UTF8Buffer.decode(topic));
                System.out.println(UTF8Buffer.decode(Buffer.utf8(payload)));
                ack.run();
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("listen fail" + value.toString());
            }
        });
        connection.connect(new Callback<Void>() {
            @Override
            public void onFailure(Throwable value) {
                System.out.println("connect fail");
            }

            // Once we connect..
            @Override
            public void onSuccess(Void v) {
                // Subscribe to a topic
                Topic[] topics = {new Topic("mqtt/test/#", QoS.AT_LEAST_ONCE)};
                connection.subscribe(topics, new Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] qoses) {
                        System.out.println("subscribe success");
                        System.out.println(new String(qoses).toString());
                    }

                    @Override
                    public void onFailure(Throwable value) {
                        System.out.println("subscribe fail");
                    }
                });

                // Send a message to a topic
                // connection.publish("foo", "Hello".getBytes(),
                // QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
                // public void onSuccess(Void v) {
                // System.out.println("publish success");
                // }
                //
                // public void onFailure(Throwable value) {
                // System.out.println("publish fail");
                // }
                // });
            }
        });
        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // To disconnect..
        connection.disconnect(new Callback<Void>() {
            @Override
            public void onSuccess(Void v) {
                System.out.println("disconnect success");
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("fail");
            }
        });

    }


    private static void testSendMqtt() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        // factory.setVirtualHost("");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection conn = null;
        Channel channel = null;
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();

            byte[] messageBodyBytes = "{'text':'Hello, world!中文'}".getBytes();
            channel.basicPublish("amq.topic", "mqtt.test.aaa", null, messageBodyBytes);
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}
