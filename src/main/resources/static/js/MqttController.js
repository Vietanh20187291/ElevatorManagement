function MQTTStartConnect() {
    if (typeof mqttConnection.path == "undefined" || mqttConnection.path == null || mqttConnection.path == "") {
        mqttConnection.path = '/mqtt';
    }
    mqtt = new Paho.MQTT.Client(
        mqttConnection.host,
        mqttConnection.port,
        mqttConnection.path,
        "web_1",
    );

    var options = {
        timeout: 3,
        keepAliveInterval: 60,
        useSSL: useTLS,
        cleanSession: cleansession,
        onSuccess: MQTTOnConnect,
        onFailure: function (message) {
            alertError("Connection failed. Reload website to retry.");

        }
    };


    // alertError("Connected failed: " + message.errorMessage + "Retrying")

    mqtt.onConnectionLost = MQTTOnConnectionLost;
    mqtt.onMessageArrived = MQTTOnMessageArrived;

    if (mqttConnection.username != null) {
        options.userName = mqttConnection.username;
        options.password = mqttConnection.password;
    }
    console.log("Host=");

    console.log("Host=" + mqttConnection.host + ", port=" + mqttConnection.port + ", path=" + mqttConnection.path + " TLS = " + useTLS + " username=" + mqttConnection.username + " password=" + mqttConnection.password);
    mqtt.connect(options);
}

function MQTTOnConnect() {

    for (var i = 0; i < elevatorList.length; i++) {
        var slug = elevatorList[i].topic;
        var subscribetopicindicator = slug + "/elvtopc/indicator";
        mqtt.subscribe(subscribetopicindicator, { qos: 0 });

        var subscribetopiccallcar = slug + "/elvtopc/callcar";
        mqtt.subscribe(subscribetopiccallcar, { qos: 0 });

        var subscribetopiccallup = slug + "/elvtopc/callhallup";
        mqtt.subscribe(subscribetopiccallup, { qos: 0 });

        var subscribetopiccalldown = slug + "/elvtopc/callhalldown";
        mqtt.subscribe(subscribetopiccalldown, { qos: 0 });
    }

    GUIOnConnect();
}

function MQTTOnConnectionLost() {
    GUIOnConnectionLost();
}
// Tạo hàm bọc để truyền thêm topic
function MQTTOnMessageArrived(message) {
    var topic = message.destinationName;
    var payload = message.payloadString;

    for (var i = 0; i < elevatorList.length; i++) {

        var slug = elevatorList[i].topic;
        if (topic == slug + "/elvtopc/indicator") {
            var payloadBytes = message.payloadBytes;

            // Convert payload bytes to hexadecimal string
            var payloadHex = bytesToHex(payloadBytes);
            // Insert space after every two characters
            payloadHex = insertSpaceEveryNChars(payloadHex, 4);
            var info = handleInput(payloadHex.replace(/\s/g, ''),i);
            GUIShowIndicator(info, elevatorList[i].id);
        }
        // if (topic == slugs[i] + "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator") {
        //     var payloadBytes = message.payloadBytes;
        //
        //     // Convert payload bytes to hexadecimal string
        //     var payloadHex = bytesToHex(payloadBytes);
        //     // Insert space after every two characters
        //     payloadHex = insertSpaceEveryNChars(payloadHex, 4);
        //     handleInput(payloadHex.replace(/\s/g, ''));
        // }
        if (topic == slug + "/elvtopc/callcar") {
            var payloadBytes = message.payloadBytes;
            var payloadHex = bytesToHex(payloadBytes);

            var floors = signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2))

            highlightFloors(0, floors)
        }

        if (topic == slug + "/elvtopc/callhallup") {

            var payloadBytes = message.payloadBytes;
            var payloadHex = bytesToHex(payloadBytes);
            var floors = signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2))
            // alert(floors)
            // highlightButtonUpFloors(signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2)))
        }
        if (topic == slug + "/elvtopc/callhalldown") {
            var payloadBytes = message.payloadBytes;
            var payloadHex = bytesToHex(payloadBytes);
            highlightButtonDownFloors(signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2)))
        }
    }

}

function MQTTstartDisconnect() {
    mqtt.disconnect();
}

//Luu y cho nay vì da thay NQTTconect bang startConnect, this line will make a auto connect when lunch app and keep alive to broker
$(document).ready(function () {
    GUIstartConnect();

});

// function attClick(mqtt) {
//     var msg = "att activation";
//     var topic = elevator + "/" + "att";
//     Message = new Paho.MQTT.Message(msg);
//     Message.destinationName = topic;
//     mqtt.send(Message);
//     document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
// }

function MQTTOpenDoor(eleid){
    var elevator = elevatorList.find(elevator => elevator.id === eleid);

    var msg = new Uint8Array([0x01]);
    var topic = elevator.topic +"/pctoelv/status";
    Message = new Paho.MQTT.Message(msg.buffer);
    Message.destinationName = topic;
    mqtt.send(Message);
    alertSuccess("Sent request successfully")
}

function MQTTCloseDoor(eleid) {
    // alert(eleid)
    var elevator = elevatorList.find(elevator => elevator.id === eleid);

    var msg = new Uint8Array([0x02]);
    var topic = elevator.topic+"/pctoelv/status";
    Message = new Paho.MQTT.Message(msg.buffer);
    Message.destinationName = topic;
    mqtt.send(Message);
    alertSuccess("Sent request successfully")
}

function MQTTcarCall(input, eleid) {
    var elevator = elevatorList.find(elevator => elevator.id === eleid);
    var topic = elevator.topic + "/pctoelv/callcar";
    handleCallClick(input,topic);
}

function handleCallClick(input,topic) {
    var value = input;
    var floorList = elevatorList[0].listFloors;
    if (floorList!=null){
        value = getFloorIndex(value, floorList);
    }

    var floors = [];
    floors.push(value)

    var signal = generateElevatorCallSignal(floors);
    // mqtt.publish(topic, signal, {qos: 0});

    let message = new Paho.MQTT.Message(signal);
    message.destinationName = topic;
    mqtt.send(message);

}

function MQTTcallUp(input, eleid) {
    var elevator = elevatorList.find(elevator => elevator.id === eleid);
    var topic = elevator.topic + "/pctoelv/callhallup";
    handleCallClick(input,topic, mqtt);
}


function MQTTcallDn(input, eleid) {
    var elevator = elevatorList.find(elevator => elevator.id === eleid);
    var topic = elevator.topic+"/pctoelv/callhalldown";
    handleCallClick(input,topic);
}

function bytesToHex(bytes) {
    var hexString = "";
    for (var i = 0; i < bytes.length; i++) {
        var byteHex = bytes[i].toString(16);
        if (byteHex.length === 1) {
            byteHex = '0' + byteHex; // Ensure two digits
        }
        hexString += byteHex;
    }
    return hexString;
}

// Function to insert space after every n characters
function insertSpaceEveryNChars(str, n) {
    var result = '';
    for (var i = 0; i < str.length; i += n) {
        result += str.substr(i, n) + ' ';
    }
    return result.trim(); // Remove trailing space
}
function handleInput(input,eleno) {
    var d0 = input.substring(0, 2);
    var d1 = input.substring(2, 4);
    var d2 = input.substring(4, 6);
    var d3 = input.substring(6, 8);
    var d4 = input.substring(8, 10);
    var d5 = input.substring(10, 12);
    var d6 = input.substring(12, 14);
    var d7 = input.substring(14, 16);

    // Convert d1, d2, d3 from hex to decimal
    var floor = convertHex(d0) +convertHex(d1)+convertHex(d2);
    // // Bỏ ký tự 0 ở đâu nếu ký tự thứ 2 là chữ
    // if (floor.length > 1 && floor[0] === '0' && isNaN(floor[1])) {
    //     floor = floor.substring(1);
    // }

    // Xóa tất cả các số 0 ở đầu chuỗi
    floor = floor.replace(/^0+/, '');

    // Nếu chuỗi rỗng (trường hợp toàn bộ chuỗi chỉ chứa các số 0), thì trả về '0'
    if (floor === '') {
        floor = '0';
    }

    // Direction based on d3
    var direction = "";
    if (d3 === "10") {
        direction = "Up";
    } else if (d3 === "11") {
        direction = "Run Up";
    } else if (d3 === "20") {
        direction = "Down";
    } else if (d3 === "21") {
        direction = "Run Down";
    } else {
        direction = "Staying";
    }
    //Status based on d4
    var status = "";
    if (d4.charAt(7) === "1") {
        status = "Parking";
    } else if (d4.charAt(6) === "1") {
        status = "Overload";
    } else if (d4.charAt(5) === "1") {
        status = "Priority";
    } else if (d4.charAt(4) === "1") {
        status = "Fire";
    } else if (d4.charAt(3) === "1") {
        status = "Full";
    } else {
        status = "Available";
    }

    var door = "";
    if (d5 === "04") {
        door = "Open";
    } else if (d5 === "08") {
        door = "Close";
    }

    console.log("Floor: " + floor);
    console.log("Direction: " + direction);
    console.log("Status: " + status);
    console.log("Door: " + door);

    if (d3 === "10") {
        direction = "Up";
    } else if (d3 === "11") {
        direction = "Run Up";
    } else if (d3 === "20") {
        direction = "Down";
    } else if (d3 === "21") {
        direction = "Run Down";
    } else {
        direction = "-";
    }
    var info = {
        floor: floor,
        direction: direction,
        status: status,
        door: door
    };

    // controller_move(1,floor,door)

    return info;


}
function getFloorIndex(floorName, FloorList) {
    const listFloors = FloorList.split('->');
    const index = listFloors.indexOf(floorName);

    if (index !== -1) {
        return index + 1;
    } else {
        alertError('Invalid Floor Input');
        return null;
    }
}
function convertHex(hexInput) {
    var decimalValue = parseInt(hexInput, 16);
    if (decimalValue == 0) {
        // Convert using first method
        var result = 0;
        return result;
    }else if (decimalValue >= 4 && decimalValue <= 44) {
        // Convert using first method
        var result = decimalValue - 4 + 10;
        return result;
    } else {
        // Convert using second method
        var asciiCharacter = String.fromCharCode(decimalValue);
        return asciiCharacter;
    }
}

function generateElevatorCallSignal(floors) {
    let result = generateSignal(floors);

    // alert(result);
// Chuyển đổi chuỗi hex thành mảng các byte
    let byteArray = result.split(' ').map(byte => parseInt(byte, 16));

// Tạo một Uint8Array từ mảng các byte
    let uint8Array = new Uint8Array(byteArray);
    return uint8Array.buffer;
}
function generateSignal(floors) {

    // Tìm số phần tử cần thiết cho signalArray
    var maxFloor = Math.max(...floors);
    var signalArraySize = Math.ceil(maxFloor / 8);
    var signalArray = Array(signalArraySize).fill('00000000');

    // Lặp qua các tầng đã gọi và thiết lập bit tương ứng trong signalArray
    floors.forEach(function (floor) {
        var index = Math.floor((floor - 1) / 8);
        var bitPosition = 7 - ((floor - 1) % 8);
        var binaryString = signalArray[index];
        var updatedBinaryString = binaryString.substring(0, bitPosition) + '1' + binaryString.substring(bitPosition + 1);
        signalArray[index] = updatedBinaryString;
    });

    // Chuyển đổi từng chuỗi nhị phân thành giá trị thập lục phân
    var result = signalArray.map(function (binaryString) {
        return parseInt(binaryString, 2).toString(16).toUpperCase().padStart(2, '0');
    });

    // Điền thêm các phần tử '00' để đảm bảo mảng có độ dài là bội số của 8
    while (result.length < 8) {
        result.push('00');
    }

    return result.join(' ');
}
function signalCalledFloorsToOutput(signalInput) {
    var signalArray = signalInput.split(" ");
    var result = [];

    for (var i = 0; i < signalArray.length; i++) {
        var binaryString = parseInt(signalArray[i], 16).toString(2).padStart(8, '0');
        for (var j = 0; j < binaryString.length; j++) {
            if (binaryString[j] === '1') {
                var floorNumber = (i * 8) + (8 - j);
                result.push(floorNumber);
            }
        }
    }
    return result;
}
// Hàm để bật sáng các nút theo danh sách các tầng
function highlightFloors(elevator_no,floorList) {
    try {
        // Tắt tất cả các nút trước
        $('.choose-floor-button').css({'background': 'white'});

        // Bật sáng các nút trong danh sách floorList
        floorList.forEach(floor_no => {
            set_indoor_floor_switch_state(floor_no, elevator_no, ON);
        });
    } catch (e) {
        console.log(e);
    }
}
function highlightButtonUpFloors(floorList) {
    // alert('highlightButtonUpFloors')
    let elevator_no = 1; // Giả sử là thang máy số 1, bạn có thể điều chỉnh nếu cần

    // Tắt tất cả các nút trước
    $('.choose-up').css({'background': 'white'});

    // Bật sáng các nút trong danh sách floorList
    floorList.forEach(floor_no => {
        var element = document.getElementById('choose-up ' + floor_no);
        element.style.backgroundColor = 'orange';
        // choose_outdoor_witches(floor_no, DIRECTION_UP).css('background', 'orange')
    });
}
function highlightButtonDownFloors(floorList) {
    let elevator_no = 1; // Giả sử là thang máy số 1, bạn có thể điều chỉnh nếu cần

    // Tắt tất cả các nút trước
    $('.choose-up').css({'background': 'white'});

    // Bật sáng các nút trong danh sách floorList
    floorList.forEach(floor_no => {
        pressButtonDown(floor_no)
    });
}

