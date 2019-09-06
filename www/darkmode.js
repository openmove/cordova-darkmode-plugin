module.exports = {
    isDarkModeEnabled: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "DarkMode", "darkmode", []);
    },
    isInversionEnabled: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "DarkMode", "inversion", []);
    }	
};
