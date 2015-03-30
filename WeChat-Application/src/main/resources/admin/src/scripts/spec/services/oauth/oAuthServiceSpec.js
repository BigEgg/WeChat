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
        expect(oAuthSrv.getUsername()).toBeNull();
    }));

    it('should logged in after sign in success', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(200, {access_token: 'access', refresh_token: 'refresh', name: 'name'});
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        oAuthSrv.signIn('abc@abc.com', 'password');
        $httpBackend.flush();

        expect($window.sessionStorage.getItem('access_token')).toBe('access');
        expect($window.sessionStorage.getItem('refresh_token')).toBe('refresh');
        expect($window.sessionStorage.getItem('user_name')).toEqual('name');
        expect(oAuthSrv.isLoggedIn()).toBeTruthy();
        expect(oAuthSrv.getUsername()).toBe('name');
    }));

    it('should throw system bad network exception if bad network', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(404);
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        try {
            oAuthSrv.signIn('abc@abc.com', 'password');
            $httpBackend.flush();
        } catch (e) {
            expect(e instanceof SystemBadNetworkException).toBeTruthy();
        }
    }));

    it('should throw authorize failed exception if sign in failed', inject(function ($httpBackend, $window, oAuthSrv) {
        $httpBackend
            .expectPOST('/api/oauth/admin', {username: 'abc@abc.com', password: 'password'})
            .respond(401);
        $httpBackend.expectGET('../html/views/home.html').respond(200, '');

        try {
            oAuthSrv.signIn('abc@abc.com', 'password');
            $httpBackend.flush();
        } catch (e) {
            expect(e instanceof AuthorizeFailedException).toBeTruthy();
        }
    }));
});
