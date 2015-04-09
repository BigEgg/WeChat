describe('OAuth Service Test', function () {
    beforeEach(module('adminApp', function ($provide, $translateProvider) {
        $provide.factory('customLoader', function ($q) {
            return function () {
                var deferred = $q.defer();
                deferred.resolve({});
                return deferred.promise;
            };
        });
        $translateProvider.useLoader('customLoader');

        $provide.factory('$location', function () {
            return {
                path: function (url) {
                }
            }
        });
    }));

    it('should not logged in before sign in', inject(function (oAuthSrv) {
        expect(oAuthSrv.isLoggedIn()).toBeFalsy();
        expect(oAuthSrv.getUsername()).toBe('');
    }));

    it('should logged in after sign in success', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/uas/oauth/accesstoken', {clientId: 'abc@abc.com', clientSecret: 'password'})
            .respond(200, {access_token: 'access', refresh_token: 'refresh'});

        oAuthSrv.signIn('abc@abc.com', 'password').then(function (name) {
            expect(name).toBe('abc');
        });
        $httpBackend.flush();

        expect($window.sessionStorage.getItem('access_token')).toBe('access');
        expect($window.sessionStorage.getItem('refresh_token')).toBe('refresh');
        expect($window.sessionStorage.getItem('username')).toBe('abc');
        expect(oAuthSrv.isLoggedIn()).toBeTruthy();
        expect(oAuthSrv.getUsername()).toBe('abc');
    }));

    it('should not logged in after sign out', inject(function ($httpBackend, oAuthSrv) {
        $httpBackend
            .expectPOST('/uas/oauth/accesstoken', {clientId: 'abc@abc.com', clientSecret: 'password'})
            .respond(200, {access_token: 'access', refresh_token: 'refresh'});

        oAuthSrv.signIn('abc@abc.com', 'password');
        $httpBackend.flush();

        expect(oAuthSrv.isLoggedIn()).toBeTruthy();
        oAuthSrv.signOut();
        expect(oAuthSrv.isLoggedIn()).toBeFalsy();
        expect(oAuthSrv.getUsername()).toBe('');
    }));

    it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, $window, $rootScope, oAuthSrv) {
        $httpBackend
            .expectPOST('/uas/oauth/accesstoken', {clientId: 'abc@abc.com', clientSecret: 'password'})
            .respond(404);

        oAuthSrv.signIn('abc@abc.com', 'password').then(
            null,
            function (e) {
                expect(e instanceof TimeOutException).toBeTruthy();
                expect(oAuthSrv.getUsername()).toBe('');
            }
        );
        $timeout.flush(3001);
        $httpBackend.flush();
    }));

    it('should return system bad network exception if bad network', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/uas/oauth/accesstoken', {clientId: 'abc@abc.com', clientSecret: 'password'})
            .respond(404);

        oAuthSrv.signIn('abc@abc.com', 'password').then(
            null,
            function (e) {
                expect(e instanceof BadNetworkException).toBeTruthy();
                expect(oAuthSrv.getUsername()).toBe('');
            }
        );
        $httpBackend.flush();
    }));

    it('should return authorize failed exception if sign in failed', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/uas/oauth/accesstoken', {clientId: 'abc@abc.com', clientSecret: 'password'})
            .respond(401);

        oAuthSrv.signIn('abc@abc.com', 'password').then(
            null,
            function (e) {
                expect(e instanceof AuthorizeFailedException).toBeTruthy();
                expect(oAuthSrv.getUsername()).toBe('');
            }
        );
        $httpBackend.flush();
    }));
});
