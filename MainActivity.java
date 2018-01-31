package com.sn.swcaller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sn.swcaller.websocket.WebsocketClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvShow;
    private Button btnLogout;
    private Button btnHeartbeat;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    //接收的消息
                    String recData = msg.obj.toString();
                    tvShow.setText(recData);
                    break;
                case 2:
                    //错误信息
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    //关闭
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    public static void actionStart(Activity context, String data) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebsocketClient.handler = mHandler;
        setContentView(R.layout.activity_main);
        String json = getIntent().getSerializableExtra("data").toString();
        tvShow = (TextView)findViewById(R.id.tv_showdata);
        tvShow.setText(json);
        btnLogout = (Button)findViewById(R.id.btnlogout);
        btnHeartbeat = (Button)findViewById(R.id.btnSend);
        btnLogout.setOnClickListener(this);
        btnHeartbeat.setOnClickListener(this);
    }

    public void GoToLogin() {
        finish();
        LoginActivity.actionStart(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSend:
                String getpatient = "{\"type\":\"caller\", \"command\":\"getpatient\", \"isDisableRepeatQueued\":\"true\", \"version\":\"V2\", \"data\":{\"accountid\":\"20\"}}";
                WebsocketClient.sendMsg(getpatient);
                break;
            case R.id.btnlogout:
                WebsocketClient.closeConnect();
                GoToLogin();
                break;
        }
    }
}
