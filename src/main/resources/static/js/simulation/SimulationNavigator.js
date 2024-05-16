// var host = document.getElementById("host").value;
// var port = parseInt(document.getElementById("port").value);
// var username = document.getElementById("username").value;
// var password = document.getElementById("password").value;
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


    // }
    /*
     // Connection succeeded; subscribe to lift of topic:doorstatus
    mqtt.subscribe(doorstatusp, {qos: 0});
    $('#doorstatusp').val(doorstatusp);
     // Connection succeeded; subscribe to lift of topic:direction
    mqtt.subscribe(directionp, {qos: 0});
    $('#directionp').val(directionp);
     // Connection succeeded; subscribe to lift of topic:onoff
    mqtt.subscribe(onoffp, {qos: 0});
    $('#onoffp').val(onoffp);
    // Connection succeeded; subscribe to lift of topic:liftstatus
    mqtt.subscribe(liftstatusp, {qos: 0});
    $('#liftstatusp').val(liftstatusp);
   */
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

        var payloadBytes = message.payloadBytes;

        // Convert payload bytes to hexadecimal string
        var payloadHex = bytesToHex(payloadBytes);
        // Insert space after every two characters
        payloadHex = insertSpaceEveryNChars(payloadHex, 4);
        var result =handleInputTopicDisplay(payloadHex.replace(/\s/g, ''));

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

    if (topic ==  "/display"){
        alert(payload.toUpperCase())
    }
    if (topic == elevator + "/doorstatus")
    {
        if (payload.toUpperCase() == "CLOSED") {
            document.getElementById("doorImage").src = "/images/doorclose";
            // $('#doorstatus').val(payload.toUpperCase());
        } else if (payload.toUpperCase() == "OPENED") {
            document.getElementById("doorImage").src = "/images/dooropen";
            // $('#doorstatus').val(payload.toUpperCase());
        }
    } else if (topic == elevator + "/display") {
        if (payload.toUpperCase() == "1" || payload.toUpperCase() == "2" || payload.toUpperCase() == "3" || payload.toUpperCase() == "4" || payload.toUpperCase() == "5" ||
            payload.toUpperCase() == "6" || payload.toUpperCase() == "7" || payload.toUpperCase() == "8" || payload.toUpperCase() == "9" || payload.toUpperCase() == "10" ||
            payload.toUpperCase() == "11" || payload.toUpperCase() == "12" || payload.toUpperCase() == "13" || payload.toUpperCase() == "14" || payload.toUpperCase() == "15" ||
            payload.toUpperCase() == "16" || payload.toUpperCase() == "17" || payload.toUpperCase() == "18" || payload.toUpperCase() == "19" || payload.toUpperCase() == "20" ||
            payload.toUpperCase() == "21" || payload.toUpperCase() == "22" || payload.toUpperCase() == "23" || payload.toUpperCase() == "24" || payload.toUpperCase() == "25" ||
            payload.toUpperCase() == "26" || payload.toUpperCase() == "27" || payload.toUpperCase() == "28" || payload.toUpperCase() == "29" || payload.toUpperCase() == "30" ||
            payload.toUpperCase() == "31" || payload.toUpperCase() == "32" || payload.toUpperCase() == "33" || payload.toUpperCase() == "34" || payload.toUpperCase() == "35" ||
            payload.toUpperCase() == "36" || payload.toUpperCase() == "37" || payload.toUpperCase() == "38" || payload.toUpperCase() == "39" || payload.toUpperCase() == "40" ||
            payload.toUpperCase() == "B1" || payload.toUpperCase() == "B2" || payload.toUpperCase() == "B3" || payload.toUpperCase() == "G" || payload.toUpperCase() == "M" ||
            payload.toUpperCase() == "B" || payload.toUpperCase() == "12A" || payload.toUpperCase() == "12B" || payload.toUpperCase() == "A" || payload.toUpperCase() == "3A") {
            $('#display').val(payload.toUpperCase());
        }

    } else if (topic == elevator + "/direction") {
        if (payload.toUpperCase() == "UP") {
            // $('#direction').val('\u2191')
            $('#direction').val('▲')
        } else if (payload.toUpperCase() == "DN") {
            // $('#direction').val('\u2193')
            $('#direction').val('▼')
        } else if (payload.toUpperCase() == "STP") {
            $('#direction').val('_')
        }
    } else if (topic == elevator + "/onoff") {
        if (payload.toUpperCase() == "STP") {
            $('#direction').val('LIFT-OFF')
        } else if (payload.toUpperCase() == "ON") {
            $('#onoff').val('ON');
            $('#onoff').blur();
        } else if (payload.toUpperCase() == "OFF") {
            $('#onoff').val('OFF')

        }
    } else if (topic == elevator + "/liftstatus") {
        if (payload.toUpperCase() == "JU" || payload.toUpperCase() == "IF" || payload.toUpperCase() == "AUTO" || payload.toUpperCase() == "FULL" ||
            payload.toUpperCase() == "FIRE" || payload.toUpperCase() == "OVERLOAD" || payload.toUpperCase() == "--" || payload.toUpperCase() == "ATT"
            || payload.toUpperCase() == "PAK")
            $('#liftstatus').val(payload.toUpperCase())
    }

    //$('#ws').prepend(' ' + payload);

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

//function in clycle
// function myTimer() {
//     var msg1 = i.toString();
//     var topic1 = elevator+ "/display";
//     var msg2 = "up";
//     var topic2 = elevator+ "/direction";
//     var msg3 = "on";
//     var topic3 = elevator+ "/onoff";
//     var msg4 = "closed";
//     var topic4 = elevator+ "/doorstatus";
//     var msg5 = "auto";
//     var topic5 = elevator+ "/liftstatus";
//     if (i % 3 == 0) msg4 = "opened";
//     Message1 = new Paho.MQTT.Message(msg1);
//     Message1.destinationName = topic1;
//     Message2 = new Paho.MQTT.Message(msg2);
//     Message2.destinationName = topic2;
//     Message3 = new Paho.MQTT.Message(msg3);
//     Message3.destinationName = topic3;
//     Message4 = new Paho.MQTT.Message(msg4);
//     Message4.destinationName = topic4;
//     Message5 = new Paho.MQTT.Message(msg5);
//     Message5.destinationName = topic5;
//     mqtt.send(Message1);
//     mqtt.send(Message2);
//     mqtt.send(Message3);
//     mqtt.send(Message4);
//     mqtt.send(Message5);
//     i++;
//     if (i > 40) i = 1;
// }

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

// document.addEventListener('DOMContentLoaded', function() {
//     var inputFloorNums = document.getElementById('input-floor-nums');
//
//     inputFloorNums.addEventListener('input', function() {
//         var value = parseInt(inputFloorNums.value);
//
//         if (isNaN(value) || value > 50) {
//             inputFloorNums.value = '';
//             alertError("Invalid Floor Number")
//         }
//     });
// });
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
    // var input = document.getElementById("input").value;
    // alert("1")
    // Check if input is valid
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

    // alert("d0: " + d0 + "\n" +
    //     "d1: " + d1 + "\n" +
    //     "d2: " + d2 + "\n" +
    //     "d3: " + d3 + " (" + direction + ")\n" +
    //     "d4: " + d4 + "\n\n" +
    //     "Floor: " + floor + "\n" +
    //     "Direction: " + direction + "\n" +
    //     "Status: " + status);
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