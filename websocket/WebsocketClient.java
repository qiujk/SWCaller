package com.sn.swcaller.websocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by Administrator on 2018/1/31.
 */

public class WebsocketClient {
    public static Handler handler = null;

    private static WebSocketClient mSocketClient;

    public static void initWebSocket(String serverip, String serverport) {
        try {
            String address = "ws://" + serverip + ":" + serverport + "/suwen2.0/websocket";
            if (mSocketClient == null) {
                mSocketClient = new WebSocketClient(new URI(address)) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        openSocket(handshakedata);
                    }

                    @Override
                    public void onMessage(String message) {
                        receivedSocket(message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        closeSocket(reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        errorSocket(ex);
                    }
                };
            }
        } catch (Exception ex) {
            Log.e("WebsocketClient", "initWebSocket Error:" + ex.getMessage());
        }
    }

    public static void openSocket(ServerHandshake handshakedata) {
        if (handler != null) {
            Message openMsg = new Message();
            openMsg.what = 0;
            handler.sendMessage(openMsg);
        }
    }

    public static void closeSocket(String msg) {
        if (handler != null) {
            Message closeMsg = new Message();
            closeMsg.what = 3;
            closeMsg.obj = msg;
            handler.sendMessage(closeMsg);
        }
    }

    public static void errorSocket(Exception ex) {
        if (handler != null) {
            Message closeMsg = new Message();
            closeMsg.what = 2;
            closeMsg.obj = ex.getMessage();
            handler.sendMessage(closeMsg);
        }
    }

    public static void receivedSocket(String msg) {
        Log.i("receive",msg);
        if (handler != null) {
            Message recMsg = new Message();
            recMsg.what = 1;
            recMsg.obj = msg;
            handler.sendMessage(recMsg);
        }
    }

    //连接
    public static void connect() {
        if (mSocketClient != null && !mSocketClient.isClosed()) {
            Log.i("close","content websocket");
            mSocketClient.connect();
        }
    }


    //断开连接
    public static void closeConnect() {
        try {
            if (mSocketClient != null && mSocketClient.isOpen())
            {
                Log.i("close","close websocket");
                mSocketClient.close();
            }
            else
                mSocketClient = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSocketClient = null;
        }
    }

    //发送消息
    public static void sendMsg(String msg) {
        if (mSocketClient != null && mSocketClient.isOpen())
        {
            Log.i("send",msg);
            mSocketClient.send(msg);
        }

    }
}
