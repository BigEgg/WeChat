exports.status = function (req, res) {
    if (req.query.access_token === 'access') {
        res.send({
            server_connected: true,
            api_status: false
        });
    } else {
        res.sendStatus(500);
    }
};

exports.serverStatus = function (req, res) {
    if (req.query.access_token === 'access') {
        res.send({
            entry_point: 'http://localhost:3000/wechat',
            token: 'ABCDE_TOKEN'
        });
    } else {
        res.sendStatus(500);
    }
};

exports.getDeveloperInfo = function (req, res) {
    if (req.query.access_token === 'access') {
        res.send({
            app_id: '',
            app_secret: ''
        });
    } else {
        res.sendStatus(500);
    }
};

exports.setDeveloperInfo = function (req, res) {
    if (!req.body.app_id || !req.body.app_secret) {
        res.sendStatus(400);
        return;
    }

    if (req.query.access_token === 'access') {
        res.send({
            app_id: req.body.app_id,
            app_secret: req.body.app_secret
        });
    } else {
        res.sendStatus(500);
    }
};