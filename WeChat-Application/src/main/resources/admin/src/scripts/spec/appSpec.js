describe("App Initiation Test", function () {
    beforeEach(angular.mock.module("adminApp"));

    it('should initialize correctly',
        function () {
            expect(admin.app).toBeDefined();
        }
    );
});