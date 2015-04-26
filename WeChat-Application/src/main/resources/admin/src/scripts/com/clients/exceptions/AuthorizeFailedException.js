function AuthorizeFailedException(message) {
    this.name = 'AuthorizeFailedException';
    this.message = 'error.oauth.authorize.failed';
}
AuthorizeFailedException.prototype = new Error();
AuthorizeFailedException.prototype.constructor = AuthorizeFailedException;