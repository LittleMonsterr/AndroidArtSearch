package com.weiyu.androiddevelopmentartsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_okhttp_get;
    private Button bt_okhttp_post;

    final OkHttpClient client = new OkHttpClient();

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown;charset=utf-8");

    public static final MediaType MEDIA_TYPE_PNG
            = MediaType.parse("image/png");
    private static final String IMGUR_CLIENT_ID = "...";


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
            case R.id.bt_okhttp_syncGet:
                synchronousGet();
                break;
            case R.id.bt_okhttp_asyncGet:
                asynchronousGet();
                break;
            case R.id.bt_okhttp_accessHeader:
                accessingHeaders();
                break;
            case R.id.bt_okhttp_postaString:
                postAString();
                break;
            case R.id.bt_okhttp_postaFile:
                postAFile();
                break;
            case R.id.bt_okhttp_postform:
                postFormParameters();
                break;
            case R.id.bt_okhttp_postStream:
                postStreaming();
                break;
            case R.id.bt_okhttp_postmulitipartreq:
                postMultipartRequset();
                break;
            case R.id.bt_okhttp_responsecaching:
                responseCaching();
                break;
            case R.id.bt_okhttp_parseJson:
                parseJsonwithGson();
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

    /**
     * 同步Get
     * 下载一个文件，打印他的响应头，以string形式打印响应体。
     * 响应体的string()方法碎玉小文档来说十分方便、高效。
     * 但是如果响应体太大（超过1MB），应避免适应string()方法，因为它会将把整个文档加载到内存中。
     * 对于超过1MB的响应body，应使用流的方式来处理body。
     */
    private void synchronousGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code :"+response);
                    Headers responseHeaders = response.headers();
                    for(int i = 0; i<responseHeaders.size();i++){
                        System.out.println(responseHeaders.name(i)+":"+responseHeaders.value(i));
                    }
                    System.out.println(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    /**
     * 异步Get
     * 在一个工作线程中下载文件，当响应可读时回调callback接口。
     * 读取响应时会阻塞当前线程。
     * OkHttp现阶段不提供异步API来接收响应体
     *
     */
    private void asynchronousGet() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(!response.isSuccessful())
                            throw new IOException("Unexpected code:"+response);
                        Headers responseHeaders = response.headers();
                        for(int i = 0; i<responseHeaders.size(); i++){
                            System.out.println(responseHeaders.name(i)+":"+responseHeaders.value(i));
                        }
                        System.out.println(response.body().string());
                    }
                });
            }
        }).start();
    }

    /**
     * post方式提交String
     * 使用http post提交请求到服务。
     * 这个例子提交了一个markdown文档到web服务，以HTML方式渲染markdown。
     * 因为整个请求体都在内存中，因此避免使用此api提交大文档（大于1MB）。
     */
    private void postAString(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String postBody = ""
                        + "Releases\n"
                        + "--------\n"
                        + "\n"
                        + " * _1.0_ May 6, 2013\n"
                        + " * _1.1_ June 15, 2013\n"
                        + " * _1.2_ August 11, 2013\n";
                Request request = new Request.Builder()
                        .url("https://api.github.com/markdown/raw")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 提取响应头
     * 典型的HTTP头。像是一个Map
     */
    private void accessingHeaders(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/square/okhttp/issue")
                        .header("User-Agent","OkHttp Headers.java")
                        .addHeader("Accept","application/json;q=0.5")
                        .addHeader("Accept","application/vnd.github.v3+json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);
                    System.out.println("Server:"+response.header("Server"));
                    System.out.println("Date:"+response.header("Date"));
                    System.out.println("Vary:"+response.headers("Vary"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * post方式提交流
     * 以流的方式post提交请求。请求体的内容由流写入产生
     * 这个例子是流直接写入Okio的BufferedSink。
     * 在其他地方可以使用OutputStream写入，可以使用BuffedSink.outputStream()来获取
     */
    private void postStreaming(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MEDIA_TYPE_MARKDOWN;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        sink.writeUtf8("Numbers\n");
                        sink.writeUtf8("-------\n");
                        for(int i = 0; i<=997; i++){
                            sink.writeUtf8(String.format(" * %s = %s\n",i,factor(i)));
                        }
                    }

                    private String factor(int n){
                        for(int i = 2; i<n; i++){
                            int x = n/i;
                            if(x*i == n)
                                return factor(x)+" &times; "+i;
                        }
                        return Integer.toString(n);
                    }
                };

                Request request = new Request.Builder()
                        .url("https://api.github.com/markdown/raw")
                        .post(requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postAFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File("README.md");

                Request request = new Request.Builder()
                        .url("https://api.github.com/markdown/raw")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,file))
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * post方式提交表单
     * 使用FormEncodingBuilder来构建和HTML标签相同效果的请求体。
     * 键值对将使用一种HTML兼容形式的URL编码来进行编码。
     */
    private void postFormParameters(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormBody.Builder()
                        .add("search", "Jurassic Park")
                        .build();
                Request request = new Request.Builder()
                        .url("https://en.wikipedia.org/w/index.php")
                        .post(formBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * post方式提交分块请求
     * MutipartBuilder可以构成复杂的请求体，与HTML文件上传形式兼容。
     * 多块请求体中每块请求都是一个请求体，可以定义自己的请求头。
     * 这些请求头可以用来描述这块请求。
     */
    private void postMultipartRequset(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title","Aquare Logo")
                        .addFormDataPart("image","logo-square.png",
                                RequestBody.create(MEDIA_TYPE_PNG,new File("website/static/logo-square.png")))
                        .build();

                Request request = new Request.Builder()
                        .header("Authorization","Client-ID"+IMGUR_CLIENT_ID)
                        .url("https://api.imgur.com/3/image")
                        .post(requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 使用GSON解析JSON响应
     * ResponseBody.charStream()使用响应头Content-Type指定的字符集来解析响应体。默认是UTF-8。
     */
    private final Gson gson = new Gson();
    private void parseJsonwithGson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://api.github.com/gists/c2a7c39532239ff261be")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if(!response.isSuccessful())
                        throw new IOException("Unexpected code:"+response);

                    Gist gist = gson.fromJson(response.body().charStream(),Gist.class);
                    for(Map.Entry<String,GistFile> entry : gist.files.entrySet()){
                        System.out.println(entry.getKey());
                        System.out.println(entry.getValue().content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class Gist{
        Map<String,GistFile> files;
    }

    static class GistFile{
        String content;
    }


    /**
     * 响应缓存
     * 为了缓存响应，需要一个可以读写的缓存目录和缓存大小的限制。
     * 这个缓存目录应该是私有的，不信任的程序应不能读取缓存内容。
     * 一个缓存目录同时拥有多个缓存访问时错误的。大多数程序只需要调用一次new OkHttp()，在第一次调用时配置好缓存，
     * 然后其他地方只需要调用这个实例就可以了。。否则两个缓存示例互相干扰，破坏响应缓存，而且有可能会导致程序崩溃。
     * 响应缓存使用HTTP头作为配置。
     * 可以在请求头中添加Cache-Control:max-stale=3600,OkTttp缓存会支持。
     * 服务通过响应头确定响应缓存多长时间，例如使用Cache-Control: max-age=9600。
     */
    private void responseCaching(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}

