/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ibp.plugin.socket;

import ibp.plugin.nsd.NSDHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class SocketPlugin extends CordovaPlugin {
//
//    public static final String TAG = "NsdChat";
//    private String serverName;
//    private int serverPort;
    private SocketConnection socketConn;
    public SocketPlugin(){
        socketConn = new SocketConnection(this);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
        case "startServerSocket":
            this.startServerSocket(callbackContext);
            break;
        case "stopServerSocket":
            this.stopServerSocket(callbackContext);
            break;
        case "initHandler":
            this.initHandler(callbackContext);
            break;
        case "sendMessage":
            this.sendMessage(callbackContext);
            break;
        }
        return true;
    }
    private void sendMessage(CallbackContext callbackContext){
        
    }
    private void startServerSocket(CallbackContext callbackContext){
//        initHandler(callbackContext);
        socketConn.startServerSocket(callbackContext, 7777);
    }
    private void stopServerSocket(CallbackContext callbackContext){
        socketConn.stopServerSocket(callbackContext);
    }
    private Handler mHandler;
    private void initHandler(CallbackContext callbackContext){
        final CallbackContext cbc = callbackContext;
        try {
            if (null == mHandler) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
//                        String type = msg.getData().getString("type");
//                        String content  = msg.getData().getString("content");
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put("type", new String(type));
//                            data.put("content", new String(content));
//                        } catch (JSONException e) {
//                        }
                        PluginResult result = new PluginResult(PluginResult.Status.OK, (JSONObject)msg.obj);
                        result.setKeepCallback(true);
                        cbc.sendPluginResult(result);
                    }
                };
            } 
        } catch (Exception e) {
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, "initHandler Exception: " + e);
            callbackContext.sendPluginResult(result);
        }        
    } 
    public void sendByHandler(JSONObject contentObj) {
        if(null != mHandler){
            Message message = new Message();
            message.obj = contentObj;
            mHandler.sendMessage(message);
        }
    }
    public void processMsgObj(JSONObject mMsgObj){
        try {
            if(mMsgObj.has("content")){
                sendByHandler(new JSONObject(mMsgObj.getString("content")));
            }      
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
    }
}
