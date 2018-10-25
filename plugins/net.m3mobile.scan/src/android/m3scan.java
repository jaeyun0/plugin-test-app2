package net.m3mobile.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.m3.sdk.scannerlib.Barcode;
import com.m3.sdk.scannerlib.BarcodeListener;
import com.m3.sdk.scannerlib.BarcodeManager;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class m3scan extends CordovaPlugin {

    private static final String SCANNER_ACTION_SETTING_CHANGE = "com.android.server.scannerservice.settingchange";
    public static final String SCANNER_ACTION_BARCODE = "com.android.server.scannerservice.broadcast";
    private static final String SCANNER_ACTION_SETTING = "com.android.server.scannerservice.setting";
    private static final String SCANNER_ACTION_GET_SETTING = "com.android.server.scannerservice.getsetting";
    public static final String SCANNER_ACTION_PARAMETER = "android.intent.action.SCANNER_PARAMETER";
    private static final String SCANNER_EXTRA_BARCODE_DATA = "m3scannerdata";

    private Barcode mBarcode = null;
    private BarcodeListener mListener = null;
    private BarcodeManager mManager = null;
    private CallbackContext callbackContext = null;
    private CallbackContext prefixCallbackContext = null;
    private CallbackContext postfixCallbackContext = null;
    private CallbackContext beginIndexCallbackContext = null;
    private CallbackContext endIndexCallbackContext = null;
    private CallbackContext endCharCallbackContext = null;
    private CallbackContext code128Length1CallbackContext = null;
    private CallbackContext code128Length2CallbackContext = null;
    private CallbackContext code39Length1CallbackContext = null;
    private CallbackContext code39Length2CallbackContext = null;
    private CallbackContext i2o5Length1CallbackContext = null;
    private CallbackContext i2o5Length2CallbackContext = null;
    private CallbackContext outPutModeCallbackContext = null;
    private CallbackContext scanCallbackContext = null;

    @Override

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.d(getClass().getSimpleName(), action.toString());
        this.callbackContext = callbackContext;
        final Context context = this.cordova.getActivity().getApplicationContext();
        if(action.equals("setScanner")) {
            mBarcode.setScanner(args.getBoolean(0));
            callbackContext.success(action);
            return true;
        } else if(action.equals("scanStart")) {
            mBarcode.scanStart();
            scanCallbackContext = callbackContext;
            return true;
        } else if(action.equals("scanDispose")) {
            mBarcode.scanDispose();
            callbackContext.success(action);
            return true;
        } else if(action.equals("setPrefix")) {
            Intent intent  = new Intent(SCANNER_ACTION_SETTING_CHANGE);
            intent.putExtra("setting", "prefix");
            intent.putExtra("prefix_value", args.getString(0));
            context.sendOrderedBroadcast(intent, null);

            intent = new Intent(SCANNER_ACTION_SETTING_CHANGE);
            intent.putExtra("setting", "postfix");
            intent.putExtra("postfix_value", args.getString(1));
            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("setEndCharacter")) {
            Intent intent = new Intent(SCANNER_ACTION_SETTING_CHANGE);
            intent.putExtra("setting", "end_char");

            if(args.getString(0).equals("enter"))
                intent.putExtra("end_char_value", 0);
            else if(args.getString(0).equals("space"))
                intent.putExtra("end_char_value", 1);
            else if(args.getString(0).equals("tab"))
                intent.putExtra("end_char_value", 2);
            else if(args.getString(0).equals("key_enter"))
                intent.putExtra("end_char_value", 3);
            else if(args.getString(0).equals("key_space"))
                intent.putExtra("end_char_value", 4);
            else if(args.getString(0).equals("key_tab"))
                intent.putExtra("end_char_value", 5);
            else if(args.getString(0).equals("none"))
                intent.putExtra("end_char_value", 6);

            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("setSubString")) {
            Intent intent = new Intent(SCANNER_ACTION_SETTING_CHANGE);
            //intent.putExtra("setting", "begin_index");
            intent.putExtra("setting", "begin_index");
            intent.putExtra("begin_value", args.getInt(0));
            context.sendOrderedBroadcast(intent, null);

            intent.putExtra("setting", "end_index");
            intent.putExtra("end_value", args.getInt(1));
            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("setCode128Length")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 209);
            intent.putExtra("value", args.getInt(0));
            context.sendOrderedBroadcast(intent, null);

            intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 210);
            intent.putExtra("value", args.getInt(1));
            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("setCode39Length")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 18);
            intent.putExtra("value", args.getInt(0));
            context.sendOrderedBroadcast(intent, null);

            intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 19);
            intent.putExtra("value", args.getInt(1));
            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("setI2o5Length")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 22);
            intent.putExtra("value", args.getInt(0));
            context.sendOrderedBroadcast(intent, null);

            intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 23);
            intent.putExtra("value", args.getInt(1));
            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("setOutPutMode")) {
            Intent intent = new Intent(SCANNER_ACTION_SETTING_CHANGE);
            intent.putExtra("setting", "output_mode");

            if(args.getString(0).equals("copy_paste"))
                intent.putExtra("output_mode_value", 0);
            else if(args.getString(0).equals("key"))
                intent.putExtra("output_mode_value", 1);
            else if(args.getString(0).equals("output_none"))
                intent.putExtra("output_mode_value", 2);

            context.sendOrderedBroadcast(intent, null);

            callbackContext.success(action);
            return true;
        } else if(action.equals("showToast")) {
            Toast.makeText(context, args.getString(0), Toast.LENGTH_LONG).show();
            callbackContext.success();

            return true;
        } else if(action.equals("getPrefix")) {
            Intent intent = new Intent(SCANNER_ACTION_GET_SETTING);
            intent.putExtra("setting", "prefix");
            context.sendOrderedBroadcast(intent, null);
            prefixCallbackContext = callbackContext;

            return true;
        } else if(action.equals("getSuffix")) {
            Intent intent = new Intent(SCANNER_ACTION_GET_SETTING);
            intent.putExtra("setting", "postfix");
            context.sendOrderedBroadcast(intent, null);
            postfixCallbackContext = callbackContext;

            return true;
        } else if(action.equals("getBeginIndex")) {
            Intent intent = new Intent(SCANNER_ACTION_GET_SETTING);
            intent.putExtra("setting", "begin_index");
            context.sendOrderedBroadcast(intent, null);
            beginIndexCallbackContext = callbackContext;

            return true;
        } else if(action.equals("getEndIndex")) {
            Intent intent = new Intent(SCANNER_ACTION_GET_SETTING);
            intent.putExtra("setting", "end_index");
            context.sendOrderedBroadcast(intent, null);
            endIndexCallbackContext = callbackContext;

            return true;
        } else if(action.equals("getEndChar")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SCANNER_ACTION_GET_SETTING);
                    intent.putExtra("setting", "end_char");
                    context.sendOrderedBroadcast(intent, null);
                    endCharCallbackContext = callbackContext;
                }
            });
            return true;
        } else if(action.equals("getCode128Length1")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 209);
            intent.putExtra("value", -1);
            context.sendOrderedBroadcast(intent, null);
            code128Length1CallbackContext = callbackContext;

            return true;
        } else if(action.equals("getCode128Length2")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 210);
            intent.putExtra("value", -1);
            context.sendOrderedBroadcast(intent, null);
            code128Length2CallbackContext = callbackContext;

            return true;
        } else if(action.equals("getCode39Length1")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 18);
            intent.putExtra("value", -1);
            context.sendOrderedBroadcast(intent, null);
            code39Length1CallbackContext = callbackContext;

            return true;
        } else if(action.equals("getCode39Length2")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 19);
            intent.putExtra("value", -1);
            context.sendOrderedBroadcast(intent, null);
            code39Length2CallbackContext = callbackContext;

            return true;
        } else if(action.equals("getI2o5Length1")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 22);
            intent.putExtra("value", -1);
            context.sendOrderedBroadcast(intent, null);
            i2o5Length1CallbackContext = callbackContext;

            return true;
        } else if(action.equals("getI2o5Length2")) {
            Intent intent = new Intent(SCANNER_ACTION_PARAMETER);
            intent.putExtra("symbology", 23);
            intent.putExtra("value", -1);
            context.sendOrderedBroadcast(intent, null);
            i2o5Length2CallbackContext = callbackContext;

            return true;
        } else if(action.equals("getOutPutMode")) {
            Intent intent = new Intent(SCANNER_ACTION_GET_SETTING);
            intent.putExtra("setting", "output_mode");
            context.sendOrderedBroadcast(intent, null);
            outPutModeCallbackContext = callbackContext;

            return true;
        }
        return false;
    }

    @Override
    public void initialize(CordovaInterface cordova, final CordovaWebView webView) {
        super.initialize(cordova, webView);
        Context context = this.cordova.getActivity().getApplicationContext();

        mBarcode = new Barcode(context);
        mManager = new BarcodeManager(context);
        mBarcode.setScanner(true);

        mListener = new BarcodeListener() {
            @Override
            public void onBarcode(String s) {
                Log.i("ScannerTest", "result=" + s);
            }

            @Override
            public void onBarcode(String s, String s1) {
                Log.i("ScannerTest", "result=" + s);
                if(scanCallbackContext != null)
                    scanCallbackContext.success(s);
                webView.loadUrl("javascript:setMessage('data : " + s + "\\ntype : " + s1 + "')");
            }

            @Override
            public void onGetSymbology(int i, int i1) {

            }
        };
        mManager.addListener(mListener);

        // intent filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCANNER_ACTION_SETTING);
        filter.addAction(SCANNER_ACTION_BARCODE);
        context.registerReceiver(BarcodeIntentBroadcast,filter);

    }

    @Override
    public void onDestroy() {
        Context context = this.cordova.getActivity().getApplicationContext();
        context.unregisterReceiver(BarcodeIntentBroadcast);
        super.onDestroy();
    }

    public BroadcastReceiver BarcodeIntentBroadcast = new BroadcastReceiver() {

        private String strPrefix;
        private String strSuffix;
        private int nBegin;
        private int nEnd;
        private int nEndChar;
        private int nOutPutMode;
        private String strBarcode;

        private static final String SCANNER_EXTRA_PREFIX = "m3scanner_prefix";
        private static final String SCANNER_EXTRA_POSTFIX = "m3scanner_postfix";
        private static final String SCANNER_EXTRA_BEGIN_INDEX = "m3scanner_beginindex";
        private static final String SCANNER_EXTRA_END_INDEX = "m3scanner_endindex";
        private static final String SCANNER_EXTRA_END_CHAR = "m3scanner_endchar";
        private static final String SCANNER_EXTRA_OUTPUT_MODE = "m3scanner_output_mode";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive", intent.getAction());

            if(intent.getAction().equals(SCANNER_ACTION_SETTING)) {
                String extra = intent.getStringExtra("setting");
                if(extra.equals("prefix")) {
                    strPrefix = intent.getExtras().getString(SCANNER_EXTRA_PREFIX);
                    Log.d("onReceive", "strPrefix = " + strPrefix);
                    prefixCallbackContext.success(strPrefix);
                } else if(extra.equals("postfix")) {
                    strSuffix = intent.getExtras().getString(SCANNER_EXTRA_POSTFIX);
                    Log.d("onReceive", "strSuffix = " + strSuffix);
                    postfixCallbackContext.success(strSuffix);
                } else if(extra.equals("begin_index")) {
                    nBegin = intent.getExtras().getInt(SCANNER_EXTRA_BEGIN_INDEX);
                    Log.d("onReceive", "nBegin = " + nBegin);
                    beginIndexCallbackContext.success(nBegin);
                } else if(extra.equals("end_index")) {
                    nEnd = intent.getExtras().getInt(SCANNER_EXTRA_END_INDEX);
                    Log.d("onReceive", "nEnd = " + nEnd);
                    endIndexCallbackContext.success(nEnd);
                } else if(extra.equals("end_char")) {
                    nEndChar = intent.getExtras().getInt(SCANNER_EXTRA_END_CHAR);
                    Log.d("onReceive", "nEndChar = " + nEndChar);
                    endCharCallbackContext.success(nEndChar);
                }else if(extra.equals("output_mode")) {
                    nOutPutMode = intent.getExtras().getInt(SCANNER_EXTRA_OUTPUT_MODE);
                    Log.d("onReceive", "nOutputMode = " + nOutPutMode);
                    outPutModeCallbackContext.success(nOutPutMode);
                }
            } else if(intent.getAction().equals(SCANNER_ACTION_BARCODE)) {
                strBarcode = intent.getExtras().getString(SCANNER_EXTRA_BARCODE_DATA);

                if(strBarcode != null) {

                } else {
                    int nSymbol = intent.getExtras().getInt("symbology", -1);
                    int nValue = intent.getExtras().getInt("value", -1);
                    Log.i("onReceive", "getSymbology [" + nSymbol + "][" + nValue + "]");
                    if(nSymbol != -1) {
                        switch (nSymbol){
                            case 209:
                                code128Length1CallbackContext.success(nValue);
                                break;
                            case 210:
                                code128Length2CallbackContext.success(nValue);
                                break;
                            case 18:
                                code39Length1CallbackContext.success(nValue);
                                break;
                            case 19:
                                code39Length2CallbackContext.success(nValue);
                                break;
                            case 22:
                                i2o5Length1CallbackContext.success(nValue);
                                break;
                            case 23:
                                i2o5Length2CallbackContext.success(nValue);
                                break;
                        }
                    }
                }
            }
        }
    };


}
