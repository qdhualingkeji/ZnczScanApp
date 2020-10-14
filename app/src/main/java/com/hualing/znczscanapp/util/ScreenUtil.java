package com.hualing.znczscanapp.util;

import android.util.Log;

import com.hualing.znczscanapp.activities.BaseActivity;

public class ScreenUtil {

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    private void setScreenMode(BaseActivity context,int paramInt){
        try{
            android.provider.Settings.System.putInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }

    /**
     SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public int getScreenMode(BaseActivity context){
        int screenMode=0;
        try{
            screenMode =android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE);
            Log.e("screenMode===",""+screenMode);
        }
        catch (Exception localException){

        }
        return screenMode;
    }

    /**
     * 获得当前屏幕亮度值  0--255
     */
    public static int getScreenBrightness(BaseActivity context){
        int screenBrightness=255;
        try{
            screenBrightness = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            Log.e("screenBrightness===",""+screenBrightness);
        }
        catch (Exception localException){

        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度值  0--255
     */
    public static void setScreenBrightness(BaseActivity context,int paramInt){
        try{
            android.provider.Settings.System.putInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, paramInt);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    private void saveScreenBrightness(BaseActivity context,int paramInt){
        android.view.Window localWindow = context.getWindow();
        android.view.WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }
}
