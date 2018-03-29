package com.zj.mqtt;
import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 模拟一个客户端接收消息
 * @author rao
 *
 */
public class ClientMQTT {

    public static final String HOST = "tcp://192.168.88.18:1883";
    public static final String TOPIC1 = "toclient";
    private static final String clientid = "client11";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "mpc";    //非必须
    private String passWord = "123456";  //非必须
    @SuppressWarnings("unused")
    private ScheduledExecutorService scheduler;

    private void start() {
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(100);
            options.setKeepAliveInterval(20);
            client.setCallback(new PushCallback());
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//遗嘱        options.setWill(topic, "close".getBytes(), 2, true);
            client.connect(options);
            int[] Qos  = {0};
            String[] topic1 = {TOPIC1};
            client.subscribe(topic1, Qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException {
        ClientMQTT client = new ClientMQTT();
        client.start();
    }
}