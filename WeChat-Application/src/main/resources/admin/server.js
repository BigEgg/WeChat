var express = require('express'),
    bodyParser = require('body-parser'),
    oAuth = require('./mock_server/oauth'),
    WeChatSettings = require('./mock_server/basicSettings/weChat');

var app = express();
app.locals.title = 'Mock Admin Site';
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({extended: true})); // for parsing application/x-www-form-urlencoded

app.use('/admin', express.static(__dirname + '/src'));
app.use('/vendor', express.static(__dirname + '/vendor'));
app.use('/i18n', express.static(__dirname + '/i18n'));

app.post('/uas/oauth/accesstoken', oAuth.accessToken);

app.get('/api/admin/wechat/server', WeChatSettings.serverStatus);
app.get('/api/admin/wechat/developer', WeChatSettings.getDeveloperInfo);
app.post('/api/admin/wechat/developer', WeChatSettings.setDeveloperInfo);

app.listen(3000);