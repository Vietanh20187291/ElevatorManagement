import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SimpleMqttClient {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883"; // Thay đổi thành broker của bạn
        String clientId = "JavaClient";
        String topic = "test/topic"; // Chủ đề bạn muốn publish
        int qos = 2;
        String content = "Hello MQTT"; // Nội dung tin nhắn bạn muốn gửi

        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");

            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            System.out.println("Message published");

            client.disconnect();
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
