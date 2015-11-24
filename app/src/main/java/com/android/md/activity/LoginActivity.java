package com.android.md.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.md.config.BmobConstants;

import cn.bmob.im.config.BmobConstant;

/**
 * 登录界面
 * Created by mingdasen on 2015/11/24.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    EditText et_username,et_password;
    Button btn_login;
    TextView btn_register;

    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && BmobConstants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())){
                finish();;
            }
        }
    }
    @Override
    public void onClick(View v) {

    }
}
