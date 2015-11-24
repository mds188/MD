package com.android.md.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.android.md.CustomApplcation;
import com.android.md.R;
import com.android.md.entity.User;
import com.android.md.util.CollectionUtils;
import com.android.md.view.HeaderLayout;
import com.android.md.view.dialog.DialogTips;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 基类
 * Created by mingdasen on 2015/11/23.
 */
public class BaseActivity extends FragmentActivity{

    BmobUserManager userManager;
    BmobChatManager manager;

    CustomApplcation mApplication;
    protected HeaderLayout mHeaderLayout;

    protected  int mScreenWidht;
    protected  int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        manager = BmobChatManager.getInstance(this);
        mApplication = CustomApplcation.getInstance();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidht = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    Toast mToast;

    public void ShowToast(final String text){
        if (!TextUtils.isEmpty(text)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null){
                        mToast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG);
                    }else{
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });
        }
    }

    public void ShowToast(final int resId){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null){
                    mToast = Toast.makeText(BaseActivity.this.getApplicationContext(),resId,Toast.LENGTH_LONG);
                }else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    /**
     * 打Log
     * @param msg
     */
    public void ShowLog(String msg){
        Log.i("result", msg);
    }

    /**
     * 只有title initTopBarLayoutByTitle
     * @param titleName
     */
    public void initTopBarForOnlyTitle(String titleName){
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    /**
     * 初始化标题栏-带左右按钮
     * @param titleName
     * @param rightDrawableId
     * @param text
     * @param listener
     */
    public void initTopBarForBoth(String titleName,int rightDrawableId,String text,
                                  HeaderLayout.onRightImageButtonClickListener listener){
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName, R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId, text, listener);
    }

    public void initTopBarForBoth(String titleName, int rightDrawableId,
                                  HeaderLayout.onRightImageButtonClickListener listener){
        mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    /**
     * 只有左边按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    public void showOfflineDialog(final Context context){
        DialogTips dialog = new DialogTips(this,"您的账号已在其他设备上登录!", "重新登录");
        //设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CustomApplcation.getInstance().logout();
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        //显示确认对话框
        dialog.show();
        dialog = null;
    }

    //左边按钮的点击事件
    public class OnLeftButtonClickListener implements HeaderLayout.onLeftImageButtonClickListener {
        @Override
        public void onClick() {
            finish();
        }
    }

    public void startAnimActivity(Class<?> cla){
        this.startActivity(new Intent(this,cla));
    }

    public void startAnimAntivity(Intent intent){
        this.startActivity(intent);
    }

    /**
     * 用于登陆或者自动登录情况下的用户资料及好友资料的检测更新
     */
    public void updateUserInfos(){
        //更新地理位置信息
        updateUserLocation();
        //查询该用户的好友列表（这个好友列表是去除黑名单用户的哦），目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
        //这里默认采取的是登录成功之后即将好友列表存储到数据库中，并更新到当前内存中
        userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
            @Override
            public void onSuccess(List<BmobChatUser> list) {
                //保存到application中方便比较
                CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(list));
            }

            @Override
            public void onError(int i, String s) {
                if (i == BmobConfig.CODE_COMMON_NONE) {
                    ShowLog(s);
                } else {
                    ShowLog("查询好友列表失败：" + s);
                }
            }
        });
    }

    /**
     * 更新用户的经纬度信息
     */
    public void updateUserLocation(){
        if (CustomApplcation.lastPoint != null){
            String saveLatitude = mApplication.getLatitude();
            String SaveLongtitude = mApplication.getLongtitude();
            String newLat = String.valueOf(CustomApplcation.lastPoint.getLatitude());
            String newlong = String.valueOf(CustomApplcation.lastPoint.getLongitude());
            if (!saveLatitude.equals(newLat) || !saveLatitude.equals(newlong)){//只有位置有变化就更新当前位置，达到实时更新的目的
                User u = (User)userManager.getCurrentUser(User.class);
                final User user = new User();
                user.setLocation(CustomApplcation.lastPoint);
                user.setObjectId(u.getObjectId());
                user.update(this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        CustomApplcation.getInstance().setLatitude(String.valueOf(user.getLocation().getLatitude()));
                        CustomApplcation.getInstance().setLongtitude(String.valueOf(user.getLocation().getLongitude()));
                        ShowLog("经纬度更新成功");
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        ShowLog("经纬度更新失败：" + s);
                    }
                });
            }else{
                ShowLog("用户位置未发生过变化");
            }
        }
    }
}
