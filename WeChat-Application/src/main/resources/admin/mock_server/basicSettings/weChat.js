exports.entryPoint = function (req, res) {
    if (req.param('access_token') === 'access') {
        res.send('http://localhost:3000/wechat');
    } else {
        res.sendStatus(403);
    }
};