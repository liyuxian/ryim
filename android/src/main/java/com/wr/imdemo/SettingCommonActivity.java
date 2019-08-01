package com.wr.imdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.wr.imdemo.R;
import com.yuntongxun.plugin.common.ui.ECSuperActivity;
import com.yuntongxun.plugin.common.ui.base.RXDialogMgr;
import com.yuntongxun.plugin.common.view.SettingItem;
import com.yuntongxun.plugin.im.manager.IMPluginManager;

/**
 * 设置界面
 */
public class SettingCommonActivity extends ECSuperActivity {

	private SettingItem headSet;
	private SettingItem clear;
	private SettingItem showNotify;
	private SettingItem audio;
	private SettingItem vibrate;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initResourceRefs();
	}

	private void initView() {
		headSet = (SettingItem) findViewById(R.id.headSet);
		clear = (SettingItem) findViewById(R.id.clear);
		showNotify = (SettingItem) findViewById(R.id.show_notify);
		audio = (SettingItem) findViewById(R.id.audio);
		vibrate = (SettingItem) findViewById(R.id.vibrate);


		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showClearChatRecordDialog();
			}
		});

	}

	private void initResourceRefs() {
		headSet.getCheckedTextView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				headSet.toggle();
				IMPluginManager.getManager().useHandSetToPlayVoice(headSet.isChecked());
				initHeadSetting();
			}
		});

		showNotify.getCheckedTextView().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showNotify.toggle();
						IMPluginManager.getManager().setReceiveMessagesNotify(showNotify.isChecked());
						initNewsNotifySettings();
					}
				});
		audio.getCheckedTextView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				audio.toggle();
				IMPluginManager.getManager().useSoundToNotify(audio.isChecked());
			}
		});
		vibrate.getCheckedTextView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vibrate.toggle();
				IMPluginManager.getManager().useShakeToNotify(vibrate.isChecked());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initSettings();
	}

	@Override
	public int getLayoutId() {
		return R.layout.ytx_activity_common_setting;
	}

	private void initSettings() {
		boolean mUseHeadSet =IMPluginManager.getManager().getHandSetSetting();
		headSet.setChecked(mUseHeadSet);
		initNewsNotifySettings();
	}



		

	private void showClearChatRecordDialog() {
		RXDialogMgr.showDialog(this, R.string.app_tip, R.string.fmt_delcontactmsg_confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showPostingDialog("清空聊天记录");
				ECHandlerHelper handlerHelper = new ECHandlerHelper();
				handlerHelper.postRunnOnThead(new Runnable() {
					@Override
					public void run() {
						IMPluginManager.getManager().clearAllChatRecord();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dismissDialog();
							}
						});
					}
				});
			}
		});
	}
	


	/**
	 * 初始化接收新消息通知设置参数  包括声音与振动
	 */
	private void initNewsNotifySettings() {
		if (showNotify == null) {
			return;
		}
		boolean mShowNotifySetting = IMPluginManager.getManager().getReceiveMessagesNotifySetting();
		showNotify.setChecked(mShowNotifySetting);
		showNotify.showDivider(true);
		
		if(!mShowNotifySetting){
			audio.setVisibility(View.GONE);
			vibrate.setVisibility(View.GONE);
			showNotify.showDivider(false);
			return;
		}
		
		boolean mAudioSetting = IMPluginManager.getManager().getSoundNotifySetting();
		audio.setChecked(mAudioSetting);
		audio.setVisibility(View.VISIBLE);
		
		boolean mVibrateSetting =IMPluginManager.getManager().getShakeNotifySetting();
		vibrate.setChecked(mVibrateSetting);
		vibrate.setVisibility(View.VISIBLE);
	}
	private void initHeadSetting(){
		if(headSet == null){
			return;
		}
		boolean mHeadSetting=IMPluginManager.getManager().getHandSetSetting();
		headSet.setChecked(mHeadSetting);
	}

}
