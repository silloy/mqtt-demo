package com.zj;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/3/29
 * Time: 16:28
 * Description: MAIN
 */
public class EClient {

    public static final String HOST = "tcp://192.168.88.18:1883";
    public static final String TOPIC = "toclient";
    private static final String clientid = "client124";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "mpc";
    private String passWord = "123456";
    @SuppressWarnings("unused")
    private ScheduledExecutorService scheduler;

    private void start() {
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            client.setCallback(new EPushCallback());
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//            options.setWill(client.getTopic(TOPIC), "close".getBytes(), 2, true);
            client.connect(options);
            int[] Qos = {0};
            String[] topic1 = {TOPIC};
            client.subscribe(topic1, Qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException {
        EClient client = new EClient();
        client.start();
    }
}
