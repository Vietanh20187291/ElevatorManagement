var host = document.getElementById("host").value;
var port = parseInt(document.getElementById("port").value);
var username = document.getElementById("username").value;
var password = document.getElementById("password").value;
var useTLS = false;
cleansession = true;
var mqtt;
var reconnectTimeout = 2000;
var elevator = document.getElementById("elevator").value;
var numFloors = parseInt(document.getElementById("numFloors").value);
console.log(elevator);

function startConnect() {
    let elements = document.getElementsByClassName('floor_number');
    if (elements.length > 0) {
        elements[0].innerHTML = '12A';
    }


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
            // setTimeout(MQTTconnect, reconnectTimeout);

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
    var subscribetopiccallcar = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/callcar";
    mqtt.subscribe(subscribetopiccallcar, {qos: 0});
    var subscribetopiccallup = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/callhallup";
    mqtt.subscribe(subscribetopiccallup, {qos: 0});
    var subscribetopiccalldown = "Vietnam/Hanoi/Showzoom/PL1/elvtopc/callhalldown";
    mqtt.subscribe(subscribetopiccalldown, {qos: 0});
    alertSuccess("Connected successfully");
}

function onConnectionLost(response) {
    // setTimeout(MQTTconnect, reconnectTimeout);
    // $('#connectStatus').val("connection lost");
    alertError("Connection lost");

}

function onMessageArrived(message) {
    // alert("onMessageArrived")
    var topic = message.destinationName;
    var payload = message.payloadString;
    // alert('hi'+topic+'payload'+payload)

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
    if (topic ==  "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator"){
        var payloadBytes = message.payloadBytes;

        // Convert payload bytes to hexadecimal string
        var payloadHex = bytesToHex(payloadBytes);
        // Insert space after every two characters
        payloadHex = insertSpaceEveryNChars(payloadHex, 4);
        handleInput(payloadHex.replace(/\s/g, ''));
    }
    if(topic == "Vietnam/Hanoi/Showzoom/PL1/elvtopc/callcar"){
        var payloadBytes = message.payloadBytes;
        var payloadHex = bytesToHex(payloadBytes);

        var floors = signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2))

        highlightFloors(1,floors)
    }
    if(topic == "Vietnam/Hanoi/Showzoom/PL1/elvtopc/callhallup"){
        var payloadBytes = message.payloadBytes;
        var payloadHex = bytesToHex(payloadBytes);
        highlightButtonUpFloors(signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2)))
    }
    if(topic == "Vietnam/Hanoi/Showzoom/PL1/elvtopc/callhalldown"){
        var payloadBytes = message.payloadBytes;
        var payloadHex = bytesToHex(payloadBytes);
        highlightButtonDownFloors(signalCalledFloorsToOutput(insertSpaceEveryNChars(payloadHex, 2)))
    }

}
function handleInput(input) {
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
    // Bỏ ký tự 0 ở đâu nếu ký tự thứ 2 là chữ
    // if (floor.length > 1 && floor[0] === '0' && isNaN(floor[1])) {
    //     floor = floor.substring(1);
    // }
    var door = "";
    if (d5 === "04") {
        door = "Open";
    } else if (d5 === "08") {
        door = "Close";
    }

    controller_move(1,floor,door)


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
    if(direction == "Up" || direction == "Run Up") {
        set_indoor_direction_display(1, DIRECTION_UP);
    }else if(direction == "Down" || direction == "Run Down") {
        set_indoor_direction_display(1, DIRECTION_DOWN);
    }else{
        set_indoor_direction_display(1, DIRECTION_STILL);
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

    // controller_open_door(1,2)
    //
    // controller_close_door(1,2)
    // controller_move(1,1,door)


}

function removeLeadingZeros(input) {
    var str = String(input);
    // Kiểm tra nếu chuỗi chỉ chứa toàn số 0
    if (/^0+$/.test(str)) {
        return '0';
    }

    // Xóa các số 0 ở đầu chuỗi
    return str.replace(/^0+/, '');
}
function getNameByFloorLevel(floorLevel) {
    floorLevel = removeLeadingZeros(floorLevel)
    for (var i = 0; i < floors.length; i++) {
        if (floors[i].floorLevel == floorLevel) {
            if (floors[i].name >= 1 && floors[i].name <= 9 && floors[i].name.length == 1) {
                return "0" + floors[i].name;
            }else{
                return floors[i].name;
            }
        }
    }
    return floorLevel;
}

function getFloorLevelByName(name) {
    name = removeLeadingZeros(name)
    for (var i = 0; i < floors.length; i++) {
        if (floors[i].name == name) {
            return floors[i].floorLevel;
        }
    }
    return name;
}



function controller_move(elevator_no, targetFloor, door) {
    targetFloor = getFloorLevelByName(targetFloor);
    // Lấy tọa độ hiện tại của thang máy
    let current_top = parseFloat($('.elevator-main.' + elevator_no).css('top'));

    // Tính toán tọa độ của tầng muốn đến dựa trên chiều cao của tầng và số tầng cần di chuyển
    let targetTop = elevator_main_first_top - (targetFloor - 1) * floor_height;

    if(current_top != targetTop && door_state == 'opened'){
        alert("Move")
        let currentFloor = Math.round((elevator_main_first_top - current_top) / floor_height) + 1;
        controller_close_door(elevator_no, currentFloor);
        door_state = 'closed';
    }



    $('.elevator-main.' + elevator_no).animate({ top: targetTop + 'px' }, {
        duration: Math.abs(targetTop - current_top) * moving_speed_millisecond_per_pixel,
        easing: "linear",
        progress: function() {

            // // Tính lại tầng hiện tại dựa trên tọa độ mới của thang máy
            // let current_floor = Math.round((elevator_main_first_top - parseFloat($(this).css('top'))) / floor_height) + 1;
            // // Hiển thị số tầng hiện tại trong thang máy
            // set_indoor_floor_number_display(getNameByFloorLevel(current_floor), elevator_no);
            // // set_indoor_floor_number_display(current_floor, elevator_no);
            // if(current_floor>targetFloor) {
            //     set_indoor_direction_display(elevator_no, DIRECTION_DOWN);
            // } else if(current_floor<targetFloor) {
            //     set_indoor_direction_display(elevator_no, DIRECTION_UP);
            // }
        },
        complete: function() {

            // Khi di chuyển hoàn thành, hiển thị hướng di chuyển là đứng yên
            // set_indoor_direction_display(elevator_no, DIRECTION_STILL);
            // Hiển thị số tầng đến mà thang máy đã di chuyển đến
            if(door == "Open" && door_state == 'closed') {

                alert('open')
                controller_open_door(elevator_no, targetFloor);
                door_state = 'opened';

            } else if(door == "Close" && door_state == 'opened') {
                controller_close_door(elevator_no, targetFloor);
                door_state = 'closed';
            }
            set_indoor_floor_number_display(getNameByFloorLevel(targetFloor), elevator_no);
        }
    });
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

function openDoorClick(){
    var msg = new Uint8Array([0x01]);
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/status";
    Message = new Paho.MQTT.Message(msg.buffer);
    Message.destinationName = topic;
    mqtt.send(Message);
    alertSuccess("Sent request successfully")
}

function closeDoorClick() {
    var msg = new Uint8Array([0x02]);
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/status";
    Message = new Paho.MQTT.Message(msg.buffer);
    Message.destinationName = topic;
    mqtt.send(Message);
    alertSuccess("Sent request successfully")
}

function carcallClick(input) {
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/callcar";
    handleCallClick(input,topic);

}

function handleCallClick(input,topic) {
    // try {
    var value = input;
    // }catch (e) {
    //     alertError("Invalid type of input")
    //     return;
    // }
    // if (isNaN(value) || value < 0 || value > numFloors) {
    //     alertError("Invalid Floor Number")
    //     return;
    // }

    var floors = [];
    floors.push(value)

    var signal = generateElevatorCallSignal(floors);
    // mqtt.publish(topic, signal, {qos: 0});

    let message = new Paho.MQTT.Message(signal);
    message.destinationName = topic;
    mqtt.send(message);
    alertSuccess("Sent request successfully")
}

function callupClick(input) {
    // alert(input)
    // var msg = document.getElementById("callup").value;
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/callhallup";

    handleCallClick(input,topic);
}


function calldnClick(input) {
    // var msg = document.getElementById("calldn").value;
    var topic = "Vietnam/Hanoi/Showzoom/PL1/pctoelv/callhalldown";

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

                // alert(floorNumber)
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
    let elevator_no = 1; // Giả sử là thang máy số 1, bạn có thể điều chỉnh nếu cần

    // Tắt tất cả các nút trước
    $('.choose-up').css({'background': 'white'});

    // Bật sáng các nút trong danh sách floorList
    floorList.forEach(floor_no => {
        pressButtonUp(floor_no)
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

