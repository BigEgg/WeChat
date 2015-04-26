function AuthenticateFailedException(message) {
    this.name = 'AuthenticateFailedException';
    this.message = 'error.oauth.signIn.failed';
}
AuthenticateFailedException.prototype = new Error();
AuthenticateFailedException.prototype.constructor = AuthenticateFailedException;