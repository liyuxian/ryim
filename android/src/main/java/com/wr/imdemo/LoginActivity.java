package com.wr.imdemo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.wr.imdemo.R;
import com.yuntongxun.plugin.common.AppMgr;
import com.yuntongxun.plugin.common.PluginUser;
import com.yuntongxun.plugin.common.SDKCoreHelper;
import com.yuntongxun.plugin.common.common.utils.ECPreferences;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.common.ui.ECSuperActivity;
import com.yuntongxun.plugin.common.ui.PermissionActivity;
import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.im.dao.helper.IMDao;


import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.huawei.android.pushagent.api.PushManager;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.imdemo.custom.OnBindViewHolderListenerImpl;
import com.wr.imdemo.GroupInviteUI;
import com.wr.imdemo.PersonInfoUI;
import com.yuntongxun.plugin.common.SDKCoreHelper;
import com.yuntongxun.plugin.common.common.utils.DeviceUtils;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.greendao3.helper.DaoHelper;
import com.yuntongxun.plugin.im.dao.helper.IMDao;
import com.yuntongxun.plugin.im.manager.IMPluginHelper;
import com.yuntongxun.plugin.im.manager.IMPluginManager;
import com.yuntongxun.plugin.im.manager.bean.IMConfiguration;
import com.yuntongxun.plugin.im.manager.bean.IMPanel;
import com.yuntongxun.plugin.im.manager.bean.RETURN_TYPE;
import com.yuntongxun.plugin.im.manager.port.OnBindViewHolderListener;
import com.yuntongxun.plugin.im.manager.port.OnIMBindViewListener;
import com.yuntongxun.plugin.im.manager.port.OnIMPanelClickListener;
import com.yuntongxun.plugin.im.manager.port.OnMessagePreproccessListener;
import com.yuntongxun.plugin.im.manager.port.OnNotificationClickListener;
import com.yuntongxun.plugin.im.manager.port.OnReturnIdsCallback;
import com.yuntongxun.plugin.im.manager.port.OnReturnIdsClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 模拟原生登录页面
 */
public class LoginActivity extends ECSuperActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText et_account, et_name;
    private Button btn_login;
    private EditText et_pwd;
    public static OnReturnIdsCallback mOnReturnIdsCallback;
    private List<String> HeadList = new ArrayList<>();
    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected String[] getReceiverAction() {
        return new String[]{SDKCoreHelper.ACTION_SDK_CONNECT};
    }

    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if (SDKCoreHelper.ACTION_SDK_CONNECT.equals(intent.getAction())) {
            if (SDKCoreHelper.isLoginSuccess(intent)) {
                String pushToken = ECPreferences.getSharedPreferences().getString("pushToken", null);
                LogUtil.d(TAG, "SDK connect Success ,reportToken:" + pushToken);
                if (!TextUtils.isEmpty(pushToken)) {
                    // 上报华为/小米推送设备token
                    ECDevice.reportHuaWeiToken(pushToken);
                }
                // 初始化IM数据库
                DaoHelper.init(LoginActivity.this, new IMDao());
                dismissDialog();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                int error = intent.getIntExtra("error", 0);
                if (error == SdkErrorCode.CONNECTING) {
                    return;
                }
                dismissDialog();
                LogUtil.e(TAG, "SDK connect fail [" + error + "]");
                ToastUtil.showMessage("登入失败[" + error + "]");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSdk();
        setDisplayHomeAsUpEnabled(false);
        checkPermissions();
        et_account = (EditText) findViewById(R.id.et_account);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_login.setOnClickListener(onClickListener);

        if (AppMgr.getPluginUser() != null) {
            LogUtil.d(TAG, "SDK auto connect...");
            SDKCoreHelper.init(getApplicationContext());
        }
    }

    public void initSdk(){
        if (!IMPluginHelper.shouldInit(this)) {
            //防止多进程初始化多次
            return;
        }
        SDKCoreHelper.setContext(this);
        // 插件日志开启(放在setContext之后)
        LogUtil.setDebugMode(true);
        DaoHelper.init(this, new IMDao());
        if (DeviceUtils.isHuaweiDevices()) {
            PushManager.requestToken(this);
        }
        if (DeviceUtils.isXiaomiDevices()) {
            initXiaoMiPush("2882303761517568985", "5601756880985");
        }

        HeadList.add("http://123.57.204.169:8888/vtm/8ab3bdf156e3e63d0156e43bb86a0006/rxhf12926/1485158266332197170.png_thum");
        initIMPlugin();
    }
    /**
     * 小米初始化
     */
    private void initXiaoMiPush(String appid, String appkey) {
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        MiPushClient.registerPush(this, appid, appkey);

        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }



    private OnIMBindViewListener onIMBindViewListener = new OnIMBindViewListener() {
        @Override
        public String onBindNickName(Context context, String userId) {
            return userId;
        }

        @Override
        public void OnAvatarClickListener(Context context, String id) {
            Intent intent = new Intent(context, PersonInfoUI.class);
            intent.putExtra(PersonInfoUI.EXTRA_USER_ID, id);
            context.startActivity(intent);
        }

        @Override
        public String onBindAvatarByUrl(Context context, String userId) {
            return HeadList.get(0);
        }
    };
    private OnReturnIdsClickListener onReturnIdsCallback = new OnReturnIdsClickListener() {
        @Override
        public void onReturnIdsClick(Context context, RETURN_TYPE return_type, OnReturnIdsCallback callback, String... ids) {
            mOnReturnIdsCallback = callback;
            if (return_type == RETURN_TYPE.ADDMEMBER_USERID) {
                //添加群成员
                context.startActivity(new Intent(context, GroupInviteUI.class));
            } else if (return_type == RETURN_TYPE.TRANSMIT_CONTACTID) {
                //转发
                context.startActivity(new Intent(context, GroupInviteUI.class));
            }
            for (String id : ids) {
                LogUtil.i(id + "");
            }
        }
    };
    private OnNotificationClickListener onNotificationClickListener = new OnNotificationClickListener() {
        @Override
        public void onNotificationClick(Context context, String contactId, Intent intent) {
         //   intent.setClassName(context, "com.yuntongxun.imdemo.MainActivity");
          //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  intent.putExtra("contactId", contactId);
        }
    };
    /**
     * This is the implementation of custom UI
     */
    private OnBindViewHolderListener mBindViewHolderListner = new OnBindViewHolderListenerImpl();
    /**
     * This is the processing of implementing message forwarding logic
     */
    private OnMessagePreproccessListener mOnMessagePreproccessListener = new OnMessagePreproccessListener() {
        @Override
        public boolean dispatchMessage(ECMessage message) {
            // 返回true意义是消费当前这条消息不交给插件内部处理,返回false意义是交给插件进行处理
            if (message.getForm().equals("10086")) {
                LogUtil.d(TAG, "dispatchMessage 10086...");
                return true;
            }
            return false;
        }
    };


    private void initIMPlugin() {
        IMPanel imPanel = new IMPanel.PanelBuilder("测试", R.mipmap.ic_launcher, IMPanel.PANELTYPE.SINGLECHAT)
                .setOnIMPannelClickListener(new OnIMPanelClickListener() {
                    @Override
                    public void onIMPanelClick(Context context, String... id) {
                        ToastUtil.showMessage("测试");
                    }
                })
                .build();


        /**
         * 推荐配置样式1
         */
        IMConfiguration imConfiguration = new IMConfiguration.IMConfigBuilder(this)
                .setOnIMBindViewListener(onIMBindViewListener)
                .setOnReturnIdsClickListener(onReturnIdsCallback)
                .setOnNotificationClickListener(onNotificationClickListener)
                // 沟通列表和聊天列表的UI自定义接口（私有云接口）
                .setOnBindViewHolderListener(mBindViewHolderListner)
                // IM消息转发接口 （私有云接口）
                .setOnMessagePreproccessListener(mOnMessagePreproccessListener)
                // (私有云功能)
                .showMsgState(true)
                .imPanel(imPanel)
                .notifyIcon(R.drawable.choose_icon)
                .build();

        IMPluginManager.getManager().init(imConfiguration);
        /**
         * 推荐配置样式2
         */
        /*IMConfiguration imConfiguration1 = new IMConfiguration.IMConfigBuilder(this)
                .setOnIMBindViewListener(IMManagerImpl.getInstance())
                .setOnNotificationClickListener(IMManagerImpl.getInstance())
                .setOnReturnIdsClickListener(IMManagerImpl.getInstance())
                .notifyIcon(R.drawable.male_icon)
                .build();
        IMPluginManager.getManager().init(imConfiguration1);*/

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String account = et_account.getText().toString();
            String name = et_name.getText().toString();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(name)) {
                ToastUtil.showMessage("ID和名字两者都必须填写");
                return;
            }
            showPostingDialog();
            PluginUser user = new PluginUser();
            user.setUserId(account);
            user.setUserName(name);
            // 以下setXXX参数都是可选
            user.setAppKey("ff8080815dbc080c015dbc9d7cd4000d");// AppId(私有云使用)
            user.setAppToken("7f4fa6d320ab49739183af1d498adb6b");// AppToken(私有云使用)
            // user.setPwd(et_pwd.getText().toString());// Password不为空情况即通讯账号密码登入
            // 下面参数是调用REST接口使用(如:语音会议一键静音功能)
            user.setAccountSid("ff8080815dbc080c015dbc9d7cc20002");// 主账号Id(REST使用)
            user.setAuthToken("b01679939aac4d3f97b4682fda349267");// 账户授权令牌(REST使用)
            user.setRestHost("http://ip:port");// REST 协议+ip+端口(REST使用)
            // user.setLvsHost("http://app.cloopen.com:8888");

            // SDK连接登入 参数一:用户信息参数 参数二:配置私有云地址(传null即为公有云登入)
            // 私有云使用两个参数login登入
            SDKCoreHelper.login(user, "server_config_rx.xml");
            // 公有云使用一个参数login登入
//            SDKCoreHelper.login(user);

        }
    };


    private void checkPermissions() {
        if (EasyPermissionsEx.hasPermissions(LoginActivity.this, PermissionActivity.needPermissionsInit)) {
            LogUtil.d(TAG, "permission is userful");
        } else {
            String rationInit = "需要存储、相机和麦克风的权限";
            EasyPermissionsEx.requestPermissions(LoginActivity.this, rationInit, PermissionActivity.PERMISSIONS_REQUEST_INIT, PermissionActivity.needPermissionsInit);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_login;
    }
}
