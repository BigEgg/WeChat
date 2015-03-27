admin.app.config(function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: '/i18n/locale-',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('zh_CN');
});