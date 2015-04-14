describe('OAuth Service Test', function () {
    var mockOAuthClient, mockOAuthRepository;

    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);

        mockOAuthClient = {
            getAccessToken: function (clientId, clientSecret) {
            },
            refreshAccessToken: function (access_token, refresh_token) {
            }
        };
        $provide.value('oAuthClient', mockOAuthClient);

        mockOAuthRepository = {
            getAccessToken: function () {
            },
            setAccessToken: function (accessToken) {
            },
            getRefreshToken: function () {
            },
            setRefreshToken: function (refreshToken) {
            },
            getUsername: function () {
            },
            setUsername: function (username) {
            },
            clearData: function () {
            }
        };
        $provide.value('oAuthRepository', mockOAuthRepository);
    }));

    it('should not logged in before sign in', inject(function (oAuthSrv) {
        spyOn(mockOAuthRepository, 'getAccessToken').and.returnValues('');
        spyOn(mockOAuthRepository, 'getUsername').and.returnValues('');

        expect(oAuthSrv.isLoggedIn()).toBeFalsy();
        expect(oAuthSrv.getUsername()).toBe('');
        expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
        expect(mockOAuthRepository.getUsername).toHaveBeenCalled();
    }));

    it('should logged in after sign in success', inject(function ($rootScope, $q, oAuthSrv) {
        spyOn(mockOAuthClient, 'getAccessToken').and.callFake(function (clientId, clientSecret) {
            if (clientId === 'abc@abc.com' && clientSecret === 'password') {
                var deferred = $q.defer();
                deferred.resolve({access_token: 'access', refresh_token: 'refresh'});
                return deferred.promise;
            }
        });
        spyOn(mockOAuthRepository, 'setAccessToken');
        spyOn(mockOAuthRepository, 'setRefreshToken');
        spyOn(mockOAuthRepository, 'setUsername');

        var onSuccess = false;
        var onFailed = false;
        oAuthSrv.signIn('abc@abc.com', 'password').then(
            function (name) {
                expect(name).toBe('abc');
                onSuccess = true;
            },
            function (error) {
                onFailed = true;
            }
        );

        $rootScope.$apply();
        expect(onSuccess).toBeTruthy();
        expect(onFailed).toBeFalsy();
        expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('access');
        expect(mockOAuthRepository.setRefreshToken).toHaveBeenCalledWith('refresh');
        expect(mockOAuthRepository.setUsername).toHaveBeenCalledWith('abc');
    }));

    it('should remove logged in information after sign out', inject(function ($rootScope, $q, oAuthSrv) {
        spyOn(mockOAuthClient, 'getAccessToken').and.callFake(function (clientId, clientSecret) {
            if (clientId === 'abc@abc.com' && clientSecret === 'password') {
                var deferred = $q.defer();
                deferred.resolve({access_token: 'access', refresh_token: 'refresh'});
                return deferred.promise;
            }
        });

        oAuthSrv.signIn('abc@abc.com', 'password');
        $rootScope.$apply();

        spyOn(mockOAuthRepository, 'clearData');

        oAuthSrv.signOut();
        expect(mockOAuthRepository.clearData).toHaveBeenCalled();
    }));

    it('should return authorize failed exception if sign in failed', inject(function ($rootScope, $q, oAuthSrv) {
        spyOn(mockOAuthClient, 'getAccessToken').and.callFake(function (clientId, clientSecret) {
            if (clientId === 'abc@abc.com' && clientSecret === 'password') {
                var deferred = $q.defer();
                deferred.reject(new AuthorizeFailedException());
                return deferred.promise;
            }
        });
        spyOn(mockOAuthRepository, 'clearData');

        var onSuccess = false;
        var onFailed = false;
        oAuthSrv.signIn('abc@abc.com', 'password').then(
            function (name) {
                onSuccess = true;
            },
            function (error) {
                expect(error instanceof AuthorizeFailedException).toBeTruthy();
                onFailed = true;
            }
        );

        $rootScope.$apply();
        expect(onSuccess).toBeFalsy();
        expect(onFailed).toBeTruthy();
        expect(mockOAuthRepository.clearData).toHaveBeenCalled();
    }));
});
