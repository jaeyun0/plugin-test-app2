package com.icsfl.rfsmart.httpviausb;

import android.content.Intent;
import android.util.Xml;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CordovaHttpViaUSBPlugin extends CordovaPlugin {
    private static final String TAG = "CordovaHttpViaUSBPlugin";

    private CallbackContext callbackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Thread socketThread = new Thread(new SocketThread(action, args, callbackContext));
        socketThread.start();
        return true;
    }

    class SocketThread implements Runnable
    {
        private String action;
        private JSONArray args;
        private CallbackContext callbackContext;

        SocketThread(String action, final JSONArray args, final CallbackContext callbackContext){
            this.action = action;
            this.args = args;
            this.callbackContext = callbackContext;
        }

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                this.args.put(this.action);
                byte[] bytes = this.args.toString().getBytes();

                serverSocket = new ServerSocket(59900);
                serverSocket.setSoTimeout(3000);
                Socket socket = serverSocket.accept();

                OutputStream outputStream = socket.getOutputStream();

                outputStream.write(bytes);
                outputStream.flush();

                BufferedReader streamReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                socket.close();

                JSONObject response = new JSONObject(responseStrBuilder.toString());

                this.callbackContext.success(response);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                JSONObject response = new JSONObject();
                try {
                    response.put("status", 500);
                    response.put("error", ex.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.callbackContext.error(response);
            }
            finally {
                if(serverSocket != null){
                    try {
                        if(!serverSocket.isClosed()){
                            serverSocket.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }
        }
    }
}