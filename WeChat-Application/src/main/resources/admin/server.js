var server = require('node-http-server');

var config = server.configTemplate();
config.errors['404']    = 'These are not the files you are looking for...';
config.contentType.mp4  = 'video/mp4';
config.contentType.m4v  = 'video/mp4';
config.contentType.ogg  = 'video/ogg';
config.contentType.ogv  = 'video/ogg';
config.contentType.webm = 'video/webm';
config.port = 8001;
config.verbose = false;

console.log(server);
server.deploy(config);