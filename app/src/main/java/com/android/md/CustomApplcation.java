package com.android.md;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.md.util.CollectionUtils;
import com.android.md.util.SharePreferenceUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 自定义全局Applcation类
 * Created by mingdasen on 2015/11/23.
 */
public class CustomApplcation extends Application{
    public static CustomApplcation mInstance;
    public LocationClient mLocationClient;
    public MyLoactionListener mMyLocationListener;

    public static BmobGeoPoint lastPoint;//上次定位到的经纬度

    @Override
    public void onCreate() {
        super.onCreate();
        //是否启动debug模式-默认开启模式
        BmobChat.DEBUG_MODE = true;
        mInstance = this;
        init();

    }
    NotificationManager mNotificationManager;
    MediaPlayer mMediaPlayer;
    private void init() {
        mMediaPlayer = MediaPlayer.create(this,R.raw.notify);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initImageLoader(getApplicationContext());
        //若用户登录过，则先从好友数据库中取出好友list存入内存中
        if (BmobUserManager.getInstance(getApplicationContext()).getCurrentUser() != null){
            //获取本地好友user list到内存，方便以后获取好友list
            contactList = CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList());
        }
        
        initBaidu();
    }

    /**
     * 初始化百度相关sdk
     */
    private void initBaidu() {
        //初始化地图sdk
        SDKInitializer.initialize(this);
        //初始化定位sdk
        initBaiduLocClient();
    }

    /**
     * 初始化百度定位sdk
     */
    private void initBaiduLocClient() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLoactionListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    /**
     * 实现定位回调监听
     */
    public class MyLoactionListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();
            double longtitude = location.getLongitude();
            if (lastPoint != null){
                if (lastPoint.getLatitude() == location.getLatitude() &&
                        lastPoint.getLongitude() == location.getLongitude()){
                    Log.i("result","两次获取坐标相同");//若两次请求获取到的地理位置坐标是相同的，则不再定位
                    mLocationClient.stop();
                    return;
                }
            }
            lastPoint = new BmobGeoPoint(longtitude,latitude);
        }
    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context){
        //获取到缓存的目录地址
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,"bmobim/Cache");
        //创建配置ImageLoader(所有的选项都是可选的，只使用那些你真的想定制)，这个可以设定在APPLACATION里面，
        //设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                //线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                //将保存的数据的URI名称用MD5加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓冲路径
                .writeDebugLogs()//Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static CustomApplcation getInstance(){
        return mInstance;
    }

    //单例模式，才能及时返回数据
    SharePreferenceUtil mSpUtil;
    public static final String PREFERENCE_NAME = "_sharedinfo";

    public synchronized SharePreferenceUtil getSpUtil(){
        if (mSpUtil == null){
            String currentId = BmobUserManager.getInstance(getApplicationContext()).getCurrentUserObjectId();
            String sharedName = currentId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this,sharedName);
        }
        return mSpUtil;
    }

    public NotificationManager getNotificationManager(){
        if (mNotificationManager == null){
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public synchronized MediaPlayer getMediaPlayer(){
        if (mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this,R.raw.notify);
        }
        return mMediaPlayer;
    }

    public final String PREF_LONGTITUTE = "longtitude";//经度
    private String longtitude = "";

    /**
     * 获取经度
     */
    public String getLongtitude(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        longtitude = preferences.getString(PREF_LONGTITUTE,"");
        return longtitude;
    }

    /**
     * 设置经度
     */
    public void setLongtitude(String lon){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LONGTITUTE,lon).commit()){
            longtitude = lon;
        }
    }

    public final String PREF_LATITUDE = "latitude";// 经度
    private String latitude = "";

    /**
     * 获取纬度
     */
    public String getLatitude() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = preferences.getString(PREF_LATITUDE, "");
        return latitude;
    }

    /**
     * 设置维度
     */
    public void setLatitude(String lat) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LATITUDE, lat).commit()) {
            latitude = lat;
        }
    }

    private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

    /**
     * 获取内存中好友user list;
     * @return
     */
    public Map<String,BmobChatUser> getContactList(){
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     * @param contactList
     */
    public void setContactList(Map<String,BmobChatUser> contactList){
        if (this.contactList != null){
            this.contactList.clear();
        }
        this.contactList = contactList;
    }

    /**
     * 退出登录，清空缓存数据
     */
    public void logout(){
        BmobUserManager.getInstance(getApplicationContext()).logout();
        setContactList(null);
        setLatitude(null);
        setLongtitude(null);
    }
}
