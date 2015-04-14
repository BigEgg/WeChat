function AuthorizeFailedException(message) {
    this.name = 'AuthorizeFailedException';
    this.message = 'oauth.signIn.failed';
}
AuthorizeFailedException.prototype = new Error();
AuthorizeFailedException.prototype.constructor = AuthorizeFailedException;