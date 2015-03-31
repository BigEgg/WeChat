exports.admin = function (req, res) {
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