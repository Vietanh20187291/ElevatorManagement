var useTLS = false;
cleansession = true;
var mqtt;
var reconnectTimeout = 2000;
// var elevator = document.getElementById("elevator").value;


function GUIstartConnect() {
    MQTTStartConnect()
}

function GUIOnConnect() {
    // $('#connectStatus').val('Connected');//status "+ host + ':' + port + path"
    // document.getElementById('btnConnect').hidden = true;
    // document.getElementById('btnDisconnect').hidden = false;
    alertSuccess("Connected to server");
}

function GUIOnConnectionLost(response) {
    alertError("Disconnected")
    document.getElementById('btnConnect').hidden = false;
    document.getElementById('btnDisconnect').hidden = true;
}

// function onMessageArrived(message) {
//     // alert("onMessageArrived")
//     var topic = message.destinationName;
//     var payload = message.payloadString;
//
//
//     // Alert the formatted hexadecimal payload
//     // alert("Hexadecimal Payload: " + payloadHex);
//     if (topic ==  "Vietnam/Hanoi/Showzoom/PL1/elvtopc/indicator"){
//         var payloadBytes = message.payloadBytes;
//         // Convert payload bytes to hexadecimal string
//         var payloadHex = bytesToHex(payloadBytes);
//         // Insert space after every two characters
//         payloadHex = insertSpaceEveryNChars(payloadHex, 4);
//         handleInput(payloadHex.replace(/\s/g, ''));
//     }
//
// }
function GUIShowIndicator(info,eleid) {
    if (info.direction == "Up" || info.direction == "Run Up") {
        info.direction = "↑";
    }else if (info.direction == "Down" || info.direction == "Run Down") {
        info.direction = "↓";
    }else {
        info.direction = "";
    }
    if (info.door == "Open") {
        info.door = "<|>";
    }else if (info.door == "Close") {
        info.door = ">|<";
    }else {
        info.door = ">|<";
    }
    $('.' + eleid + '.floor').text(info.floor);
    $('.' + eleid + '.direction').text(info.direction);
    $('.' + eleid + '.door').text(info.door);
    $('.' + eleid + '.status').text(info.status);
    // $('#display').val(info.floor);
    //
    // if (info.direction != "Unknown") {
    //     $('#direction').val(info.direction);
    // }
    // $('#onoff').val(info.status);
    // if(info.door == "Open") {
    //     document.getElementById("doorImage").src = "/images/dooropen";
    // } else if(info.door == "Close") {
    //     document.getElementById("doorImage").src = "/images/doorclose";
    // } else{
    //     document.getElementById("doorImage").src = "/images/doorclose";
    // }

}

function GUIStartDisconnect() {
    MQTTstartDisconnect();
    $('#connectStatus').val('Disconnected');
    document.getElementById('btnConnect').hidden = false;
    document.getElementById('btnDisconnect').hidden = true;
}



function GUICheckValidCallInput(input){
    let connectStatus = document.getElementById('connectStatus');
    if (connectStatus.value !== 'Connected') {
        alertError("Please connect to the server first")
        return false;
    }
    try {
        var value = parseInt(input);
    }catch (e) {
        alertError("Invalid type of input")
        return false;
    }
    if (isNaN(value) || value < 0 || value > numFloors) {
        alertError("Invalid Floor Number")
        return false;
    }
    return true;
}
function GUICarcallClick() {
    let connectStatus = document.getElementById('connectStatus');
    if (connectStatus.value !== 'Connected') {
        alertError("Please connect to the server first")
        return;
    }
    var input = document.getElementById("carcall").value;
    var valid = GUICheckValidCallInput(input);
    if (!valid) {
        return;
    }
    MQTTcarCall(input,elevatorList[0].id)
    alertSuccess("Sent request successfully")

}


function GUICallupClick() {
    var input = document.getElementById("callup").value;
    var valid = GUICheckValidCallInput(input);
    if (!valid) {
        return;
    }
    MQTTcallUp(input, elevatorList[0].id)
    alertSuccess("Sent request successfully")
}


function GUICalldnClick() {
    var input = document.getElementById("calldn").value;
    var valid = GUICheckValidCallInput(input);
    if (!valid) {
        return;
    }
    MQTTcallDn(input, elevatorList[0].id)
    alertSuccess("Sent request successfully")
}
function GUIOpenDoorClick(){
    let connectStatus = document.getElementById('connectStatus');
    if (connectStatus.value !== 'Connected') {
        alertError("Please connect to the server first")
        return;
    }
    MQTTOpenDoor(elevatorList[0].id)
    alertSuccess("Sent request successfully")
}

function GUICloseDoorClick() {
    let connectStatus = document.getElementById('connectStatus');
    if (connectStatus.value !== 'Connected') {
        alertError("Please connect to the server first")
        return;
    }
    MQTTCloseDoor(elevatorList[0].id)
    alertSuccess("Sent request successfully")
}
