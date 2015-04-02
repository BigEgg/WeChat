describe('When Open the Homepage', function () {
    var homepage = require('../pages/Homepage.js');

    beforeEach(function () {
        homepage.navigate();
    });

    it('should disable the submit button if no username input', function () {
        expect(homepage.isSignInButtonDisabled()).toBeTruthy();
    });

    it('should enable the submit button if username had input', function () {
        homepage.fillSignInInfo('username', '');
        expect(homepage.isSignInButtonDisabled()).toBeFalsy();
    });

    it('should keep invalid sign in on this page', function () {
        homepage.fillSignInInfo('username', '');
        homepage.signIn().then(function () {
            expect(homepage.isInThisPage()).toBeTruthy();
        });
    });

    it('ensures user can log in', function () {
        homepage.fillSignInInfo('BigEgg', '');
        homepage.signIn().then(function () {
            expect(homepage.isInThisPage()).toBeFalsy();
        });
    });
});