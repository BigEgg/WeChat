module.exports = function (grunt) {
    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        scriptsSrc: 'src/scripts',
        build: 'build',
        jshint: {
            all: [
                'Gruntfile.js',
                '<%= scriptsSrc %>/com/*.js',
                '<%= scriptsSrc %>/com/**/*.js'
            ],
            options: {
                reporter: require('jshint-stylish'),
                globals: {
                    angular: true,
                    "_": true
                }
            }
        }
    });

    // 加载包含 "uglify" 任务的插件。
    grunt.loadNpmTasks('grunt-contrib-jshint');

    // 默认被执行的任务列表。
    grunt.registerTask('default', ['jshint']);

};