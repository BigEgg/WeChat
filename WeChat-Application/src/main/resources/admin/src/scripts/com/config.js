admin.app.config(function ($routeProvider, $translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: '/i18n/locale-',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('zh_CN');

    $routeProvider
        .when('/', {
            controller: 'AppCtrl',
            templateUrl: '../html/views/home.html'
        })
        .otherwise({
            redirectTo: '/'
        });
});