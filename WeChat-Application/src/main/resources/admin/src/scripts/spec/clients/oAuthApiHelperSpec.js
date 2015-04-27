describe('OAuth API Helper Test', function () {
    var mockApiHelper, mockOAuthClient, mockOAuthRepository;

    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);

        mockApiHelper = {
            get: function (url) {
            },
            post: function (url, data) {
            },
            put: function (url, data) {
            },
            patch: function (url, data) {
            },
            addParameterToURL: function (url, key, value) {
            }
        };
        $provide.value('apiHelper', mockApiHelper);

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

    describe('When no access token', function () {
        describe('for GET method', function () {
            it('should throw Authenticate Failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('');

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('/api/test').then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthenticateFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
            }));
        });

        describe('for POST method', function () {
            it('should throw Authenticate Failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('');

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthenticateFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
            }));
        });

        describe('for PUT method', function () {
            it('should throw Authenticate Failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('');

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.put('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthenticateFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
            }));
        });

        describe('for PATCH method', function () {
            it('should throw Authenticate Failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('');

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.patch('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthenticateFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
            }));
        });
    });

    describe('When access token valid', function () {
        describe('for GET method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('/api/test').then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('/api/test').then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
            }));
        });

        describe('for POST method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'post').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'post').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));
        });

        describe('for PUT method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'put').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.put('/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'put').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.put('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));
        });

        describe('for PATCH method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'patch').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.patch('/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockApiHelper, 'addParameterToURL').and.returnValue('/api/test?access_token=access');
                spyOn(mockApiHelper, 'patch').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.patch('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
            }));
        });
    });

    describe('when access token invalid and can refresh token', function () {
        describe('for GET method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('/api/test').then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=new_access');
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.reject(404);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('/api/test').then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=new_access');
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for POST method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for PUT method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'put').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.put('/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'put').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.put('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for PATCH method', function () {
            it('should success when success', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'patch').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.resolve({test: 'value'});
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.patch('/api/test', {test: 'value'}).then(
                    function (data) {
                        expect(data.test).toBe('value');
                        onSuccess = true;
                    },
                    function (error) {
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeTruthy();
                expect(onError).toBeFalsy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));

            it('should error if get error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'setAccessToken');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                    if (value === 'new_access') {
                        return '/api/test?access_token=new_access'
                    }
                });
                spyOn(mockApiHelper, 'patch').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                    if (url === '/api/test?access_token=new_access') {
                        var deferred = $q.defer();
                        deferred.reject(403);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.resolve('new_access');
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.patch('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof UnknownException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.setAccessToken).toHaveBeenCalledWith('new_access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'new_access');
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=new_access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });
    });

    describe('when both access token and refresh token are invalid ', function () {
        describe('for GET method', function () {
            it('should throw authenticate failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'clearData');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                });
                spyOn(mockApiHelper, 'get').and.callFake(function (url) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.reject(new AuthorizeFailedException());
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.get('/api/test').then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthorizeFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.clearData).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.get).toHaveBeenCalledWith('/api/test?access_token=access');
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for POST method', function () {
            it('should throw authenticate failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'clearData');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                });
                spyOn(mockApiHelper, 'post').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.reject(new AuthorizeFailedException());
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.post('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthorizeFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.clearData).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.post).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for PUT method', function () {
            it('should throw authenticate failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'clearData');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                });
                spyOn(mockApiHelper, 'put').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.reject(new AuthorizeFailedException());
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.put('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthorizeFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.clearData).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.put).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });

        describe('for PATCH method', function () {
            it('should throw authenticate failed error', inject(function ($rootScope, $q, oAuthApiHelper) {
                spyOn(mockOAuthRepository, 'getAccessToken').and.returnValue('access');
                spyOn(mockOAuthRepository, 'getRefreshToken').and.returnValue('refresh');
                spyOn(mockOAuthRepository, 'clearData');
                spyOn(mockApiHelper, 'addParameterToURL').and.callFake(function (url, parameter, value) {
                    if (value === 'access') {
                        return '/api/test?access_token=access'
                    }
                });
                spyOn(mockApiHelper, 'patch').and.callFake(function (url, data) {
                    if (url === '/api/test?access_token=access') {
                        var deferred = $q.defer();
                        deferred.reject(500);
                        return deferred.promise;
                    }
                });
                spyOn(mockOAuthClient, 'refreshAccessToken').and.callFake(function (access_token, refresh_token) {
                    if (refresh_token === 'refresh' && access_token === 'access') {
                        var deferred = $q.defer();
                        deferred.reject(new AuthorizeFailedException());
                        return deferred.promise;
                    }
                });

                var onSuccess = false;
                var onError = false;
                oAuthApiHelper.patch('/api/test', {test: 'value'}).then(
                    function (data) {
                        onSuccess = true;
                    },
                    function (error) {
                        expect(error instanceof AuthorizeFailedException).toBeTruthy();
                        onError = true;
                    }
                );

                $rootScope.$apply();
                expect(onSuccess).toBeFalsy();
                expect(onError).toBeTruthy();
                expect(mockOAuthRepository.getAccessToken).toHaveBeenCalled();
                expect(mockOAuthRepository.getRefreshToken).toHaveBeenCalled();
                expect(mockOAuthRepository.clearData).toHaveBeenCalled();
                expect(mockApiHelper.addParameterToURL).toHaveBeenCalledWith('/api/test', 'access_token', 'access');
                expect(mockApiHelper.patch).toHaveBeenCalledWith('/api/test?access_token=access', {test: 'value'});
                expect(mockOAuthClient.refreshAccessToken).toHaveBeenCalledWith('access', 'refresh');
            }));
        });
    });
});