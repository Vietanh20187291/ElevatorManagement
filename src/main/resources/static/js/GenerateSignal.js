function indicatorpayloadBytesToResult(payloadBytes) {
    var payloadHex = bytesToHex(payloadBytes);
    // Insert space after every two characters
    payloadHex = insertSpaceEveryNChars(payloadHex, 4);
    var result =handleInputTopicDisplay(payloadHex.replace(/\s/g, ''));
    return result;
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
        // direction = "Up";
        direction = "▲";
    } else if (d3 === "11") {
        // direction = "Run Up";
        direction = "▲";
    } else if (d3 === "20") {
        // direction = "Down";
        direction = "▼";
    } else if (d3 === "21") {
        // direction = "Run Down";
        direction = "▼";
    } else {
        // direction = "Unknown";
        direction = "_";
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

    return result.join(' ');
}