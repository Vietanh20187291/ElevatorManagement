alert("jo")
alert("Chiều rộng của màn hình là: " + window.innerWidth + " và chiều cao của màn hình là: " + window.innerHeight + ".")
if (window.innerWidth > window.innerHeight) {
    alert("Màn hình ngang lớn hơn dọc.");
} else if (window.innerWidth < window.innerHeight) {
    alert("Màn hình dọc lớn hơn ngang.");
} else {
    alert("Màn hình có tỷ lệ chiều rộng và chiều cao bằng nhau.");
}