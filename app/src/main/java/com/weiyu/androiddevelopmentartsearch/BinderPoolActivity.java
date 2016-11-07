package com.weiyu.androiddevelopmentartsearch;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import aidldemo.BinderPool;
import aidldemo.ComputeImpl;
import aidldemo.ICompute;
import aidldemo.ISecurityCenter;
import aidldemo.SecurityCenterImpl;

public class BinderPoolActivity extends AppCompatActivity {

    private  static  final String TAG = "BinderPoolActivity";
    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getsInstance(BinderPoolActivity.this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = (ISecurityCenter) SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG,"visit ISecurityCenter");
        String msg = "helloworld-android";
        System.out.println("content:"+msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            System.out.println("entrypt:"+password);
            System.out.println("decrypt:"+mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"visitICompute");
        IBinder compteBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = ComputeImpl.asInterface(compteBinder);
        try {
            System.out.println("3+5="+mCompute.add(3,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
