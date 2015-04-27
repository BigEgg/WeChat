describe('OAuth Service Test', function () {
    beforeEach(angular.mock.module('adminApp', function ($provide, $translateProvider) {
        ignoreTranslate($provide, $translateProvider);
        ignoreRoute($provide);
    }));

    describe('for GET methods', function () {
        it('should success when success', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.get('/api/test').then(
                function (data) {
                    expect(data.test).toBe('value');
                    onSuccess = true;
                },
                function (error) {
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeTruthy();
            expect(onError).toBeFalsy();
        }));

        it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.get('/api/test').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof TimeOutException).toBeTruthy();
                    onError = true;
                }
            );

            $timeout.flush(3001);
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return system bad network exception if bad network', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(404);

            var onSuccess = false;
            var onError = false;
            apiHelper.get('/api/test').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof BadNetworkException).toBeTruthy();
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return http status code if return error', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(500);

            var onSuccess = false;
            var onError = false;
            apiHelper.get('/api/test').then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error).toBe(500);
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));
    });

    describe('for POST methods', function () {
        it('should success when success', inject(function ($httpBackend, $window, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data) {
                    expect(data.test).toBe('value');
                    onSuccess = true;
                },
                function (error) {
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeTruthy();
            expect(onError).toBeFalsy();
        }));

        it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof TimeOutException).toBeTruthy();
                    onError = true;
                }
            );

            $timeout.flush(3001);
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return system bad network exception if bad network', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(404);

            var onSuccess = false;
            var onError = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof BadNetworkException).toBeTruthy();
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return http status code if return error', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(500);

            var onSuccess = false;
            var onError = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error).toBe(500);
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));
    });

    describe('for PUT methods', function () {
        it('should success when success', inject(function ($httpBackend, $window, apiHelper) {
            $httpBackend
                .expectPUT('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.put('/api/test', {test: 'value'}).then(
                function (data) {
                    expect(data.test).toBe('value');
                    onSuccess = true;
                },
                function (error) {
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeTruthy();
            expect(onError).toBeFalsy();
        }));

        it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, apiHelper) {
            $httpBackend
                .expectPUT('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.put('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof TimeOutException).toBeTruthy();
                    onError = true;
                }
            );

            $timeout.flush(3001);
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return system bad network exception if bad network', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPUT('/api/test', {test: 'value'})
                .respond(404);

            var onSuccess = false;
            var onError = false;
            apiHelper.put('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof BadNetworkException).toBeTruthy();
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return http status code if return error', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPUT('/api/test', {test: 'value'})
                .respond(500);

            var onSuccess = false;
            var onError = false;
            apiHelper.put('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error).toBe(500);
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));
    });

    describe('for PATCH methods', function () {
        it('should success when success', inject(function ($httpBackend, $window, apiHelper) {
            $httpBackend
                .expectPATCH('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.patch('/api/test', {test: 'value'}).then(
                function (data) {
                    expect(data.test).toBe('value');
                    onSuccess = true;
                },
                function (error) {
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeTruthy();
            expect(onError).toBeFalsy();
        }));

        it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, apiHelper) {
            $httpBackend
                .expectPATCH('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var onSuccess = false;
            var onError = false;
            apiHelper.patch('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof TimeOutException).toBeTruthy();
                    onError = true;
                }
            );

            $timeout.flush(3001);
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return system bad network exception if bad network', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPATCH('/api/test', {test: 'value'})
                .respond(404);

            var onSuccess = false;
            var onError = false;
            apiHelper.patch('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error instanceof BadNetworkException).toBeTruthy();
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));

        it('should return http status code if return error', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPATCH('/api/test', {test: 'value'})
                .respond(500);

            var onSuccess = false;
            var onError = false;
            apiHelper.patch('/api/test', {test: 'value'}).then(
                function (data) {
                    onSuccess = true;
                },
                function (error) {
                    expect(error).toBe(500);
                    onError = true;
                }
            );
            $httpBackend.flush();

            expect(onSuccess).toBeFalsy();
            expect(onError).toBeTruthy();
        }));
    });

    describe('for Add parameter', function () {
        it('when url without parameter', inject(function (apiHelper) {
            var newUrl = apiHelper.addParameterToURL('http://a.com/abc', 'key', 'value');
            expect(newUrl).toBe('http://a.com/abc?key=value');
        }));

        it('when url with parameter', inject(function (apiHelper) {
            var newUrl = apiHelper.addParameterToURL('http://a.com/abc?k1=v1', 'key', 'value');
            expect(newUrl).toBe('http://a.com/abc?k1=v1&key=value');
        }));
    });
});
