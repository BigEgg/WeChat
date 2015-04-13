function AuthenticateFailedException(message) {
    this.name = "AuthenticateFailedException";
    this.message = message || "";
}
AuthenticateFailedException.prototype = new Error();
AuthenticateFailedException.prototype.constructor = AuthenticateFailedException;