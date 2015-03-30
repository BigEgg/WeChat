admin.app.factory('Notify', function ($filter) {
    var translate = $filter('translate');

    var sendNotify = function (icon, title, message, type) {
        $.notify(
            {
                icon: icon,
                title: '<strong>' + translate(title) + '</strong>',
                message: translate(message),
                url: '',
                target: '_blank'
            },
            {
                type: type,
                allow_dismiss: true,
                newest_on_top: true,
                placement: {
                    from: 'top',
                    align: 'center'
                },
                offset: {
                    x: 20,
                    y: 60
                },
                spacing: 10,
                z_index: 1000,
                delay: 3000,
                mouse_over: null,
                template: '<div data-notify="container" style="padding: 0;" class="col-xs-4 alert alert-{0}" role="alert">' +
                '<button type="button" style="margin-right: 5px;" aria-hidden="true" class="close" data-notify="dismiss">×</button>' +
                '<div style="margin: 10px 15px 10px 15px;">' +
                '<span style="margin-right: 8px;" data-notify="icon"></span>' +
                '<span style="margin-right: 8px;" data-notify="title"><strong>{1}</strong></span>' +
                '<span style="vertical-align: middle; font-size: 14px;" data-notify="message">{2}</span>' +
                '</div>' +
                '<div class="progress" style="height: 3px; margin-bottom: 0;" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '</div>'
            }
        );
    };

    var notify = {};
    notify.success = function (title, message) {
        sendNotify('fui-check-circle', title, message, 'success');
    };
    notify.success = function (message) {
        sendNotify('fui-check-circle', 'notify.title.success', message, 'success');
    };
    notify.info = function (title, message) {
        sendNotify('fui-info-circle', title, message, 'info');
    };
    notify.info = function (message) {
        sendNotify('fui-info-circle', 'notify.title.info', message, 'info');
    };
    notify.warning = function (title, message) {
        sendNotify('fui-question-circle', title, message, 'warning');
    };
    notify.warning = function (message) {
        sendNotify('fui-question-circle', 'notify.title.warning', message, 'warning');
    };
    notify.danger = function (title, message) {
        sendNotify('fui-alert-circle', title, message, 'danger');
    };
    notify.danger = function (message) {
        sendNotify('fui-alert-circle', 'notify.title.danger', message, 'danger');
    };

    return notify;
})
;

