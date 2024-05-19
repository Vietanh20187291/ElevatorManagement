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

        //keyPath: 'client.key',
        //certPath: 'client.crt',
        //rejectUnauthorized : false,
        //The CA list will be used to determine if server is authorized
        //protocol: 'wss',
        // protocolId: 'MQTT',
        cleanSession: cleansession,
        onSuccess: onConnect,
        // onSuccess: alert("Connected"),
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

    // Connection succeeded; subscribe to lift of topic:display
    // for (var i = 0; i < 8; i++) {

    var subscribetopicdisplay = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator";
    mqtt.subscribe(subscribetopicdisplay, {qos: 0});
    var subscribetopiccarcall = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/carcall";
    mqtt.subscribe(subscribetopiccarcall, {qos: 0});
    // console.log(subscribetopicdisplay)
    // alert(subscribetopicdisplay)

    // var topic ="Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator";
    // mqtt.subscribe(topic, {qos: 0});
    // alert("Subscribed to topic: " + topic);


    var subscribetopicdisplay = elevator + "/display";
    mqtt.subscribe(subscribetopicdisplay, {qos: 0});
    console.log(subscribetopicdisplay)
    $('#subscribetopic').val(subscribetopicdisplay);
    var subscribetopicdoorstatus = elevator + "/doorstatus";
    mqtt.subscribe(subscribetopicdoorstatus, {qos: 0});
    console.log(subscribetopicdisplay)
    $('#subscribetopicdisplay').val(subscribetopicdoorstatus);
    var subscribetopicdirection = elevator + "/direction";
    mqtt.subscribe(subscribetopicdirection, {qos: 0});
    $('#subscribetopic').val(subscribetopicdirection);
    var subscribetopiconoff = elevator + "/onoff";
    mqtt.subscribe(subscribetopiconoff, {qos: 0});
    $('#subscribetopic').val(subscribetopiconoff);
    var subscribetopicliftstatus = elevator + "/liftstatus";
    mqtt.subscribe(subscribetopicliftstatus, {qos: 0});
    $('#subscribetopic').val(subscribetopicliftstatus);


    var subscribetopicdisplay = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator";
    mqtt.subscribe(subscribetopicdisplay, {qos: 0});

}

function onConnectionLost(response) {
    setTimeout(MQTTconnect, reconnectTimeout);
    $('#connectStatus').val("connection lost: " + responseObject.errorMessage + ". Reconnecting");

}

function onMessageArrived(message) {
    var topic = message.destinationName;
    var payload = message.payloadString;
    // alert(topic + " : " + payload);

    // Alert the formatted hexadecimal payload
    if (topic ==  "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator"){
        alert('Received message: ' + payload);

        var payloadBytes = message.payloadBytes;
        alert(payloadBytes);

        // Convert payload bytes to hexadecimal string
        var payloadHex = bytesToHex(payloadBytes);
        // Insert space after every two characters
        payloadHex = insertSpaceEveryNChars(payloadHex, 4);
        var result =handleInputTopicDisplay(payloadHex.replace(/\s/g, ''));
        alert('rs'+result)
        if (result === null) {
            console.log("Invalid input length. Please enter a 12-character input.");
        } else {
            // Sử dụng các tham số trả về
            var floor = result.floor;
            var direction = result.direction;
            var status = result.status;

            // In ra các giá trị đã nhận được
            console.log("Floor: " + floor);
            controller_move(1,floor)
            console.log("Direction: " + direction);
            console.log("Status: " + status);
            $('#display').val(floor);

            if(direction != "Unknown") {
                $('#direction').val(direction);
            }
            $('#onoff').val(status)
        }
    }
    if (topic ==  "Vietnam/Hanoi/Showzoom/PL1/elvtopc/carcall") {

        var payloadBytes = message.payloadBytes;
        // Convert payload bytes to hexadecimal string
        var payloadHex = bytesToHex(payloadBytes);
        // Insert space after every two characters
        payloadHex = insertSpaceEveryNChars(payloadHex, 2);
        // handleInput(payloadHex.replace(/\s/g, ''));
        var result = findCalledFloors(payloadHex);
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

function publishMessage() {
    var msg = document.getElementById("messagep").value;
    var topic = document.getElementById("topic_p").value;
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}//set timer interval event
setInterval(myTimer, 3000);
var i = 1;
var msg2 = "up";

function attClick() {
    var msg = "att activation";
    var topic = elevator + "/" + "att";
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function opendoor() {
    var msg = "door is opened";
    var topic = elevator + "/" + "dooropened";
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function closedoor() {
    var msg = "door is closed";
    var topic = elevator + "/" + "doorclosed";
    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function carcallClick() {
    var msg = document.getElementById("carcall").value;
    var topic = elevator + "/" + "carcall";

    var value = parseInt(msg);
    if (isNaN(value) || value < 0 || value > numFloors) {
        alertError("Invalid Floor Number")
        return;
    }

    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function callupClick() {
    var msg = document.getElementById("callup").value;
    var topic = elevator + "/" + "callup";

    var value = parseInt(msg);
    if (isNaN(value) || value < 0 || value > numFloors) {
        alertError("Invalid Floor Number")
        return;
    }

    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
}

function calldnClick() {
    var msg = document.getElementById("calldn").value;
    var topic = elevator + "/" + "calldn";

    var value = parseInt(msg);
    if (isNaN(value) || value < 0 || value > numFloors) {
        alertError("Invalid Floor Number")
        return;
    }

    Message = new Paho.MQTT.Message(msg);
    Message.destinationName = topic;
    mqtt.send(Message);
    document.getElementById("messages").innerHTML += "<span> Message to topic " + topic + " with value = " + msg + " is sent </span><br>";
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
function handleInputTopicDisplay(input) {
    alert('jo')
    // var input = document.getElementById("input").value;
    // alert("1")
    // Check if input is valid
    alert("Input: " + input)
    if (input.length !== 10) {
        // alert("Invalid input length. Please enter a 12-character input.");
        return;
    }
    // alert("Input: " + input);
    // Extracting d0, d1, d2, d3, d4 from input
    var d0 = input.substring(0, 2);
    var d1 = input.substring(2, 4);
    var d2 = input.substring(4, 6);
    var d3 = input.substring(6, 8);
    var d4 = input.substring(8, 10);

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
        direction = "Unknown";
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
        status = "Active";
    }

    alert("d0: " + d0 + "\n" +
        "d1: " + d1 + "\n" +
        "d2: " + d2 + "\n" +
        "d3: " + d3 + " (" + direction + ")\n" +
        "d4: " + d4 + "\n\n" +
        "Floor: " + floor + "\n" +
        "Direction: " + direction + "\n" +
        "Status: " + status);
    return {
        floor: floor,
        direction: direction,
        status: status
    };

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

function findCalledFloors(signalInput) {
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