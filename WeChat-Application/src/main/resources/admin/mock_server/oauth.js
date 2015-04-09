exports.accessToken = function (req, res) {
    if (req.body.clientId === "BigEgg") {
        res.send({
            access_token: "access",
            refresh_token: "refresh"
        });
    } else {
        res.sendStatus(401);
    }
};