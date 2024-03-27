import paho.mqtt.client as mqtt
import ssl

# Thiết lập thông tin kết nối
host = "tkevn.ddns.ne"
port = 8001
username = "user1"
password = "minh"
topic = "practical.html:63"
tls = False
client_id = "client_id"  # Đặt client ID tùy ý

# Thiết lập đường dẫn đến các tệp chứng chỉ
root_ca_path = "rootCA.crt"
client_cert_path = "client.crt"
client_key_path = "client.key"

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    # Subscribe đến topic sau khi kết nối thành công
    client.subscribe(topic)

def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))

client = mqtt.Client(client_id=client_id)

if tls:
    client.tls_set(ca_certs=root_ca_path, certfile=client_cert_path, keyfile=client_key_path, cert_reqs=ssl.CERT_NONE, tls_version=ssl.PROTOCOL_TLSv1_2)

client.username_pw_set(username, password)
client.on_connect = on_connect
client.on_message = on_message

client.connect(host, port, 60)

# Gửi lệnh "Hello" lên topic
client.publish(topic, "Hello")

# Lắng nghe các tin nhắn từ topic (sử dụng một vòng lặp vô hạn)
client.loop_forever()
