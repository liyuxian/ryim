package com.wr.imdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wr.imdemo.LoginActivity;
import com.wr.imdemo.pluginimpl.IMManagerImpl;
import com.wr.imdemo.R;
import com.yuntongxun.plugin.common.common.utils.TextUtil;
import com.yuntongxun.plugin.common.ui.ECSuperActivity;

import java.util.ArrayList;

/**
 * Created with Android Studio IDEA.
 *
 * @author born
 * @version 1.0
 * @since 2016/12/23 10:56
 */

public class GroupInviteUI extends ECSuperActivity {
    private EditText other1, other2;
    private String userID;
    protected ArrayList<String> list = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getIntent().getStringExtra("userID");
        list.clear();

        other1 = (EditText) findViewById(R.id.other01);
        other2 = (EditText) findViewById(R.id.other02);
        Button btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st1 = other1.getText().toString();
                String st2 = other2.getText().toString();

                if (!TextUtil.isEmpty(userID)) {
                    list.add(userID);
                }
                if (!TextUtil.isEmpty(st1)) {
                    list.add(st1);
                }
                if (!TextUtil.isEmpty(st2)) {
                    list.add(st2);
                }
                if (list.size() != 0) {

                    String[] users = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        users[i] = "" + list.get(i);
                    }

                    if (LoginActivity.mOnReturnIdsCallback != null) {
                        LoginActivity.mOnReturnIdsCallback.returnIds(users);
                    } else {
                        IMManagerImpl.setResult(users);
                    }

                    finish();

                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ytx_activity_addothers_layout;
    }

}
