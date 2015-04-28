describe('WeChat Basic Settings Controller Test', function () {
    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);
    }));

    var $scope, notify, weChatBasicSettingsSrv, WeChatBasicSettingsCtrl;

    beforeEach(inject(function ($rootScope) {
        $scope = $rootScope.$new();
        weChatBasicSettingsSrv = {
            getServerStatus: function () {
            },
            getDeveloperInfo: function () {
            }
        };
        notify = {
            warning: function (message) {
            },
            danger: function (message) {
            }
        };
    }));

    it('should initiate data when can get both developer info and server status', inject(function ($rootScope, $q, $controller) {
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                app_id: 'app_id',
                app_secret: 'app_secret'
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                entry_point: 'http://localhost:3000/wechat',
                token: 'ABCDE_TOKEN',
                connected: true
            });
            return deferred.promise;
        });

        WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
            $scope: $scope,
            notify: notify,
            weChatBasicSettingsSrv: weChatBasicSettingsSrv
        });

        $rootScope.$apply();

        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerStatus).toHaveBeenCalled();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.appId).toBe('app_id');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('app_secret');

        expect($scope.status).not.toBeNull();
        expect($scope.status.loading).toBeFalsy();

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('http://localhost:3000/wechat');
        expect($scope.weChatServerStatus.appToken).toBe('ABCDE_TOKEN');
        expect($scope.weChatServerStatus.connectionStatus).toBeTruthy();

        expect($scope.status.statusGetting).toBeFalsy();
    }));

    it('should initiate part data when only get developer info', inject(function ($rootScope, $q, $controller) {
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                app_id: 'app_id',
                app_secret: 'app_secret'
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.reject(new UnknownException());
            return deferred.promise;
        });

        WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
            $scope: $scope,
            notify: notify,
            weChatBasicSettingsSrv: weChatBasicSettingsSrv
        });

        $rootScope.$apply();

        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerStatus).toHaveBeenCalled();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.appId).toBe('app_id');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('app_secret');

        expect($scope.status).not.toBeNull();
        expect($scope.status.loading).toBeFalsy();

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('');
        expect($scope.weChatServerStatus.appToken).toBe('');
        expect($scope.weChatServerStatus.connectionStatus).toBeFalsy();

        expect($scope.status.statusGetting).toBeFalsy();
    }));

    it('should have default data when can not get developer info', inject(function ($rootScope, $q, $controller) {
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.reject(new UnknownException());
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                entry_point: 'http://localhost:3000/wechat',
                token: 'ABCDE_TOKEN',
                connected: true
            });
            return deferred.promise;
        });

        WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
            $scope: $scope,
            notify: notify,
            weChatBasicSettingsSrv: weChatBasicSettingsSrv
        });

        $rootScope.$apply();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.appId).toBe('');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('');

        expect($scope.status).not.toBeNull();
        expect($scope.status.loading).toBeFalsy();

        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerStatus).not.toHaveBeenCalled();

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('');
        expect($scope.weChatServerStatus.appToken).toBe('');
        expect($scope.weChatServerStatus.connectionStatus).toBeFalsy();

        expect($scope.status.statusGetting).toBeFalsy();
    }));

    describe('after initiation', function () {
        describe('can handle server status refresh', function () {
            var statusType = 'data1';

            beforeEach(inject(function ($rootScope, $q, $controller) {
                spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: 'app_id',
                        app_secret: 'app_secret'
                    });
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getServerStatus').and.callFake(function () {
                    var deferred = $q.defer();
                    if (statusType === 'data1') {
                        deferred.resolve({
                            entry_point: 'http://localhost:3000/wechat',
                            token: 'ABCDE_TOKEN',
                            connected: true
                        });
                    }
                    if (statusType === 'data2') {
                        deferred.resolve({
                            entry_point: 'http://localhost:3001/wechat',
                            token: 'ABCDE_TOKEN2',
                            connected: true
                        });
                    }
                    if (statusType === 'authorize') {
                        deferred.reject(new AuthorizeFailedException());
                    }
                    if (statusType === 'unknown') {
                        deferred.reject(new UnknownException());
                    }
                    return deferred.promise;
                });

                WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
                    $scope: $scope,
                    notify: notify,
                    weChatBasicSettingsSrv: weChatBasicSettingsSrv
                });

                $rootScope.$apply();
            }));

            it('when can get server status', inject(function ($rootScope, $q) {
                statusType = 'data2';

                $scope.getServerStatus();
                expect($scope.status.statusGetting).toBeTruthy();

                $rootScope.$apply();
                expect($scope.status.statusGetting).toBeFalsy();

                expect($scope.weChatServerStatus.entryPoint).toBe('http://localhost:3001/wechat');
                expect($scope.weChatServerStatus.appToken).toBe('ABCDE_TOKEN2');
                expect($scope.weChatServerStatus.connectionStatus).toBeTruthy();
            }));

            it('when authenticate failed get server status', inject(function ($rootScope, $q) {
                spyOn(notify, 'danger');
                statusType = 'authorize';

                $scope.getServerStatus();
                expect($scope.status.statusGetting).toBeTruthy();

                $rootScope.$apply();
                expect($scope.status.statusGetting).toBeFalsy();
                expect(notify.danger).toHaveBeenCalled();
            }));

            it('when other failed get server status', inject(function ($rootScope, $q) {
                spyOn(notify, 'warning');
                statusType = 'unknown';

                $scope.getServerStatus();
                expect($scope.status.statusGetting).toBeTruthy();

                $rootScope.$apply();
                expect($scope.status.statusGetting).toBeFalsy();
                expect(notify.warning).toHaveBeenCalled();
            }));
        })
    });
});