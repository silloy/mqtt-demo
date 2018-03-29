package com.zj.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {
    public static void main(String[] args) {

        //定义一个主题
//    public static final String TOPIC = "pos_message_all";
        String topic = "uber/drivers/3/1";
        String content = "Message from MqttPublishSample";
        int qos = 2;
        String broker = "tcp://60.205.114.177:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        String userName = "admin";    //非必须
        String passWord = "duduadmin";  //非必须
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(userName);
            connOpts.setPassword(passWord.toCharArray());
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
//            sampleClient.
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
//            sampleClient,
            System.out.println("Message published");
            //接受消息
            MqttTopic mqttTopic = sampleClient.getTopic(topic);
            System.out.println(mqttTopic.getName());
            //
//           sampleClient.subscribe(topic);

            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
