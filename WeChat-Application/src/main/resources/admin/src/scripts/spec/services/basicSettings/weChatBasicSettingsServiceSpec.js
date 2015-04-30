describe('WeChat Basic Settings Service Test', function () {
    var mockWeChatBasicSettingsClient;

    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);

        mockWeChatBasicSettingsClient = {
            getConnectionStatus: function () {
            },
            getServerInfo: function () {
            },
            getDeveloperInfo: function () {
            },
            setDeveloperInfo: function () {
            }
        };
        $provide.value('weChatBasicSettingsClient', mockWeChatBasicSettingsClient);
    }));

    describe('when get WeChat connection status', function () {
        it('return status when success', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getConnectionStatus').and.callFake(function () {
                var deferred = $q.defer();
                deferred.resolve({
                    server_connected: true,
                    api_status: false
                });
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getConnectionStatus().then(
                function (data) {
                    expect(data.server_connected).toBeTruthy();
                    expect(data.api_status).toBeFalsy();
                    onSuccess = true;
                },
                function (error) {
                    onFailed = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeTruthy();
            expect(onFailed).toBeFalsy();
            expect(mockWeChatBasicSettingsClient.getConnectionStatus).toHaveBeenCalled();
        }));

        it('return error when failed', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getConnectionStatus').and.callFake(function () {
                var deferred = $q.defer();
                deferred.reject(new UnknownException());
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getConnectionStatus().then(
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
            expect(mockWeChatBasicSettingsClient.getConnectionStatus).toHaveBeenCalled();
        }));
    });

    describe('when get WeChat server info', function () {
        it('return server info when success', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getServerInfo').and.callFake(function () {
                var deferred = $q.defer();
                deferred.resolve({
                    entry_point: 'http://localhost:3000/wechat',
                    token: 'ABCDE_TOKEN'
                });
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getServerInfo().then(
                function (data) {
                    expect(data.entry_point).toBe('http://localhost:3000/wechat');
                    expect(data.token).toBe('ABCDE_TOKEN');
                    onSuccess = true;
                },
                function (error) {
                    onFailed = true;
                }
            );

            $rootScope.$apply();
            expect(onSuccess).toBeTruthy();
            expect(onFailed).toBeFalsy();
            expect(mockWeChatBasicSettingsClient.getServerInfo).toHaveBeenCalled();
        }));

        it('return error when failed', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'getServerInfo').and.callFake(function () {
                var deferred = $q.defer();
                deferred.reject(new UnknownException());
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.getServerInfo().then(
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
            expect(mockWeChatBasicSettingsClient.getServerInfo).toHaveBeenCalled();
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

    describe('when set WeChat developer info', function () {
        it('return status when success', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                var deferred = $q.defer();
                deferred.resolve({
                    app_id: app_id,
                    app_secret: app_secret
                });
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.setDeveloperInfo('app_id', 'app_secret').then(
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
            expect(mockWeChatBasicSettingsClient.setDeveloperInfo).toHaveBeenCalledWith('app_id', 'app_secret');
        }));

        it('return error when failed', inject(function ($rootScope, $q, weChatBasicSettingsSrv) {
            spyOn(mockWeChatBasicSettingsClient, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                var deferred = $q.defer();
                deferred.reject(new UnknownException());
                return deferred.promise;
            });

            var onSuccess = false;
            var onFailed = false;
            weChatBasicSettingsSrv.setDeveloperInfo('app_id', 'app_secret').then(
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
            expect(mockWeChatBasicSettingsClient.setDeveloperInfo).toHaveBeenCalledWith('app_id', 'app_secret');
        }));
    });
});