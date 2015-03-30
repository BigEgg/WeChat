module.exports = function (grunt) {
    var BUILD_DIR = 'build/';
    var SRC_DIR = 'src/';

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        clean: {
            build: {
                src: [BUILD_DIR, SRC_DIR + 'scripts/main.js']
            }
        },
        copy: {
            build: {
                cwd: SRC_DIR,
                src: ['**', 'scripts/main.js', '!scripts/**', '!styles/less/**'],
                dest: BUILD_DIR,
                expand: true
            }
        },
        concat: {
            options: {
                separator: ';'
            },
            modules: {
                src: [
                    SRC_DIR + 'scripts/app.js',
                    SRC_DIR + 'scripts/config.js',
                    SRC_DIR + 'scripts/**/*.js'
                ],
                dest: SRC_DIR + 'scripts/main.js'
            }
        },
        jshint: {
            all: [
                'Gruntfile.js',
                SRC_DIR + 'com/*.js',
                SRC_DIR + 'com/**/*.js'
            ],
            options: {
                reporter: require('jshint-stylish'),
                globals: {
                    angular: true,
                    "_": true
                }
            }
        },
        less: {
            development: {
                options: {
                    cleancss: true
                },
                files: {
                    'src/styles/main.css': "src/styles/less/*.less"
                }
            }
        },
        jasmine: {
            module: {
                src: 'src/scripts/com/**/*.js',
                options: {
                    specs: 'src/scripts/spec/**/*Spec.js',
                    vendor: [
                        "vendor/scripts/angular.min.js",
                        "vendor/scripts/angular-route.min.js",
                        "vendor/scripts/angular-translate.min.js",
                        "vendor/scripts/angular-translate-loader-static-files.min.js",
                        "vendor/scripts/spec/angular-mocks.js"
                    ]
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-jasmine');

    grunt.registerTask('default', ['jshint', 'less', 'clean', 'concat', 'copy', 'jasmine']);
};