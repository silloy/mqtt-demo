package com.zj;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/3/29
 * Time: 16:01
 * Description: MAIN
 */

import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import sun.awt.windows.ThemeReader;

import java.net.URISyntaxException;

/**
 * fusesource提供三种方式实现发布消息的方式：
 1.采用阻塞式的连接的（BlockingConnection）
 2.采用回调式的连接 （CallbackConnection）
 3.采用Future样式的连接（FutureConnection）

 * 采用Future模式 订阅主题为例
 */
public class MQTTFutureClient {
    private final static String CONNECTION_STRING = "tcp://192.168.88.18:1883";
    private final static boolean CLEAN_START = true;
    private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
    private final static String CLIENT_ID = "client";
    public static Topic[] topics = {
            new Topic("mqtt/aaa", QoS.EXACTLY_ONCE),
            new Topic("mqtt/bbb", QoS.AT_LEAST_ONCE),
            new Topic("mqtt/ccc", QoS.AT_MOST_ONCE) };

    public final static long RECONNECTION_ATTEMPT_MAX = 6;
    public final static long RECONNECTION_DELAY = 2000;

    public final static int SEND_BUFFER_SIZE = 2 * 1024 * 1024;// 发送最大缓冲为2M

    public static void main(String[] args) {
        // 创建MQTT对象
        MQTT mqtt = new MQTT();
        try {
            // 设置mqtt broker的ip和端口
            mqtt.setHost(CONNECTION_STRING);
            // 连接前清空会话信息
            mqtt.setCleanSession(CLEAN_START);
            // 设置重新连接的次数
            mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
            // 设置重连的间隔时间
            mqtt.setReconnectDelay(RECONNECTION_DELAY);
            // 设置心跳时间
            mqtt.setKeepAlive(KEEP_ALIVE);
            // 设置缓冲的大小
            mqtt.setSendBufferSize(SEND_BUFFER_SIZE);
            //设置客户端id
            mqtt.setClientId(CLIENT_ID);

            mqtt.setUserName("mpc");
            //服务器认证密码
            mqtt.setPassword("123456");

            // 获取mqtt的连接对象BlockingConnection
            final FutureConnection connection = mqtt.futureConnection();
            connection.connect();
            connection.subscribe(topics);
            while (true) {
                Thread.sleep(3000L);
                Future<Message> futrueMessage = connection.receive();
                Message message = futrueMessage.await();
                System.out.println("MQTTFutureClient.Receive Message " + "Topic Title :" + message.getTopic() + " context :"
                        + String.valueOf(message.getPayloadBuffer()));
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
        }
    }
}
