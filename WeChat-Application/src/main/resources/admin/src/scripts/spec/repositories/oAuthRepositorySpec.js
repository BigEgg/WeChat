describe('OAuth Repository Test', function () {
    beforeEach(angular.mock.module('adminApp'));

    it('can get and set Access Token', inject(function ($window, oAuthRepository) {
        var accessToken = oAuthRepository.getAccessToken();
        expect(accessToken).toBe('');

        oAuthRepository.setAccessToken('access');
        accessToken = oAuthRepository.getAccessToken();
        expect(accessToken).toBe('access');
        expect($window.sessionStorage.getItem('access_token')).toBe('access');
    }));

    it('can get and set Refresh Token', inject(function ($window, oAuthRepository) {
        var refreshToken = oAuthRepository.getRefreshToken();
        expect(refreshToken).toBe('');

        oAuthRepository.setRefreshToken('refresh');
        refreshToken = oAuthRepository.getRefreshToken();
        expect(refreshToken).toBe('refresh');
        expect($window.sessionStorage.getItem('refresh_token')).toBe('refresh');
    }));

    it('can get and set Username', inject(function ($window, oAuthRepository) {
        var username = oAuthRepository.getUsername();
        expect(username).toBe('');

        oAuthRepository.setUsername('username');
        username = oAuthRepository.getUsername();
        expect(username).toBe('username');
        expect($window.sessionStorage.getItem('username')).toBe('username');
    }));

    it('will remove all data after clear', inject(function ($window, oAuthRepository) {
        oAuthRepository.setAccessToken('access');
        oAuthRepository.setRefreshToken('refresh');
        oAuthRepository.setUsername('username');

        oAuthRepository.clearData();
        expect(oAuthRepository.getAccessToken()).toBe('');
        expect(oAuthRepository.getRefreshToken()).toBe('');
        expect(oAuthRepository.getUsername()).toBe('');
    }))
});