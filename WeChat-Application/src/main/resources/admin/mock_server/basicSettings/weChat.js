exports.serverStatus = function (req, res) {
    if (req.query.access_token === 'access') {
        res.send({
            entry_point: 'http://localhost:3000/wechat',
            token: 'ABCDE_TOKEN',
            connected: true
        });
    } else {
        res.sendStatus(403);
    }
};

exports.developerInfo = function (req, res) {
    if (req.query.access_token === 'access') {
        res.send({
            app_id: 'app_id',
            app_secret: 'app_secret'
        });
    } else {
        res.sendStatus(403);
    }
};