# cordova-http-via-usb
Cordova plugin to forward http request information over a socket to the host machine so the host machine can perform the web traffic. Currently only support on Android.

### What problem is this plugin solving?
This plugin address a very specific problem of needing to perform http traffic when there is no wireless connectivity available. Android supports the concept of *tethering* your device; using the internet connection from the android device on the tethered PC. However, it does not support the concept of *reverse tethering*; using the internet connection from the PC on the android device. *(technically this can be done, but requires root access and some reconfiguring of the network stack on the device)*

### How does this plugin solve that problem?
The information needed to send the http request (Method, Url, Body, Headers), is wrapped up into a json string. The plugin accepts a connection on a socket, and then proceeds to send the json string, and then listen for the response.

### Is this a complete solution?
No, this will not solve the stated problem on it's own. It will require a service of some shape on the PC open the socket, field the request, and send the response back. It also will require ADB tools to be installed on the connected PC, and for you to use those tools to forward a port on the PC to ADB: `adb forward tcp:59900 tcp:59900`

Our use case is that we try to use wirless connection on the device if it is available, and then fallback to using this socket communication if it is not. We are using [cordova-HTTP](https://github.com/brendonparker/cordova-HTTP) for our network traffic and [cordova-plugin-network-information](https://github.com/apache/cordova-plugin-network-information) to determine if we have a wireless connection. Then create a utility class to swap out this behavior depending on connectivity:

````javascript
export class HttpUtil {
	get isConnected() { return navigator.connection.type != Connection.NONE; };
	get strategy(){ return this.isConnected ? cordovaHTTP : httpviausb };
	
	get(url, data, headers, success, fail){
		this.strategy.get(url, data, headers, success, fail);
	}

	postJson(url, data, headers, success, fail){
		this.strategy.postJson(url, data, headers, success, fail);
	}
}

export default new HttpUtil();
````
Therefore the signatures of the methods available on `httpviausb` will match the signatures of `cordovaHTTP`
