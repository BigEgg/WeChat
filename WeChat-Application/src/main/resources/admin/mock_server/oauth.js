exports.accessToken = function (req, res) {
    if (req.body.clientId === 'BigEgg') {
        res.send({
            access_token: 'access',
            refresh_token: 'refresh'
        });
    } else {
        res.send({
            access_token: '',
            refresh_token: ''
        });
    }
};

exports.refresh = function (req, res) {
    if (req.body.access_token === 'access' && req.body.refresh_token === 'refresh') {
        res.send({
            access_token: 'access',
            refresh_token: 'refresh'
        });
    } else {
        res.send({
            access_token: '',
            refresh_token: ''
        });
    }
};