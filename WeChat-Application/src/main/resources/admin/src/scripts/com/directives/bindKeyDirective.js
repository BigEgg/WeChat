admin.app.directive('bindKey', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === Number(attrs.key)) {
                    scope.$apply(function () {
                        scope.$eval(attrs.bindKey, {'event': event});
                    });

                    event.preventDefault();
                }
            });
        }
    };
});