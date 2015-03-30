function SystemBadNetworkException() {
    this.name = "SystemBadNetworkException";
    this.message = "error.system.bad.network";
    this.toString = function () {
        return this.name + ": " + this.message
    };
}
SystemBadNetworkException.prototype = new Error();
SystemBadNetworkException.prototype.constructor = SystemBadNetworkException;
