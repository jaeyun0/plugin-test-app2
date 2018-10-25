cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "net.m3mobile.scan.m3scan",
        "file": "plugins/net.m3mobile.scan/www/m3scan.js",
        "pluginId": "net.m3mobile.scan",
        "clobbers": [
            "cordova.plugins.m3scan"
        ]
    },
    {
        "id": "com.icsfl.rfsmart.httpviausb.CordovaHttpViaUSBPlugin",
        "file": "plugins/com.icsfl.rfsmart.httpviausb/www/httpviausb.js",
        "pluginId": "com.icsfl.rfsmart.httpviausb",
        "clobbers": [
            "plugins.CordovaHttpViaUSBPlugin"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.3.3",
    "net.m3mobile.scan": "0.0.1",
    "com.icsfl.rfsmart.httpviausb": "0.1.0"
};
// BOTTOM OF METADATA
});