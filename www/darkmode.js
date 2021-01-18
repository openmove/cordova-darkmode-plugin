module.exports = {
    isDarkModeEnabled: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "DarkMode", "darkmode", []);
    },
    isInversionEnabled: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "DarkMode", "inversion", []);
    },
    registerCallback: function(setting, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "DarkMode", "register", [setting]);
    },
    unregisterCallback: function(setting, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "DarkMode", "unregister", [setting]);
    }
};
