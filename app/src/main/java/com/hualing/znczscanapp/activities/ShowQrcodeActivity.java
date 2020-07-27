package com.hualing.znczscanapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hualing.znczscanapp.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;

public class ShowQrcodeActivity extends BaseActivity {

    @BindView(R.id.qrcode_iv)
    ImageView qrcodeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected void getDataFormWeb() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                Log.e("ooooooooo","ooooooooooo");
                //URL url = new URL("http://121.196.184.205:96/hydrocarbon/download-files/88036c76dd44e7ecf1a014b2995b97ab/二维码文件.png");
                URL url = null;
                    url = new URL("https://dss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=527074007,3636349827&fm=58&app=83&f=JPEG?w=200&h=200&s=F063B1546F9C31EBB6AD4FDD03001006");
                //Bitmap bitmap = BitmapFactory.decodeFile("https://himg.bdimg.com/sys/portraitn/item/e63a68616e646f6e6771696e677875650b09");

                    HttpURLConnection conn = (HttpURLConnection) new URL("https://himg.bdimg.com/sys/portraitn/item/e63a68616e646f6e6771696e677875650b09").openConnection();

// 设置请求方式和超时时间
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(1000 * 10);
                    conn.connect();

                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

//利用消息的方式把数据传送给handler
                        Message msg = handler.obtainMessage();
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Log.e("error===",""+e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler=new Handler(){
        public void handleMessage(Message message) {
            //Bitmap bitmap = (Bitmap) message.obj;
            Log.e("ppppppp","pppppppp");
            Bitmap bm=(Bitmap)message.obj;
            qrcodeIV.setImageBitmap(bm);
        }
    };

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_qrcode;
    }
}
