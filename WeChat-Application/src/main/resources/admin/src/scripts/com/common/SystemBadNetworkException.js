function SystemBadNetworkException() {
    this.name = "SystemBadNetworkException";
    this.message = "error.system.bad.network";
}
SystemBadNetworkException.prototype = new Error();
SystemBadNetworkException.prototype.constructor = SystemBadNetworkException;
