admin.app.config(function ($routeProvider, $translateProvider) {
    $translateProvider
        .useStaticFilesLoader({
            prefix: '/i18n/locale-',
            suffix: '.json'
        })
        .preferredLanguage('zh_CN');

    $routeProvider
        .when('/', {
            controller: 'NavigateCtrl',
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