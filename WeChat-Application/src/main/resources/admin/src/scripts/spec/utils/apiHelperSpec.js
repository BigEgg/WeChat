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

        $provide.factory('$location', function () {
            return {
                path: function (url) {
                }
            }
        });
    }));

    describe('for POST methods', function () {
        it('should when success', inject(function ($httpBackend, $window, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var success = false;
            var error = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data, status, headers, config) {
                    success = true;
                },
                function (data, status, headers, config) {
                    error = true;
                }
            );
            $httpBackend.flush();

            expect(success).toBeTruthy();
            expect(error).toBeFalsy();
        }));

        it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(200, {test: 'value'});

            var success = false;
            var error = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data, status, headers, config) {
                    success = true;
                },
                function (data, status, headers, config) {
                    expect(data instanceof TimeOutException).toBeTruthy();
                    error = true;
                }
            );

            $timeout.flush(3001);
            $httpBackend.flush();

            expect(success).toBeFalsy();
            expect(error).toBeTruthy();
        }));

        it('should return system bad network exception if bad network', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectPOST('/api/test', {test: 'value'})
                .respond(404);

            var success = false;
            var error = false;
            apiHelper.post('/api/test', {test: 'value'}).then(
                function (data, status, headers, config) {
                    success = true;
                },
                function (data, status, headers, config) {
                    error = true;
                }
            );
            $httpBackend.flush();

            expect(success).toBeFalsy();
            expect(error).toBeTruthy();
        }));
    });

    describe('for GET methods', function () {
        it('should when success', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(200, {test: 'value'});

            var success = false;
            var error = false;
            apiHelper.get('/api/test').then(
                function (data, status, headers, config) {
                    success = true;
                },
                function (data, status, headers, config) {
                    error = true;
                }
            );
            $httpBackend.flush();

            expect(success).toBeTruthy();
            expect(error).toBeFalsy();
        }));

        it('should return time out exception if over 3 seconds', inject(function ($timeout, $httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(200, {test: 'value'});

            var success = false;
            var error = false;
            apiHelper.get('/api/test').then(
                function (data, status, headers, config) {
                    success = true;
                },
                function (data, status, headers, config) {
                    expect(data instanceof TimeOutException).toBeTruthy();
                    error = true;
                }
            );

            $timeout.flush(3001);
            $httpBackend.flush();

            expect(success).toBeFalsy();
            expect(error).toBeTruthy();
        }));

        it('should return system bad network exception if bad network', inject(function ($httpBackend, apiHelper) {
            $httpBackend
                .expectGET('/api/test')
                .respond(404);

            var success = false;
            var error = false;
            apiHelper.get('/api/test', {test: 'value'}).then(
                function (data, status, headers, config) {
                    success = true;
                },
                function (data, status, headers, config) {
                    error = true;
                }
            );
            $httpBackend.flush();

            expect(success).toBeFalsy();
            expect(error).toBeTruthy();
        }));
    });
});
