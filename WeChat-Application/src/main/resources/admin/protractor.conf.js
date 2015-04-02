exports.config = {
    directConnect: true,

    capabilities: {
        'browserName': 'chrome',
        'chromeOptions': {
            'args': ['incognito', 'disable-extensions', 'start-maximized']
        }
    },

    restartBrowserBetweenTests: true,

    specs: [
        'src/scripts/e2e/constants.js',
        'src/scripts/e2e/**/*Spec.js'
    ],

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    }
};
