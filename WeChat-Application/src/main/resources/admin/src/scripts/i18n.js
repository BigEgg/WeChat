admin.app.factory('i18n', [function () {
    var i18n = {};
    i18n.texts = {
        "index.navigator.menu": "菜单",
        "index.navigator.basicSetting": "基础设置",
        "index.navigator.basicSetting.wechat": "微信基本信息",
        "index.navigator.basicSetting.conversation": "系统默认对话",
        "index.navigator.basicSetting.menu": "公众号菜单",
        "index.navigator.basicSetting.user": "用户管理",
        "index.navigator.about": "关于黑工小组",
        "index.navigator.logIn.username": "用户名",
        "index.navigator.logIn.password": "密码",
        "index.navigator.logIn.signIn": "登陆",
        "index.navigator.user.welcome": "欢迎，",
        "index.navigator.user.bind": "微信用户绑定",
        "index.navigator.user.signOut": "登出",
        "index.content.welcome": "欢迎光临ThoughtWorks内部微信公众平台管理员界面"
    };
    i18n.get = function (key) {
        return i18n.texts[key];
    };

    return i18n;
}]);