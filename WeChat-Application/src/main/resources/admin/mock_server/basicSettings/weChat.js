exports.entryPoint = function (req, res) {
    if (req.param('access_token') === 'access') {
        res.send({
            entry_point: 'http://localhost:3000/wechat',
            token: 'ABCDE_TOKEN'
        });
    } else {
        res.sendStatus(403);
    }
};