function AuthenticateFailedException(message) {
    this.name = 'AuthenticateFailedException';
    this.message = 'oauth.authenticate.failed';
}
AuthenticateFailedException.prototype = new Error();
AuthenticateFailedException.prototype.constructor = AuthenticateFailedException;