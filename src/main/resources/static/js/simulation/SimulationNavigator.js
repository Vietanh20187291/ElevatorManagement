var host = 'tkevn.ddns.net';
var port = 8001;
var username = 'user1';
var password = 'minh';
var useTLS = false;
cleansession = true;
var mqtt;
var reconnectTimeout = 2000;
var elevator = document.getElementById("elevator").value;
var numFloors = parseInt(document.getElementById("numFloors").value);
console.log(elevator);

function startConnect() {
    if (typeof path == "undefined") {
        path = '/mqtt';
    }
    mqtt = new Paho.MQTT.Client(
        host,
        port,
        path,
        "web_1",
        // "" + parseInt(Math.random() * 100, 10),
    );

    var options = {
        timeout: 3,
        keepAliveInterval: 60,
        useSSL: useTLS,
        cleanSession: cleansession,
        onSuccess: onConnect,
        onFailure: function (message) {
            alertError("Connection failed: "+message.errorMessage);
            setTimeout(MQTTconnect, reconnectTimeout);
        }
    };


    // alertError("Connected failed: " + message.errorMessage + "Retrying")

    mqtt.onConnectionLost = onConnectionLost;
    mqtt.onMessageArrived = onMessageArrived;

    if (username != null) {
        options.userName = username;
        options.password = password;
    }
    console.log("Host=");

    console.log("Host=" + host + ", port=" + port + ", path=" + path + " TLS = " + useTLS + " username=" + username + " password=" + password);
    mqtt.connect(options);
}

function onConnect() {
    $('#connectStatus').val('Connected');//status "+ host + ':' + port + path"
    var subscribetopicindicator = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator";
    mqtt.subscribe(subscribetopicindicator, {qos: 0});
    alertSuccess("Connected successfully");
}

function onConnectionLost(response) {
    setTimeout(MQTTconnect, reconnectTimeout);
    $('#connectStatus').val("connection lost");
    alertError("Connection lost");

}

function onMessageArrived(message) {
    // alert("onMessageArrived")
    var topic = message.destinationName;
    var payload = message.payloadString;


    // Alert the formatted hexadecimal payload
    // alert("Hexadecimal Payload: " + payloadHex);
    if (topic ==  "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator"){
        var payloadBytes = message.payloadBytes;

        // Convert payload bytes to hexadecimal string
        var payloadHex = bytesToHex(payloadBytes);
        // Insert space after every two characters
        payloadHex = insertSpaceEveryNChars(payloadHex, 4);
        handleInput(payloadHex.replace(/\s/g, ''));
    }

}

function startDisconnect() {
    $('#connectStatus').val('Disconnected');
    mqtt.disconnect();


}

//Luu y cho nay vì da thay NQTTconect bang startConnect, this line will make a auto connect when lunch app and keep alive to broker
$(document).ready(function () {
    startConnect();
});

function attClick() {
    var msg = "att activation";
    var topic = elevator + "/" + "att";
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function opendoor(){
    var msg = "door is opened";
    // var topic = document.getElementById("elevator").value +"/"+ "dooropened";
    var topic = "/abc";
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic "+topic+ " with value = " +msg + " is sent </span><br>";
}

function closedoor() {
    var msg = "door is closed";
    var topic = elevator + "/" + "doorclosed";
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function carcallClick(input) {
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/callcar";
    handleCallClick(input,topic);

}

function handleCallClick(input,topic) {
    try {
        var value = parseInt(input);
    }catch (e) {
        alertError("Invalid type of input")
        return;
    }
    if (isNaN(value) || value < 0 || value > numFloors) {
        alertError("Invalid Floor Number")
        return;
    }

    var floors = [];
    floors.push(value)

    var signal = generateElevatorCallSignal(floors);
    // mqtt.publish(topic, signal, {qos: 0});

    let message = new Paho.MQTT.Message(signal);
    message.destinationName = topic;
    mqtt.send(message);
}

function callupClick(input) {
    // alert(input)
    // var msg = document.getElementById("callup").value;
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/callhallup";

    handleCallClick(msg,topic);
}


function calldnClick() {
    var msg = document.getElementById("calldn").value;
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/callhalldown";

    handleCallClick(msg,topic);
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
function handleInput(input) {
    // var input = document.getElementById("input").value;

    // // Check if input is valid
    // if (input.length !== 10) {
    //     alert("Invalid input length. Please enter a 12-character input.");
    //     return;
    // }
    // alert("Input: " + input);
    // Extracting d0, d1, d2, d3, d4 from input
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
        direction = "-";
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
        status = "Unknown";
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
    $('#display').val(floor);

    if(direction != "Unknown") {
        $('#direction').val(direction);
    }
    $('#onoff').val(status);
    // alert("d0: " + d0 + "\n" +
    //     "d1: " + d1 + "\n" +
    //     "d2: " + d2 + "\n" +
    //     "d3: " + d3 + " (" + direction + ")\n" +
    //     "d4: " + d4 + "\n\n" +
    //     "Floor: " + floor + "\n" +
    //     "Direction: " + direction + "\n" +
    //     "Status: " + status);
    controller_move(1,floor)


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
    // Tìm số phần tử cần thiết cho signalArray
    var maxFloor = Math.max(...floors);
    var signalArraySize = Math.ceil(maxFloor / 8);
    var signalArray = Array(signalArraySize).fill('00000000');

    // Lặp qua các tầng đã gọi và thiết lập bit tương ứng trong signalArray
    floors.forEach(function(floor) {
        var index = Math.floor((floor - 1) / 8);
        var bitPosition = 7 - ((floor - 1) % 8);
        var binaryString = signalArray[index];
        var updatedBinaryString = binaryString.substring(0, bitPosition) + '1' + binaryString.substring(bitPosition + 1);
        signalArray[index] = updatedBinaryString;
    });

    // Chuyển đổi từng chuỗi nhị phân thành giá trị thập lục phân
    var result = signalArray.map(function(binaryString) {
        return parseInt(binaryString, 2).toString(16).toUpperCase().padStart(2, '0');
    });

    // Điền thêm các phần tử '00' để đảm bảo mảng có độ dài là bội số của 8
    while (result.length < 8) {
        result.push('00');
    }
// Chuyển đổi chuỗi hex thành mảng các byte
    let byteArray = signal.split(' ').map(byte => parseInt(byte, 16));

// Tạo một Uint8Array từ mảng các byte
    let uint8Array = new Uint8Array(byteArray);

    return uint8Array.buffer;
}