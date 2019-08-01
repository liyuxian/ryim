package com.wr.imdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wr.imdemo.R;
import com.yuntongxun.plugin.common.ui.ECSuperActivity;
import com.yuntongxun.plugin.im.ui.group.GroupListFragment;

/**
 * 群组界面
 * GroupListFragment
 */
public class MyGroupListUI extends ECSuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 开发者在容器activity中按照一般fragment嵌入得方法使用
         */
        getSupportFragmentManager().beginTransaction()
                .add(R.id.convert_frame, Fragment.instantiate(this,
                        GroupListFragment.class.getName(), null)).commit();
        findViewById(R.id.unreadMsg).setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_conversation;
    }
}
