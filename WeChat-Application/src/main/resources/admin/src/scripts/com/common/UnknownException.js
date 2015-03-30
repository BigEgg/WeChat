function UnknownException() {
    this.name = 'UnknownException';
    this.message = '';
}
UnknownException.prototype = new Error();
UnknownException.prototype.constructor = UnknownException;