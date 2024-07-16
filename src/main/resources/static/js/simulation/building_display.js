function set_building_display(floor_nums, elevator_nums) {
    $('#elevator_nums_info').html('' + elevator_nums)
    $('#floor_nums_info').html('' + floor_nums)
    $(".g-container").html(grids_html_of_a_building(floor_nums, elevator_nums))
    set_g_container_grid_template(floor_nums, elevator_nums)
    choose_outdoor_witches(1, DIRECTION_DOWN).hide()
    choose_outdoor_witches(floor_nums, DIRECTION_UP).hide()
    cal_floor_height()
    cal_elevator_main_first_top()
    $('.elevator-line').height(cal_elevator_line_height(floor_nums) + 'px')
}

function controller_move(elevator_no, targetFloor, door) {
    console.log('controller_move');
    console.log('targetFloor' + targetFloor);

    // Lấy tọa độ hiện tại của thang máy
    let current_top = parseFloat($('.elevator-main.' + elevator_no).css('top'));

    // Tính toán tọa độ của tầng muốn đến dựa trên chiều cao của tầng và số tầng cần di chuyển
    let targetTop = elevator_main_first_top - (targetFloor - 1) * floor_height;
    // if(current_top == targetTop) {
    //     if(door == 'Open') {
    //         let current_floor = Math.round((elevator_main_first_top - parseFloat($(this).css('top'))) / floor_height) + 1;
    //         controller_open_door(elevator_no, current_floor);
    //     } else if(door == 'Close'){
    //         let current_floor = Math.round((elevator_main_first_top - parseFloat($(this).css('top'))) / floor_height) + 1;
    //         controller_close_door(elevator_no,current_floor);
    //     }
    // }
    // Di chuyển thang máy đến tọa độ của tầng muốn đến
    $('.elevator-main.' + elevator_no).animate({ top: targetTop + 'px' }, {
        duration: Math.abs(targetTop - current_top) * moving_speed_millisecond_per_pixel,
        easing: "linear",
        progress: function() {
            // Tính lại tầng hiện tại dựa trên tọa độ mới của thang máy
            let current_floor = Math.round((elevator_main_first_top - parseFloat($(this).css('top'))) / floor_height) + 1;
            // Hiển thị số tầng hiện tại trong thang máy
            set_indoor_floor_number_display("0"+current_floor, elevator_no);
            if(current_floor>targetFloor) {
                set_indoor_direction_display(elevator_no, DIRECTION_DOWN);
            } else if(current_floor<targetFloor) {
                set_indoor_direction_display(elevator_no, DIRECTION_UP);
            }
        },
        complete: function() {
            // Khi di chuyển hoàn thành, hiển thị hướng di chuyển là đứng yên
            set_indoor_direction_display(elevator_no, DIRECTION_STILL);
            // Hiển thị số tầng đến mà thang máy đã di chuyển đến
            set_indoor_floor_number_display(targetFloor, elevator_no);
        }
    });
}


function get_current_floor(elevator_no) {
    // Get the current top position of the elevator main container
    let current_top = parseFloat($('.elevator-main.' + elevator_no).css('top'));

    // Calculate the distance the elevator has moved from the starting position
    let distance_moved = elevator_main_first_top - current_top;

    // Calculate the current floor number based on the distance moved and floor height
    let current_floor = Math.round(distance_moved / floor_height) + 1;

    return current_floor;
}
function controller_move_up(elevator_no, callback) {
    $('.elevator-main.' + elevator_no + ' .elevator-line').animate({height: '-=' + floor_height + 'px'}, floor_height * moving_speed_millisecond_per_pixel, "linear", function() {
        $('.elevator-main.' + elevator_no).animate({top: '-=' + floor_height + 'px'}, floor_height * moving_speed_millisecond_per_pixel, "linear", callback);
    });
}

function controller_move_down(elevator_no, callback) {
    $('.elevator-main.' + elevator_no + ' .elevator-line').animate({height: '+=' + floor_height + 'px'}, floor_height * moving_speed_millisecond_per_pixel, "linear", function() {
        $('.elevator-main.' + elevator_no).animate({top: '+=' + floor_height + 'px'}, floor_height * moving_speed_millisecond_per_pixel, "linear", callback);
    });
}
function controller_move_up_N_floors(elevator_no, floors) {
    console.log('controller_move_up_N_floors');
    var total_height = floors * floor_height;
    console.log('total_height'+total_height);
    $('.elevator-main.' + elevator_no + ' .elevator-line').animate(
        { height: '-=' + total_height + 'px' },
        total_height * moving_speed_millisecond_per_pixel,
        "linear",
        function() {
            $('.elevator-main.' + elevator_no).animate(
                { top: '-=' + total_height + 'px' },
                total_height * moving_speed_millisecond_per_pixel,
                "linear",
                // callback
            );
        }
    );
}

function controller_move_down_N_floors(elevator_no, floors) {
    console.log('controller_move_down_N_floors');
    var total_height = floors * floor_height;
    console.log('total_height'+total_height);
    $('.elevator-main.' + elevator_no + ' .elevator-line').animate(
        { height: '+=' + total_height + 'px' },
        total_height * moving_speed_millisecond_per_pixel,
        "linear",
        function() {
            $('.elevator-main.' + elevator_no).animate(
                { top: '+=' + total_height + 'px' },
                total_height * moving_speed_millisecond_per_pixel,
                "linear",
                // callback
            );
        }
    );
}

function set_g_container_grid_template(floor_nums, elevator_nums) {
    let column_res = wall_side_with + string_repeat(' ' + wall_main_with, elevator_nums) + ' ' + wall_side_with
    let row_res = top_floor_height + string_repeat(' ' + celling_height + ' ' + wall_main_height + ' ' + ground_height, floor_nums)
    $('.g-container').css('grid-template-columns', column_res)
    $('.g-container').css('grid-template-rows', row_res)

}


function ext_set_is_AI_mode_enabled(enable_AI_mode) {
    if (enable_AI_mode) {
        $('.enable-AI-mode-button').html('')
    } else {

        $('.enable-AI-mode-button').html('')
    }
}

function toggle_outdoor_switch(floor_no, direct) {
    let rev_state = ON
    if (outdoor_buttons_state[floor_no][direct] === ON) {
        rev_state = OFF
    }
    set_outdoor_switch(floor_no, direct, rev_state)
}

function set_outdoor_switch(floor_no, direct, state) {
    outdoor_buttons_state[floor_no][direct] = state
    if (outdoor_buttons_state[floor_no][direct] === ON) {
        choose_outdoor_witches(floor_no, direct).css('background', 'orange')
        dispatch_request(floor_no, direct)
    } else {
        choose_outdoor_witches(floor_no, direct).css('background', 'white')
    }
}

function set_indoor_floor_switch_state(floor_no, elevator_no, state) {
    if (state === ON) {
        ext_choose_indoor_floor_switch(floor_no, elevator_no).css({'background': 'orange'})
    } else {
        ext_choose_indoor_floor_switch(floor_no, elevator_no).css({'background': 'white'})
    }
}

function choose_outdoor_witches(floor_no, direct) {
    if (direct === DIRECTION_UP) {
        return $('.elevator-window.' + floor_no + ' .choose-up')
    } else {
        return $('.elevator-window.' + floor_no + ' .choose-down')
    }
}

function ext_choose_indoor_floor_switch(floor_no, elevator_no) {
    return $('.choose-floor-block.' + elevator_no + ' .choose-floor-button.' + floor_no)
}


function ext_choose_indoor_open_door_switch(elevator_no) {
    return $('.choose-floor-block.' + elevator_no + ' .indoor-open-door')
}

function ext_choose_indoor_close_door_switch(elevator_no) {
    return $('.choose-floor-block.' + elevator_no + ' .indoor-close-door')
}

function ext_choose_indoor_floor_number_display(elevator_no) {
    return $('.floor-number.' + elevator_no)
}

function ext_choose_indoor_direction_display(elevator_no) {
    return $('.elevator-direction.' + elevator_no)
}

function set_indoor_floor_number_display(floor_no, elevator_no) {
    ext_choose_indoor_floor_number_display(elevator_no).html('' + floor_no)
}


let direction_display_symbol = new Map([
    [DIRECTION_UP, '￪'],
    [DIRECTION_DOWN, '￬'],
    [DIRECTION_STILL, ''],
    [DIRECTION_AI_MODE_WAITING_FOR_CHANGING, 'C']
])

function set_indoor_direction_display(elevator_no, direct) {
    let res = ''
    if (enable_AI_mode_display && elevators[elevator_no].state.now_direction === DIRECTION_STILL && elevators[elevator_no].state.auto_mode_state === AI_MODE_RUNNING) {
        res = 'A'
    } else if (direct === DIRECTION_AI_MODE_WAITING_FOR_CHANGING && (!enable_AI_mode_display || !enable_DIRECTION_AI_MODE_WAITING_FOR_CHANGING_display)) {
        res = ''
    } else {
        res = direction_display_symbol.get(direct)
    }
    ext_choose_indoor_direction_display(elevator_no).html(res)
}


let tempalte_of_choose_floor_button = '<button onclick="carcallClick(mark-of-fno)" class="btn btn-default btn-circle  choose-floor-button mark-of-fno" type="button">name-of-fno</button>\n' +
    '                    '
let template_of_open_close_buttons = '' +
    '            <div class="open-close-buttons">\n' +
    '                <button onclick="openDoorClick()" class="btn btn-default btn-circle open-close choose-floor-button" type="button">◀|▶\n' +
    '                </button>\n' +
    '                <button onclick="closeDoorClick()" class="btn btn-default btn-circle open-close indoor-close-door choose-floor-button" type="button">▶|◀\n' +
    '                </button>\n' +
    '\n' +
    '            </div>'

function div_html_of_open_close_buttons() {
    return template_of_open_close_buttons
}

function div_html_of_choose_floor_block(floor_nums, elevator_no, elevator_name){
    let res = ('<div class="g-panel-item-wrapper"><div class="elevator-window choose-floor-block mark-of-eno">\n' +
        '<div class="take-place elevator-info">\n' +
        // '                        <p class="choose-floor-prompt"><span class="a">#mark-of-eno</span></p>\n' +
        '\n' +
        '     <div class="now-floor-info">\n' +
        '                        <div class="floor-info-decoration"></div>\n' +
        '                        <div class="elevator-direction mark-of-eno">￬</div><div class="floor-number mark-of-eno">1</div></div>\n' +
        '\n' +
        '                    </div>').replace(/mark-of-eno/g, elevator_no)
    for (let k = 1; k <= floor_nums; k++) {
        res += tempalte_of_choose_floor_button.replace(/mark-of-fno/g, k).replace(/name-of-fno/g, getNameByFloorLevel(k))
    }
    res += div_html_of_open_close_buttons()
    res += '<h1>mark-of-eno</h1>'.replace(/mark-of-eno/g, elevator_name)
    res += '</div></div>'
    return res
}

let template_of_choose_floor_block_side = '<div class="g-panel-item-wrapper"><div class="elevator-window choose-floor-block panel-side"></div></div>'

function div_html_off_choose_floor_block_side() {
    return template_of_choose_floor_block_side
}

function set_panel_body(floor_nums, elevator_nums,list_elevators) {
    let res = div_html_off_choose_floor_block_side()
    console.log(list_elevators)
    // for (let i = 0; i < elevatorList.length; i++) {
        res += div_html_of_choose_floor_block(elevatorList[0].numFloors, elevatorList[0].id, elevatorList[0].name);

    // }

    res += div_html_off_choose_floor_block_side()

    $('#g-panel-body').html(res)
    let grid_template_columns_of_panel_body =
        wall_side_with + string_repeat(' ' + wall_main_with, elevator_nums) + ' ' + wall_side_with
    $('#g-panel-body').css('grid-template-columns', grid_template_columns_of_panel_body)
    $('.take-place-panel').css({'height': $('#g-panel-content').height()})

    // for (let i = 1; i <= elevator_nums; i++) {
        elevators[0].set_now_floor_no(elevators[i].state.now_floor_no)
        elevators[0].set_now_direction(elevators[i].state.now_direction)
    // }

    // for (let elevatorId in list_elevators) {
    //     if (list_elevators.hasOwnProperty(elevatorId)) {
            elevators[0].set_now_floor_no(elevators[0].state.now_floor_no)
            elevators[0].set_now_direction(elevators[0].state.now_direction)
    //     }
    // }
    pin_elements_to_right_by_class('choose-floor-block')
}


//building

let template_of_td_ceiling = '<div class="elevator-window ceiling"></div>'

function divs_html_of_a_ceiling(floor_no, elevator_nums) {
    return string_repeat(template_of_td_ceiling, elevator_nums + 2)
}


let template_of_div_elevator_window = '<div class="elevator-window mark-of-fno mark-of-fno-mark-of-eno">\n' +
    '                        <button onclick="callupClick(mark-of-fno)" class="btn btn-default btn-circle  elevator-choose-take choose-up" type="button">▲\n' +
    '                        </button>\n' +
    '                        <button onclick="calldnClick(mark-of-fno)" class="btn btn-default btn-circle  elevator-choose-take choose-down" type="button">▼\n' +
    '                        </button>\n' +
    '\n' +
    '                        mark-of-elevator-main\n' +
    '                        <div class="elevator-body elevator-outdoor ">\n' +
    '                            <div class="elevator-door door-left"></div>\n' +
    '                            <div class="elevator-door door-right"></div>\n' +
    '                        </div>\n' +
    '                    </div>'
let template_of_div_elevator_main = '                        <div class="elevator-body elevator-main mark-of-eno">\n' +
    '\n' +
    '                            <div class="elevator-line"></div>\n' +
    '                        </div>'

function div_html_of_elevator_window(floor_no, elevator_no) {
    let elevator_main_info = ''
    if (floor_no === 1) {
        elevator_main_info = template_of_div_elevator_main.replace(/mark-of-eno/g, elevator_no)
    }
    return template_of_div_elevator_window.replace(/mark-of-fno/g, floor_no).replace(/mark-of-eno/g, elevator_no).replace(/mark-of-elevator-main/g, elevator_main_info)
}

let path_of_potting = "/images/simulation/potting"
let template_of_td_elevator_wall_side = '        ' +
    '<div class="elevator-window ">\n' +
    '            <div class="floor-symbol">\n' +
    '                mark_floor_no\n' +
    '            </div>\n' +
    '\n' +
    // '            <img class="potting" src="mark-of-potting-path">\n' +
    '\n' +
    '</div>'

function div_html_of_elevator_wall_side(floor_no) {
    return template_of_td_elevator_wall_side.replace(/mark_floor_no/g, "F"+getNameByFloorLevel(floor_no)).replace(/mark-of-potting-path/g, path_of_potting)
}

function divs_html_of_elevator_wall(floor_no, elevator_nums) {
    let res = ''
    res += div_html_of_elevator_wall_side(floor_no)
    for (let i = 1; i <= elevator_nums; i++) {
        res += div_html_of_elevator_window(floor_no, i)
    }
    res += div_html_of_elevator_wall_side(floor_no)
    return res
}

let template_of_td_elevator_ground = '<div class="elevator-window ceiling ground">\n' +
    '\n' +
    '                        <div class="blanket"></div>\n' +
    '\n' +
    '                    </div>'

function divs_html_of_a_ground(floor_no, elevator_nums) {
    return string_repeat(template_of_td_elevator_ground, elevator_nums + 2)
}

function divs_html_of_a_floor(floor_no, elevator_nums) {
    return divs_html_of_a_ceiling(floor_no, elevator_nums) + divs_html_of_elevator_wall(floor_no, elevator_nums) +
        divs_html_of_a_ground(floor_no, elevator_nums)
}

let template_of_building_banner = '<div class="banner-item">mark_of_building_name</div>'
let template_of_div_top_floor = '<div class="elevator-window elevator-ceiling"></div>'

function divs_html_of_top_floor(floor_no, elevator_nums) {
    let res = ''
    res += template_of_building_banner.replace(/mark_of_building_name/g, building_name)
    res += string_repeat(template_of_div_top_floor, elevator_nums + 2)
    return res
}

function grids_html_of_a_building(floor_nums, elevator_nums) {
    let res = divs_html_of_top_floor(floor_nums + 1, elevator_nums)
    for (let i = floor_nums; i >= 1; i--) {
        res += divs_html_of_a_floor(i, elevator_nums)
    }
    return res
}


let elevator_line_height = 330
let elevator_main_first_top = 123
let floor_height = 320


function cal_elevator_main_first_top() {
    elevator_main_first_top = $(".elevator-main.1").position().top
    return elevator_main_first_top
}

// let elevator_line_first_height
function cal_elevator_line_height(floor_nums) {
    elevator_line_height = cal_floor_height() * floor_nums - $(".elevator-main").height() -
        $('.elevator-window.ceiling.ground').height()
    return elevator_line_height
}

function cal_floor_height() {
    floor_height = $(".elevator-window.1-1").height() +
        $('.elevator-window.ceiling').height() +
        $('.elevator-window.ceiling.ground').height()
    return floor_height
}

function controller_directly_go_to_floor(elevator_no, floor_no) {
-
    $('.elevator-main.' + elevator_no + ' .elevator-line').css({'height': elevator_line_height - (floor_no - 1) * floor_height + 'px'})

}

function controller_wait_for_timeout_and_callBack(elevator_no, waiting_duration, callBack) {
    $('.elevator-main.' + elevator_no).animate({opacity: '-=%0'}, waiting_duration, "linear", callBack)
}

function controller_stop_waiting_for_timeout_and_callback(elevator_no, callBack) {
    $('.elevator-main.' + elevator_no).stop()
    callBack()
}



function controller_close_door(elevator_no) {
    let current_floor = Math.round((elevator_main_first_top - parseFloat($(this).css('top'))) / floor_height) + 1;
    let elevator_window_mark = current_floor + '-' + elevator_no
    $('.elevator-main.' + elevator_no).animate({opacity: '75%'}, toggle_door_secs, "linear")
    $('.elevator-window.' + elevator_window_mark + ' .elevator-door').animate({width: '50%'}, toggle_door_secs, "linear")
}

function controller_stop_closing_door(floor_no, elevator_no, callBack) {
    let elevator_window_mark = floor_no + '-' + elevator_no
    $('.elevator-main.' + elevator_no).stop()
    $('.elevator-window.' + elevator_window_mark + ' .elevator-door').stop()
    callBack()
}

function controller_open_door(elevator_no,ele_no) {
    // let current_floor = Math.round((elevator_main_first_top - parseFloat($(this).css('top'))) / floor_height) + 1;
    let elevator_window_mark = ele_no + '-' + elevator_no
    $('.elevator-main.' + elevator_no).animate({opacity: '100%'}, toggle_door_secs, "linear")
    $('.elevator-window.' + elevator_window_mark + ' .elevator-door').animate({width: '0%'}, toggle_door_secs, "linear")

}
//
// function move(elevator_no, callback) {
//     var ran = Math.floor(Math.random() * (7 - 3 + 1)) + 7;
//     console.log(ran);
//     let elevatorMainTop = $('.elevator-main.' + elevator_no).position().top;
//     let targetFloorTop = floor_height * (ran - 1); // Tính toán độ cao của tầng cần di chuyển tới
//     if (elevatorMainTop > targetFloorTop) {
//         // Nếu độ cao hiện tại lớn hơn độ cao của tầng đích, thì di chuyển xuống
//         let floorsToMove = Math.ceil((elevatorMainTop - targetFloorTop) / floor_height);
//         for (let i = 0; i < floorsToMove; i++) {
//             controller_move_down(elevator_no);
//         }
//     } else if (elevatorMainTop < targetFloorTop) {
//         // Nếu độ cao hiện tại nhỏ hơn độ cao của tầng đích, thì di chuyển lên
//         let floorsToMove = Math.ceil((targetFloorTop - elevatorMainTop) / floor_height);
//         for (let i = 0; i < floorsToMove; i++) {
//             controller_move_up(elevator_no);
//         }
//     }
//     // Nếu độ cao hiện tại bằng với độ cao của tầng đích, không cần phải di chuyển
//     if (typeof callback === 'function') {
//         callback(); // Gọi callback sau khi di chuyển hoàn tất
//     }
// }
// function controller_move(elevator_no, target_floor_no, callback) {
//     // Calculate the floor height
//     let floor_height = cal_floor_height();
//
//     // Function to get the current floor based on the elevator's position
//     function getCurrentFloor() {
//         let current_top_position = $('.elevator-main.' + elevator_no).position().top;
//         let ground_floor_top_position = elevator_main_first_top;
//         return Math.round((ground_floor_top_position - current_top_position) / floor_height) + 1;
//     }
//
//     // Initial calculation of the current floor
//     let current_floor_no = getCurrentFloor();
//
//     // The move function
//     function move() {
//         current_floor_no = getCurrentFloor();
//
//         if (current_floor_no < target_floor_no) {
//             controller_move_up(elevator_no, function() {
//                 move(); // Recursively call move until the target floor is reached
//             });
//         } else if (current_floor_no > target_floor_no) {
//             controller_move_down(elevator_no, function() {
//                 move(); // Recursively call move until the target floor is reached
//             });
//         } else {
//             callback(); // Call the callback function when the elevator reaches the target floor
//         }
//     }
//
//     // Start the move process
//     move();
// }
// let elevatorQueues = {}; // Store queues for each elevator
//
// // Initialize the queue for each elevator
// function initializeElevatorQueues(elevator_nums) {
//     for (let i = 1; i <= elevator_nums; i++) {
//         elevatorQueues[i] = [];
//     }
// }
//
// // Function to add a floor request to the queue
// function addFloorRequest(elevator_no, floor_no) {
//     elevatorQueues[elevator_no].push(floor_no);
//     if (elevatorQueues[elevator_no].length === 1) {
//         // If the queue was empty before adding this request, start processing
//         processQueue(elevator_no);
//     }
// }
//
// // Function to process the queue for a specific elevator
// function processQueue(elevator_no) {
//     if (elevatorQueues[elevator_no].length === 0) {
//         return;
//     }
//
//     let target_floor_no = elevatorQueues[elevator_no][0];
//     controller_move(elevator_no, target_floor_no).then(() => {
//         // Remove the processed request from the queue
//         elevatorQueues[elevator_no].shift();
//         // Process the next request
//         processQueue(elevator_no);
//     });
// }
//
// // Updated controller_move function with Promise support
// function controller_move(elevator_no, target_floor_no) {
//     return new Promise((resolve) => {
//         let current_floor_no = elevators[elevator_no].state.now_floor_no;
//
//         function moveElevator() {
//             if (current_floor_no < target_floor_no) {
//                 controller_move_up(elevator_no, () => {
//                     current_floor_no++;
//                     if (current_floor_no < target_floor_no) {
//                         moveElevator();
//                     } else {
//                         // Open the door when the target floor is reached
//                         controller_open_door(elevator_no, target_floor_no);
//                         resolve(); // Resolve the promise
//                     }
//                 });
//             } else if (current_floor_no > target_floor_no) {
//                 controller_move_down(elevator_no, () => {
//                     current_floor_no--;
//                     if (current_floor_no > target_floor_no) {
//                         moveElevator();
//                     } else {
//                         // Open the door when the target floor is reached
//                         controller_open_door(elevator_no, target_floor_no);
//                         resolve(); // Resolve the promise
//                     }
//                 });
//             } else {
//                 // If already on the target floor, resolve immediately
//                 resolve();
//             }
//         }
//
//         moveElevator();
//     });
// }
//
//
//     // Function to get the current floor based on the elevator's position
//     function getCurrentFloor() {
//         let floor_height = cal_floor_height();
//         let current_top_position = $('.elevator-main.' + elevator_no).position().top;
//         let ground_floor_top_position = elevator_main_first_top;
//         return Math.round((ground_floor_top_position - current_top_position) / floor_height) + 1;
//     }
// Hàm ấn nút xuống
function pressButtonDown(floor_no) {
    set_outdoor_switch(floor_no, DIRECTION_DOWN, ON);
}

// Hàm ấn nút lên
function pressButtonUp(floor_no) {
    set_outdoor_switch(floor_no, DIRECTION_UP, ON);
}
function getNameByFloorLevel(floorLevel) {
    for (var i = 0; i < floors.length; i++) {
        if (floors[i].floorLevel === floorLevel) {
            return floors[i].name;
        }
    }
    return null;
}

function getFloorLevelByName(name) {
    for (var i = 0; i < floors.length; i++) {
        if (floors[i].name === name) {
            return floors[i].floorLevel;
        }
    }
    return null;
}

