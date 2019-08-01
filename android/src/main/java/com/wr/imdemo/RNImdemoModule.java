
package com.wr.imdemo;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class RNImdemoModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNImdemoModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }


  /**
   js调用原生toast
   */
  @ReactMethod
  public void show() {
    Toast.makeText( this.reactContext, "message---show", 1).show();
  }




  /**
   js调用原生页面跳转
   */
  @ReactMethod
  public void startActivityFromJS() {
   
     try{
            Activity currentActivity = getCurrentActivity();
            if(null!=currentActivity){
                Class toActivity = Class.forName("com.wr.imdemo.LoginActivity");
                Intent intent = new Intent(currentActivity,toActivity);
                intent.putExtra("params", "params");
                currentActivity.startActivity(intent);
            }
        }catch(Exception e){
           Toast.makeText( this.reactContext, "message ==  eee "+e.toString(), 1).show();
        }
  
  }



  @Override
  public String getName() {
    return "RNImdemo";
  }
}