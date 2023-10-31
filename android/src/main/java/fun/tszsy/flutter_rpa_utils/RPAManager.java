package fun.tszsy.flutter_rpa_utils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RPAManager {
    private final static String TAG = RPAManager.class.getSimpleName();
    private Handler mainThreadHandler;
    private Handler mHandler;
    private Context applicationContext;

    public static AccessibilityNodeInfo rootNode;

    public RPAManager(Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
    }

    private int screenWidth = 0;
    private int screenHeight = 0;

    public void init(){
        HandlerThread mHandlerThread = new HandlerThread("RPAThread");
        mHandlerThread.start();
        WindowManager windowManager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Log.d(TAG, "init: screenHeight " + screenHeight);
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public List<AccessibilityNodeInfo> findText(String content){
        List<AccessibilityNodeInfo> accessibilityNodeInfoList;
        if (rootNode != null){
            accessibilityNodeInfoList = rootNode.findAccessibilityNodeInfosByText(content);
        }else {
            accessibilityNodeInfoList = new ArrayList<>();
        }

        return accessibilityNodeInfoList;
    }
}
