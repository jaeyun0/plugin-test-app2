var exec = require('cordova/exec');

var httpviausb = {
    get: function(url, data, headers, success, failure) {
        return exec(success, failure, "CordovaHttpViaUSBPlugin", "get", [url, data, headers]);
    },
    postJson: function(url, data, headers, success, failure) {
        return exec(success, failure, "CordovaHttpViaUSBPlugin", "post", [url, data, headers]);
    }
};

module.exports = httpviausb;

window.httpviausb = httpviausb;