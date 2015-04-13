function UnknownException() {
    this.name = 'UnknownException';
    this.message = 'error.unknown';
}
UnknownException.prototype = new Error();
UnknownException.prototype.constructor = UnknownException;