admin.app.config(function ($routeProvider, $translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: '/i18n/locale-',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('zh_CN');

    $routeProvider
        .when('/', {
            controller: 'AppCtrl',
            templateUrl: '../html/views/home.html',
            publicAccess: true
        })
        .when('/dashboard', {
            controller: 'DashboardCtrl',
            templateUrl: '../html/views/dashboard.html'
        })
        .otherwise({
            redirectTo: '/'
        });
});