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
    }));

    it('should not logged in before sign in', inject(function (oAuthSrv) {
        expect(oAuthSrv.isLoggedIn()).toBeFalsy();
    }));

    it('should logged in after sign in success', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(200, {access_token: 'access', refresh_token: 'refresh', name: 'name'});
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        oAuthSrv.signIn('abc@abc.com', 'password').then(function (name) {
            expect(name).toBe('name');
        });
        $httpBackend.flush();

        expect($window.sessionStorage.getItem('access_token')).toBe('access');
        expect($window.sessionStorage.getItem('refresh_token')).toBe('refresh');
        expect(oAuthSrv.isLoggedIn()).toBeTruthy();
    }));

    it('should not logged in after sign out', inject(function ($httpBackend, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(200, {access_token: 'access', refresh_token: 'refresh', name: 'name'});
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        oAuthSrv.signIn('abc@abc.com', 'password');
        $httpBackend.flush();

        expect(oAuthSrv.isLoggedIn()).toBeTruthy();
        oAuthSrv.signOut();
        expect(oAuthSrv.isLoggedIn()).toBeFalsy();
    }));

    it('should return system bad network exception if bad network', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(404);
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        oAuthSrv.signIn('abc@abc.com', 'password').then(
            null,
            function (e) {
                expect(e instanceof SystemBadNetworkException).toBeTruthy();
            }
        );
        $httpBackend.flush();
    }));

    it('should return authorize failed exception if sign in failed', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(401);
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        oAuthSrv.signIn('abc@abc.com', 'password').then(
            null,
            function (e) {
                expect(e instanceof AuthorizeFailedException).toBeTruthy();
            }
        );
        $httpBackend.flush();
    }));
});
