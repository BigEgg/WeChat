exports.admin = function (req, res) {
    console.log(req.body);
    if (req.body.username === "BigEgg") {
        res.send({
            access_token: "access",
            refresh_token: "refresh",
            name: 'BigEgg'
        });
    } else {
        res.sendStatus(401);
    }
};