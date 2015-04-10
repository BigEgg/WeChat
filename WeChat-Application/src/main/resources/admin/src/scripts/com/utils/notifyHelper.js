admin.app.factory('notify', function ($filter) {
    var translate = $filter('translate');

    var sendNotify = function (icon, title, message, type, delay) {
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
                delay: delay,
                mouse_over: null,
                template: '<div data-notify="container" class="col-xs-4 alert alert-{0} notify-container" role="alert">' +
                '<button type="button" aria-hidden="true" class="close dismiss-button" data-notify="dismiss">×</button>' +
                '<div class="notify-data">' +
                '<span class="notify-data-icon" data-notify="icon"></span>' +
                '<span class="notify-data-title" data-notify="title"><strong>{1}</strong></span>' +
                '<span class="notify-data-message" data-notify="message">{2}</span>' +
                '</div>' +
                '<div class="progress notify-progress" data-notify="progressbar">' +
                '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
                '</div>' +
                '</div>'
            }
        );
    };

    var notify = {};
    notify.success = function (message, title, delay) {
        sendNotify('fui-check-circle', title || 'notify.title.success', message, 'success', delay || 3000);
    };
    notify.info = function (message, title, delay) {
        sendNotify('fui-info-circle', title || 'notify.title.info', message, 'info', delay || 3000);
    };
    notify.warning = function (message, title, delay) {
        sendNotify('fui-question-circle', title || 'notify.title.warning', message, 'warning', delay || 3000);
    };
    notify.danger = function (message, title, delay) {
        sendNotify('fui-alert-circle', title || 'notify.title.danger', message, 'danger', delay || 3000);
    };

    return notify;
});

