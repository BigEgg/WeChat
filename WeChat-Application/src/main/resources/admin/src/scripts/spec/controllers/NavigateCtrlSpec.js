describe('App Controller Test', function () {
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

    var $scope, $location, oAuthSrv, notify, NavigateCtrl;

    beforeEach(inject(function ($rootScope, $controller) {
        $scope = $rootScope.$new();
        $location = {
            path: function (url) {
            }
        };
        oAuthSrv = {
            isLoggedIn: function () {
            },
            signIn: function (username, password) {
            },
            signOut: function () {
            }
        };
        notify = {
            warning: function (message) {
            },
            danger: function (message) {
            }
        };

        NavigateCtrl = $controller('NavigateCtrl', {$scope: $scope, $location: $location, oAuthSrv: oAuthSrv, notify: notify});
    }));

    it('should set initiate stats', inject(function ($rootScope) {
        expect($scope.status).toBeDefined();
        expect($scope.status.logging).toBeFalsy();
    }));

    describe('should return same logged in status as OAuth Service', function () {
        it('when logged in', function () {
            spyOn(oAuthSrv, 'isLoggedIn').and.returnValue(true);

            expect($scope.isLoggedIn()).toBeTruthy();
            expect(oAuthSrv.isLoggedIn).toHaveBeenCalled();
        });

        it('when not logged in', function () {
            spyOn(oAuthSrv, 'isLoggedIn').and.returnValue(false);

            expect($scope.isLoggedIn()).toBeFalsy();
            expect(oAuthSrv.isLoggedIn).toHaveBeenCalled();
        })
    });

    it('should return to home page after sign out', function () {
        spyOn(oAuthSrv, 'signOut');
        spyOn($location, 'path');

        $scope.signOut();

        expect($location.path).toHaveBeenCalledWith('/');
        expect(oAuthSrv.signOut).toHaveBeenCalled();
        expect($location.path).toHaveBeenCalled();
    });

    describe('should handle both sign in success and error', function () {
        it('when sign in success', inject(function ($q, $rootScope, $httpBackend) {
            spyOn(oAuthSrv, 'signIn').and.callFake(function () {
                var deferred = $q.defer();
                deferred.resolve('name');
                return deferred.promise;
            });
            spyOn($location, 'path');

            $scope.signIn('username', 'password');
            expect($scope.status.logging).toBeTruthy();

            $rootScope.$apply();
            expect($scope.status.logging).toBeFalsy();
            expect($location.path).toHaveBeenCalledWith('/dashboard');
        }));

        it('when authorize failed', inject(function ($q, $rootScope, $httpBackend) {
            spyOn(oAuthSrv, 'signIn').and.callFake(function () {
                var deferred = $q.defer();
                deferred.reject(new AuthorizeFailedException());
                return deferred.promise;
            });
            spyOn(notify, 'warning');

            $scope.signIn('username', 'password');
            expect($scope.status.logging).toBeTruthy();
            $rootScope.$apply();
            expect($scope.status.logging).toBeFalsy();
            expect(notify.warning).toHaveBeenCalled();
        }));

        it('when bad network', inject(function ($q, $rootScope, $httpBackend) {
            spyOn(oAuthSrv, 'signIn').and.callFake(function () {
                var deferred = $q.defer();
                deferred.reject(new BadNetworkException());
                return deferred.promise;
            });
            spyOn(notify, 'danger');

            $scope.signIn('username', 'password');
            expect($scope.status.logging).toBeTruthy();
            $rootScope.$apply();
            expect($scope.status.logging).toBeFalsy();
            expect(notify.danger).toHaveBeenCalled();
        }));
    });
});
