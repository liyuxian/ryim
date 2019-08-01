package com.wr.imdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wr.imdemo.R;
import com.yuntongxun.plugin.common.ui.ECSuperActivity;
import com.yuntongxun.plugin.im.manager.IMPluginManager;
import com.yuntongxun.plugin.im.manager.port.OnUpdateMsgUnreadCountsListener;
import com.yuntongxun.plugin.im.ui.chatting.fragment.ConversationListFragment;

/**
 * 沟通界面 ConversationListFragment
 * 此监听用于未读消息数量变化
 * 用法：需要根据未读消息数量来对界面UI刷新的页面实现
 * OnUpdateMsgUnreadCountsListener
 */
public class ConversationActivity extends ECSuperActivity implements OnUpdateMsgUnreadCountsListener {

    private TextView unReadMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unReadMsg = (TextView) findViewById(R.id.unreadMsg);

        /*
         *   在开发者指定的activity中复制下面代码，
         *   会话列表如果需要ToolBar，那么 putBoolean()中传true
         */
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConversationListFragment.EXTRA_SHOW_TITLE, false);
        Fragment mFragment = Fragment.instantiate(this,
                ConversationListFragment.class.getName(), bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.convert_frame, mFragment)
                .commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_conversation;
    }

    @Override
    public void OnUpdateMsgUnreadCounts() {
        //里面调用方法获取未读消息数量之后，设置角标

        //示例：结合 获取未读消息使用
        /*int unReadMsgCount =IMPluginManager
                .getManager().getUn;
        unReadMsgCount.setText("未读消息＝"+ unReadMsgCount);*/
        int count = IMPluginManager.getManager().getUnReadMsgCount();
        unReadMsg.setText("未读消息＝" + count);
    }
}


