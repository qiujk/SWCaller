package com.sn.swcaller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sn.swcaller.base.BaseApplication;
import com.sn.swcaller.util.NetworkUtil;
import com.sn.swcaller.websocket.WebsocketClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mServiceSetLayout;
    private FrameLayout mLogin;

    private EditText mUserName;
    private EditText mUserPwd;

    private EditText mServerIp;
    private EditText mServerPort;

    private TextView mServerSet;
    private boolean isServerLayoutShow = false;

    public static void actionStart(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //打开成功
                    //发送login 消息
                    String data = "{ \"type\":\"caller\", \"command\":\"login\", \"isDisableRepeatQueued\":\"true\", " +
                            "\"version\":\"V2\", \"data\":{\"username\":\"abc\",\"password\":\"abc\",\"logintype\":\"\"," +
                            "\"ip\":\"192.168.66.39\",\"deviceid\":\"\",\"version\":\"\",\"upgrademd5\":\"\"}}";
                    WebsocketClient.sendMsg(data);
                    break;
                case 1:
                    //接收的消息
                    String recData = msg.obj.toString();
                    //登陆成功，跳回到主界面
                    finish();
                    MainActivity.actionStart(LoginActivity.this, recData);
                    break;
                case 2:
                    //错误信息
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    //关闭
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebsocketClient.handler = mHandler;
        setContentView(R.layout.activity_login);
        initControl();
        bindControlData();
    }

    private void initControl() {
        mServiceSetLayout = (LinearLayout) findViewById(R.id.login_service_container);

        mServerSet = (TextView) findViewById(R.id.login_change_mode);
        mUserName = (EditText) findViewById(R.id.login_username);
        mUserPwd = (EditText) findViewById(R.id.login_password);
        mServerIp = (EditText) findViewById(R.id.login_serviceip);
        mServerPort = (EditText) findViewById(R.id.login_serviceport);
        mLogin = (FrameLayout) findViewById(R.id.login_submit);
        mServerSet.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    private void bindControlData() {
        mUserName.setText(BaseApplication.getInstance().getUsername());
        mServerIp.setText(BaseApplication.getInstance().getServiceip());
        mServerPort.setText(BaseApplication.getInstance().getServiceport());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_change_mode:
                isServerLayoutShow = !isServerLayoutShow;
                if (isServerLayoutShow)
                    mServiceSetLayout.setVisibility(View.VISIBLE);
                else
                    mServiceSetLayout.setVisibility(View.GONE);
                break;
            case R.id.login_submit:
                if (NetworkUtil.GetConnectType(this) != NetworkUtil.NetworkStatus.DisConnect) {
                    Toast.makeText(this, "网络不可用，请检查网络并重试！", Toast.LENGTH_SHORT).show();
                } else {
                    BaseApplication.getInstance().setServiceip(mServerIp.getText().toString().trim());
                    BaseApplication.getInstance().setServiceport(mServerPort.getText().toString().trim());
                    BaseApplication.getInstance().setUsername(mUserName.getText().toString().trim());
                    new LoginTask().execute();
                    String ip = NetworkUtil.getIpAdress(this);
                    Toast.makeText(this, "正在登陆", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mLogin.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            WebsocketClient.initWebSocket(BaseApplication.getInstance().getServiceip(), BaseApplication.getInstance().getServiceport());
            WebsocketClient.connect();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

        }
    }
}
