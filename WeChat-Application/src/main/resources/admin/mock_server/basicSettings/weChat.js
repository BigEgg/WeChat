exports.entryPoint = function (req, res) {
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