exports.config = {
    directConnect: true,

    capabilities: {
        'browserName': 'chrome',
        'chromeOptions': {
            'args': ['incognito', 'disable-extensions', 'start-maximized']
        },
        shardTestFiles: true,
        maxInstances: 5
    },

    chromeDriver: 'node_modules/chromedriver/lib/chromedriver/chromedriver',

    restartBrowserBetweenTests: true,

    specs: [
        'src/scripts/e2e/spec/**/*.js'
    ],

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    }
};
