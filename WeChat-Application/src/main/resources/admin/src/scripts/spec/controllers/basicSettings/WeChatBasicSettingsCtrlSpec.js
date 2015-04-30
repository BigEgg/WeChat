describe('WeChat Basic Settings Controller Test', function () {
    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);
    }));

    var $scope, notify, weChatBasicSettingsSrv, WeChatBasicSettingsCtrl;

    beforeEach(inject(function ($rootScope) {
        $scope = $rootScope.$new();
        weChatBasicSettingsSrv = {
            getConnectionStatus: function () {
            },
            getServerInfo: function () {
            },
            getDeveloperInfo: function () {
            },
            setDeveloperInfo: function (app_id, app_secret) {
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
        spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                server_connected: true,
                api_status: false
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                app_id: 'app_id',
                app_secret: 'app_secret'
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                entry_point: 'http://localhost:3000/wechat',
                token: 'ABCDE_TOKEN'
            });
            return deferred.promise;
        });

        WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
            $scope: $scope,
            notify: notify,
            weChatBasicSettingsSrv: weChatBasicSettingsSrv
        });

        $rootScope.$apply();

        expect(weChatBasicSettingsSrv.getConnectionStatus).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerInfo).toHaveBeenCalled();

        expect($scope.weChatConnectionStatus).not.toBeNull();
        expect($scope.weChatConnectionStatus.serverStatus).toBeTruthy();
        expect($scope.weChatConnectionStatus.apiStatus).toBeFalsy();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.isEditing).toBeFalsy();
        expect($scope.weChatDeveloperInfo.appId).toBe('app_id');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('app_secret');
        expect($scope.weChatDeveloperInfo.new_appId).toBe('');
        expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('http://localhost:3000/wechat');
        expect($scope.weChatServerStatus.appToken).toBe('ABCDE_TOKEN');

        expect($scope.status).not.toBeNull();
        expect($scope.status.gettingStatus).toBeFalsy();
        expect($scope.status.savingDeveloperInfo).toBeFalsy();
    }));

    it('should initiate part data when only get developer info', inject(function ($rootScope, $q, $controller) {
        spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                server_connected: true,
                api_status: false
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                app_id: 'app_id',
                app_secret: 'app_secret'
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
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

        expect(weChatBasicSettingsSrv.getConnectionStatus).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerInfo).toHaveBeenCalled();

        expect($scope.weChatConnectionStatus).not.toBeNull();
        expect($scope.weChatConnectionStatus.serverStatus).toBeTruthy();
        expect($scope.weChatConnectionStatus.apiStatus).toBeFalsy();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.isEditing).toBeFalsy();
        expect($scope.weChatDeveloperInfo.appId).toBe('app_id');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('app_secret');
        expect($scope.weChatDeveloperInfo.new_appId).toBe('');
        expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('');
        expect($scope.weChatServerStatus.appToken).toBe('');

        expect($scope.status).not.toBeNull();
        expect($scope.status.gettingStatus).toBeFalsy();
        expect($scope.status.savingDeveloperInfo).toBeFalsy();
    }));

    it('should have default data when can not get developer info', inject(function ($rootScope, $q, $controller) {
        spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                server_connected: true,
                api_status: false
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.reject(new UnknownException());
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                entry_point: 'http://localhost:3000/wechat',
                token: 'ABCDE_TOKEN'
            });
            return deferred.promise;
        });

        WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
            $scope: $scope,
            notify: notify,
            weChatBasicSettingsSrv: weChatBasicSettingsSrv
        });

        $rootScope.$apply();

        expect(weChatBasicSettingsSrv.getConnectionStatus).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerInfo).toHaveBeenCalled();

        expect($scope.weChatConnectionStatus).not.toBeNull();
        expect($scope.weChatConnectionStatus.serverStatus).toBeTruthy();
        expect($scope.weChatConnectionStatus.apiStatus).toBeFalsy();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.isEditing).toBeFalsy();
        expect($scope.weChatDeveloperInfo.appId).toBe('');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('');
        expect($scope.weChatDeveloperInfo.new_appId).toBe('');
        expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('http://localhost:3000/wechat');
        expect($scope.weChatServerStatus.appToken).toBe('ABCDE_TOKEN');

        expect($scope.status).not.toBeNull();
        expect($scope.status.gettingStatus).toBeFalsy();
        expect($scope.status.savingDeveloperInfo).toBeFalsy();
    }));

    it('should start edit developer info when developer info is empty', inject(function ($rootScope, $q, $controller) {
        spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                server_connected: true,
                api_status: false
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                app_id: '',
                app_secret: ''
            });
            return deferred.promise;
        });
        spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
            var deferred = $q.defer();
            deferred.resolve({
                entry_point: 'http://localhost:3000/wechat',
                token: 'ABCDE_TOKEN'
            });
            return deferred.promise;
        });

        WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
            $scope: $scope,
            notify: notify,
            weChatBasicSettingsSrv: weChatBasicSettingsSrv
        });

        $rootScope.$apply();

        expect(weChatBasicSettingsSrv.getConnectionStatus).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getDeveloperInfo).toHaveBeenCalled();
        expect(weChatBasicSettingsSrv.getServerInfo).toHaveBeenCalled();

        expect($scope.weChatConnectionStatus).not.toBeNull();
        expect($scope.weChatConnectionStatus.serverStatus).toBeTruthy();
        expect($scope.weChatConnectionStatus.apiStatus).toBeFalsy();

        expect($scope.weChatDeveloperInfo).not.toBeNull();
        expect($scope.weChatDeveloperInfo.isEditing).toBeTruthy();
        expect($scope.weChatDeveloperInfo.appId).toBe('');
        expect($scope.weChatDeveloperInfo.appSecret).toBe('');
        expect($scope.weChatDeveloperInfo.new_appId).toBe('');
        expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');

        expect($scope.weChatServerStatus).not.toBeNull();
        expect($scope.weChatServerStatus.entryPoint).toBe('http://localhost:3000/wechat');
        expect($scope.weChatServerStatus.appToken).toBe('ABCDE_TOKEN');

        expect($scope.status).not.toBeNull();
        expect($scope.status.gettingStatus).toBeFalsy();
        expect($scope.status.savingDeveloperInfo).toBeFalsy();
    }));

    describe('after initiation', function () {
        describe('can handle WeChat connection status refresh', function () {
            var statusType = 'data1';

            beforeEach(inject(function ($rootScope, $q, $controller) {
                spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
                    var deferred = $q.defer();
                    if (statusType === 'data1') {
                        deferred.resolve({
                            server_connected: true,
                            api_status: false
                        });
                    } else if (statusType === 'data2') {
                        deferred.resolve({
                            server_connected: false,
                            api_status: true
                        });
                    } else if (statusType === 'authorize') {
                        deferred.reject(new AuthorizeFailedException());
                    } else {
                        deferred.reject(new UnknownException());
                    }
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: 'app_id',
                        app_secret: 'app_secret'
                    });
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        entry_point: 'http://localhost:3000/wechat',
                        token: 'ABCDE_TOKEN'
                    });
                    return deferred.promise;
                });

                WeChatBasicSettingsCtrl = $controller('WeChatBasicSettingsCtrl', {
                    $scope: $scope,
                    notify: notify,
                    weChatBasicSettingsSrv: weChatBasicSettingsSrv
                });

                $rootScope.$apply();
            }));

            it('when can get server status', inject(function ($rootScope) {
                statusType = 'data2';

                $scope.getConnectionStatus();
                expect($scope.status.gettingStatus).toBeTruthy();

                $rootScope.$apply();
                expect($scope.status.gettingStatus).toBeFalsy();

                expect($scope.weChatConnectionStatus).not.toBeNull();
                expect($scope.weChatConnectionStatus.serverStatus).toBeFalsy();
                expect($scope.weChatConnectionStatus.apiStatus).toBeTruthy();
            }));

            it('when authorize failed', inject(function ($rootScope) {
                spyOn(notify, 'danger');
                statusType = 'authorize';

                $scope.getConnectionStatus();
                expect($scope.status.gettingStatus).toBeTruthy();

                $rootScope.$apply();
                expect($scope.status.gettingStatus).toBeFalsy();
                expect(notify.danger).toHaveBeenCalled();
            }));

            it('when other failed', inject(function ($rootScope) {
                spyOn(notify, 'warning');
                statusType = 'unknown';

                $scope.getConnectionStatus();
                expect($scope.status.gettingStatus).toBeTruthy();

                $rootScope.$apply();
                expect($scope.status.gettingStatus).toBeFalsy();
                expect(notify.warning).toHaveBeenCalled();
            }));
        });

        describe('can edit developer info', function () {
            beforeEach(inject(function ($rootScope, $q, $controller) {
                spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        server_connected: true,
                        api_status: false
                    });
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: 'app_id',
                        app_secret: 'app_secret'
                    });
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
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
            }));

            it('when start editing', function () {
                $scope.startEditDeveloperInfo();
                expect($scope.weChatDeveloperInfo.isEditing).toBeTruthy();
                expect($scope.weChatDeveloperInfo.new_appId).toBe('app_id');
                expect($scope.weChatDeveloperInfo.new_appSecret).toBe('app_secret');
            });

            it('when cancel editing', function () {
                $scope.startEditDeveloperInfo();
                $scope.cancelEditDeveloperInfo();
                expect($scope.weChatDeveloperInfo.isEditing).toBeFalsy();
                expect($scope.weChatDeveloperInfo.new_appId).toBe('');
                expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');
            });

            it('when save the new developer info', inject(function ($rootScope, $q) {
                spyOn(weChatBasicSettingsSrv, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: app_id,
                        app_secret: app_secret
                    });
                    return deferred.promise;
                });

                $scope.startEditDeveloperInfo();
                $scope.weChatDeveloperInfo.new_appId = 'new_app_id';
                $scope.weChatDeveloperInfo.new_appSecret = 'new_app_secret';

                $scope.saveNewDeveloperInfo();
                expect($scope.status.savingDeveloperInfo).toBeTruthy();

                $rootScope.$apply();
                expect(weChatBasicSettingsSrv.setDeveloperInfo).toHaveBeenCalledWith('new_app_id', 'new_app_secret');

                expect($scope.status.savingDeveloperInfo).toBeFalsy();
                expect($scope.weChatDeveloperInfo.isEditing).toBeFalsy();
                expect($scope.weChatDeveloperInfo.new_appId).toBe('');
                expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');
                expect($scope.weChatDeveloperInfo.appId).toBe('new_app_id');
                expect($scope.weChatDeveloperInfo.appSecret).toBe('new_app_secret');
            }));

            it('when save the new developer info but not in editing model', inject(function ($rootScope, $q) {
                spyOn(weChatBasicSettingsSrv, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: app_id,
                        app_secret: app_secret
                    });
                    return deferred.promise;
                });

                $scope.weChatDeveloperInfo.new_appId = 'new_app_id';
                $scope.weChatDeveloperInfo.new_appSecret = 'new_app_secret';

                $scope.saveNewDeveloperInfo();
                expect($scope.status.savingDeveloperInfo).toBeFalsy();

                $rootScope.$apply();
                expect(weChatBasicSettingsSrv.setDeveloperInfo).not.toHaveBeenCalled();
            }));

            it('when save the new developer info but new info is empty', inject(function ($rootScope, $q) {
                spyOn(weChatBasicSettingsSrv, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: app_id,
                        app_secret: app_secret
                    });
                    return deferred.promise;
                });

                $scope.startEditDeveloperInfo();
                $scope.weChatDeveloperInfo.new_appId = '';
                $scope.weChatDeveloperInfo.new_appSecret = '';

                $scope.saveNewDeveloperInfo();
                expect($scope.status.savingDeveloperInfo).toBeFalsy();

                $rootScope.$apply();
                expect(weChatBasicSettingsSrv.setDeveloperInfo).not.toHaveBeenCalled();
            }));

            it('when authorize failed', inject(function ($rootScope, $q) {
                spyOn(notify, 'danger');
                spyOn(weChatBasicSettingsSrv, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                    var deferred = $q.defer();
                    deferred.reject(new AuthorizeFailedException());
                    return deferred.promise;
                });

                $scope.startEditDeveloperInfo();
                $scope.weChatDeveloperInfo.new_appId = 'new_app_id';
                $scope.weChatDeveloperInfo.new_appSecret = 'new_app_secret';

                $scope.saveNewDeveloperInfo();
                $rootScope.$apply();
                expect($scope.status.savingDeveloperInfo).toBeFalsy();
                expect(notify.danger).toHaveBeenCalled();
            }));

            it('when other failed', inject(function ($rootScope, $q) {
                spyOn(notify, 'warning');
                spyOn(weChatBasicSettingsSrv, 'setDeveloperInfo').and.callFake(function (app_id, app_secret) {
                    var deferred = $q.defer();
                    deferred.reject(new AuthenticateFailedException());
                    return deferred.promise;
                });

                $scope.startEditDeveloperInfo();
                $scope.weChatDeveloperInfo.new_appId = 'new_app_id';
                $scope.weChatDeveloperInfo.new_appSecret = 'new_app_secret';

                $scope.saveNewDeveloperInfo();
                $rootScope.$apply();
                expect($scope.status.savingDeveloperInfo).toBeFalsy();
                expect(notify.warning).toHaveBeenCalled();
            }));
        });

        describe('can edit developer info', function () {
            it('cannot cancel editing if developer info is empty', inject(function ($rootScope, $q, $controller) {
                spyOn(weChatBasicSettingsSrv, 'getConnectionStatus').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        server_connected: true,
                        api_status: false
                    });
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getDeveloperInfo').and.callFake(function () {
                    var deferred = $q.defer();
                    deferred.resolve({
                        app_id: '',
                        app_secret: ''
                    });
                    return deferred.promise;
                });
                spyOn(weChatBasicSettingsSrv, 'getServerInfo').and.callFake(function () {
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
                expect($scope.weChatDeveloperInfo.isEditing).toBeTruthy();
                expect($scope.weChatDeveloperInfo.new_appId).toBe('');
                expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');

                $scope.cancelEditDeveloperInfo();
                expect($scope.weChatDeveloperInfo.isEditing).toBeTruthy();
                expect($scope.weChatDeveloperInfo.new_appId).toBe('');
                expect($scope.weChatDeveloperInfo.new_appSecret).toBe('');
            }));
        });
    });
});