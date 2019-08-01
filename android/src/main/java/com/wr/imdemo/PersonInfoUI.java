package com.wr.imdemo;

import android.os.Bundle;
import android.widget.TextView;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.PersonInfo;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.wr.imdemo.R;
import com.yuntongxun.plugin.common.common.utils.LogUtil;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.common.common.utils.ToastUtil;
import com.yuntongxun.plugin.common.ui.ECSuperActivity;

/**
 * Created with Android Studio IDEA.
 *
 * @author wangjian
 * @version 1.0
 * @since 2018/5/24 10:14
 */
public class PersonInfoUI extends ECSuperActivity {
    public final static String TAG = LogUtil.getLogUtilsTag(PersonInfoUI.class);
    public final static String EXTRA_USER_ID = "user_id";
    private TextView mUserIdView;
    private TextView mUserNameView;
    private TextView mUserSex;
    private TextView mUserBirthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
        if (TextUtil.isEmpty(mUserId)) {
            throw new IllegalArgumentException("User ID can't be empty.");
        }
        mUserIdView = findViewById(R.id.ytx_user_id);
        mUserNameView = findViewById(R.id.ytx_user_name);
        mUserSex = findViewById(R.id.ytx_user_sex);
        mUserBirthday = findViewById(R.id.ytx_user_birthday);
        ECDevice.getPersonInfo(mUserId, new ECDevice.OnGetPersonInfoListener() {
            @Override
            public void onGetPersonInfoComplete(ECError ecError, PersonInfo personInfo) {
                if (ecError.errorCode == SdkErrorCode.REQUEST_SUCCESS && personInfo != null) {
                    mUserIdView.setText(personInfo.getUserId());
                    mUserNameView.setText(personInfo.getNickName());
                    mUserSex.setText(personInfo.getSex() == PersonInfo.Sex.MALE ? R.string.ytx_male : R.string.ytx_female);
                    mUserBirthday.setText(personInfo.getBirth());
                } else {
                    ToastUtil.showMessage(getString(R.string.ytx_error_get_person_info, ecError.errorCode));
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_personal_info;
    }
}
