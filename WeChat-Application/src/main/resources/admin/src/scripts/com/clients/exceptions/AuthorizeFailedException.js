function AuthorizeFailedException(message) {
    this.name = "AuthorizeFailedException";
    this.message = message || "";
}
AuthorizeFailedException.prototype = new Error();
AuthorizeFailedException.prototype.constructor = AuthorizeFailedException;