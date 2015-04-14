function BadNetworkException() {
    this.name = 'BadNetworkException';
    this.message = 'error.system.bad.network';
}
BadNetworkException.prototype = new Error();
BadNetworkException.prototype.constructor = BadNetworkException;
