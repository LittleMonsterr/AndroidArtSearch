package aidldemo;

import android.os.RemoteException;
import android.support.v4.text.ICUCompat;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
