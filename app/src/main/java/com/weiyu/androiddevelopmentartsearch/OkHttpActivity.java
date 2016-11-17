package com.weiyu.androiddevelopmentartsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_okhttp_get;
    private Button bt_okhttp_post;

    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);


        bt_okhttp_get =  (Button) findViewById(R.id.bt_okhttp_get);
        bt_okhttp_post = (Button) findViewById(R.id.bt_okhttp_post);

        bt_okhttp_get.setOnClickListener(this);
        bt_okhttp_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_okhttp_get:
                getRequest();
                break;
            case R.id.bt_okhttp_post:
                postRequest();
                break;
        }
    }

    private void getRequest() {
        final Request request = new Request.Builder()
                .get()
                .tag(this)
                .url("http://www.wooyu.org")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        Log.i("www","打印get响应的数据："+response.body().string());
                    }else {
                        throw new IOException("Unexpected code:"+response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postRequest() {


    }
}
