// 获取 Cookie 的 value
function getCookie(name) {
    //可以搜索RegExp和match进行学习
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    } else {
        return null;
    }
}

function setCookie7Days(cookie_name, cookie_value) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 1000 * 60 * 60 * 24 * 7);// 过期时间 7 天
    document.cookie = cookie_name + "=" + escape(cookie_value) + ";expires=" + exp.toGMTString();
}

function setCookie1Days(cookie_name, cookie_value) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 1000 * 60 * 60 * 24 * 1);// 过期时间 1 天
    document.cookie = cookie_name + "=" + escape(cookie_value) + ";expires=" + exp.toGMTString();
}


function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}

function check_token_value(token_value) {

    $('#table_tbody_id').html("");

    return check_obj(token_value,
        'function{checkTokenValue}...',
        "用户没有登录 ... ... ");
}

function check_parm_value(parm_value) {
    $('#table_tbody_id').html("");

    return check_obj(parm_value,
        'function{check_parm_value}...',
        "请输入关键词 ... ... ");
}

function check_file_path_sub_docs_file_path(file_path_sub_docs_file_path) {
    return check_obj(file_path_sub_docs_file_path,
        'function{check_file_path_sub_docs_file_path}...',
        "文章Id 非法 ... ... ");
}


function check_token_value(token_value) {
    $('#table_tbody_id').html("");
    return check_obj(token_value,
        'function{checkTokenValue}...',
        "用户没有登录 ... ... ");
}

function check_obj(obj, log_msg, notify_msg_html) {
    console.log(log_msg);

    if (!obj) {
        $('#notifyMsg_id').html(notify_msg_html);
        return false;
    }
    return true;

}