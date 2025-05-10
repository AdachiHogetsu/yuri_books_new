window.addEventListener('beforeunload', function(event) {
    // 获取当前时间的 ISO 字符串作为登出时间

    // 发送请求到服务器，记录用户的登出信息
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/closepage', true);
    xhr.setRequestHeader('Content-Type', 'text/plain');
    xhr.send();
});
