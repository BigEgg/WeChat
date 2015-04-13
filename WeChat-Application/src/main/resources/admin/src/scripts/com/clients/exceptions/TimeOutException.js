function TimeOutException() {
    this.name = 'TimeOutException';
    this.message = 'error.system.timeout';
}
TimeOutException.prototype = new Error();
TimeOutException.prototype.constructor = TimeOutException;