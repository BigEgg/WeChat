describe('WeChat Basic Settings Client Test', function () {
    var mockOAuthApiHelper;

    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
            ignoreTranslate($provide, $translateProvider);
            ignoreRoute($provide);

            mockOAuthApiHelper = {
                get: function (url) {
                },
                post: function (url, data) {
                }
            };
            $provide.value('oAuthApiHelper', mockOAuthApiHelper);
        })
    );

    describe('when get WeChat server status', function () {
        it('return status when success', inject(function ($rootScope, $q, weChatBasicSettingsClient) {
            spyOn(mockOAuthApiHelper, 'get').and.callFake(function (url) {
                if (url === '/api/admin/wechat/server') {
                    var deferred = $q.defer();
                    deferred.resolve({
                        entry_point: 'http://localhost:3000/wechat',
                        token: 'ABCDE_TOKEN',
                        connected: true
                    });
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsClient.getWeChatServerStatus().then(
                function (data) {
                    expect(data.entry_point).toBe('http://localhost:3000/wechat');
                    expect(data.token).toBe('ABCDE_TOKEN');
                    expect(data.connected).toBe(true);
                    onSuccess = true;
                },
                function (error) {
                    onFailed = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeTruthy();
            expect(onFailed).toBeFalsy();
            expect(mockOAuthApiHelper.get).toHaveBeenCalledWith('/api/admin/wechat/server');
        }));

        it('return error when failed', inject(function ($rootScope, $q, weChatBasicSettingsClient) {
            spyOn(mockOAuthApiHelper, 'get').and.callFake(function (url) {
                if (url === '/api/admin/wechat/server') {
                    var deferred = $q.defer();
                    deferred.reject(500);
                    return deferred.promise;
                }
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsClient.getWeChatServerStatus().then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof UnknownException).toBeTruthy();
                    onFailed = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeFalsy();
            expect(onFailed).toBeTruthy();
            expect(mockOAuthApiHelper.get).toHaveBeenCalledWith('/api/admin/wechat/server');
        }));
    });
});