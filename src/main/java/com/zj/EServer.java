package com.zj;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/3/29
 * Time: 16:25
 * Description: MAIN
 */
public class EServer {


//    public static final String HOST = "tcp://60.205.114.177:1883";
    public static final String HOST = "tcp://127.0.0.1:1883";
    public static final String TOPIC = "toclient";
    public static final String TOPIC125 = "toclient/125";


    private MqttClient client;
    private MqttTopic topic;
//    private String userName = "admin";
//    private String passWord = "duduadmin";

    private MqttMessage message;

    public EServer() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, MqttClient.generateClientId(), new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
//        options.setUserName(userName);
//        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new EPushCallback());
            client.connect(options);
            topic = client.getTopic(TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }

    public static void main(String[] args) throws MqttException {
        EServer server = new EServer();

        server.message = new MqttMessage();
        server.message.setQos(1);   // 保证消息能到达一次
        server.message.setRetained(true);
//        server.message.setPayload("给客户端124推送的信息".getBytes());

        int i = 0;
        while (true) {
            try {
                Thread.sleep(2000L);
                server.message.setPayload(("send: " + i).getBytes());
                i++;
                server.publish(server.topic , server.message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println(server.message.isRetained() + "------ratained状态");
    }
}
