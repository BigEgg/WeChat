describe('WeChat Basic Settings Service Test', function () {
    var mockWeChatBasicSettingsClient;

    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
            ignoreTranslate($provide, $translateProvider);
            ignoreRoute($provide);

            mockWeChatBasicSettingsClient = {
                getServerStatus: function () {
                },
                getDeveloperInfo: function () {
                }
            };
            $provide.value('weChatBasicSettingsClient', mockWeChatBasicSettingsClient);
        })
    );

    describe('when get WeChat server status', function () {
        it('return status when success', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getServerStatus').and.callFake(function () {
                var deferred = $q.defer();
                deferred.resolve({
                    entry_point: 'http://localhost:3000/wechat',
                    token: 'ABCDE_TOKEN',
                    connected: true
                });
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getServerStatus().then(
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
            expect(mockWeChatBasicSettingsClient.getServerStatus).toHaveBeenCalled();
        }));

        it('return error when failed', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getServerStatus').and.callFake(function () {
                var deferred = $q.defer();
                deferred.reject(new UnknownException());
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getServerStatus().then(
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
            expect(mockWeChatBasicSettingsClient.getServerStatus).toHaveBeenCalled();
        }));
    });

    describe('when get WeChat developer info', function () {
        it('return status when success', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getDeveloperInfo').and.callFake(function () {
                var deferred = $q.defer();
                deferred.resolve({
                    app_id: 'app_id',
                    app_secret: 'app_secret'
                });
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getDeveloperInfo().then(
                function (data) {
                    expect(data.app_id).toBe('app_id');
                    expect(data.app_secret).toBe('app_secret');
                    onSuccess = true;
                },
                function (error) {
                    onFailed = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeTruthy();
            expect(onFailed).toBeFalsy();
            expect(mockWeChatBasicSettingsClient.getDeveloperInfo).toHaveBeenCalled();
        }));

        it('return error when failed', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getDeveloperInfo').and.callFake(function () {
                var deferred = $q.defer();
                deferred.reject(new UnknownException());
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getDeveloperInfo().then(
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
            expect(mockWeChatBasicSettingsClient.getDeveloperInfo).toHaveBeenCalled();
        }));
    });
});