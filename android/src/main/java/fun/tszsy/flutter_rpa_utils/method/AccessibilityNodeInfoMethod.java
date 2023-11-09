package fun.tszsy.flutter_rpa_utils.method;

import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fun.tszsy.flutter_rpa_utils.RPAManager;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class AccessibilityNodeInfoMethod implements MethodChannel.MethodCallHandler {

    public Gson gson = new Gson();
    public RPAManager rpaManager = new RPAManager();


    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    }

    public boolean superOnMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "getRootNode":
                result.success(object2String(RPAManager.rootNode));
                break;
            case "findAccessibilityNodeInfosByText":
                try {
                    String text = call.argument("text");
                    if (TextUtils.isEmpty(text)) {
                        result.success(new ArrayList<String>());
                        return true;
                    }
                    List<HashMap<String, Object>> list = new ArrayList<>();
                    List<AccessibilityNodeInfo> accessibilityNodeInfoList =
                            RPAManager.rootNode.findAccessibilityNodeInfosByText(text);
                    for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfoList) {
                        list.add(object2String(accessibilityNodeInfo));
                    }
                    result.success(list);
                } catch (Exception e) {
                    result.error("error", e.toString(), "");
                }
                break;
            case "getText":
                try {
                    String source = call.argument("source");
                    if (TextUtils.isEmpty(source)) {
                        result.success(null);
                        return true;
                    }
                    assert source != null;

                    AccessibilityNodeInfo accessibilityNodeInfo = stringToObject(source, AccessibilityNodeInfo.CREATOR);
                    result.success(accessibilityNodeInfo.getText() == null ? "1" :
                            accessibilityNodeInfo.getText().toString());
                } catch (Exception e) {
                    result.error("getText error", e.toString(), "");
                    e.printStackTrace();
                }
                break;

            case "getChild":
                try {
                    String source = call.argument("source");
                    Integer index = call.argument("index");
                    if (TextUtils.isEmpty(source) || index == null) {
                        result.success(null);
                        return true;
                    }
                    assert source != null;

                    AccessibilityNodeInfo accessibilityNodeInfo = stringToObject(source, AccessibilityNodeInfo.CREATOR);
                    AccessibilityNodeInfo resultAccessibilityNodeInfo = accessibilityNodeInfo.getChild(index);

                    result.success(object2String(resultAccessibilityNodeInfo));
                } catch (Exception e) {
                    result.error("getText error", e.toString(), "");
                    e.printStackTrace();
                }
                break;

            case "getChildMore":
                try {
                    String source = call.argument("source");
                    List<Integer> indexList = call.argument("indexList");
                    if (TextUtils.isEmpty(source) || indexList == null || indexList.isEmpty()) {
                        result.success(null);
                        return true;
                    }
                    assert source != null;

                    AccessibilityNodeInfo resultAccessibilityNodeInfo = stringToObject(source, AccessibilityNodeInfo.CREATOR);
                    for (Integer index : indexList) {
                        if (resultAccessibilityNodeInfo == null) {
                            result.success(null);
                            return true;
                        }
                        resultAccessibilityNodeInfo = resultAccessibilityNodeInfo.getChild(index);
                    }

                    result.success(object2String(resultAccessibilityNodeInfo));
                } catch (Exception e) {
                    result.error("getText error", e.toString(), "");
                    e.printStackTrace();
                }
                break;
            default:
                return false;
        }
        return true;


    }


    public HashMap<String, Object> object2String(AccessibilityNodeInfo accessibilityNodeInfo) {
        String packageName = accessibilityNodeInfo.getPackageName() == null? "":accessibilityNodeInfo.getPackageName().toString();
        AccessibilityWindowInfo windowInfo = null;
        try {
            windowInfo = accessibilityNodeInfo.getWindow();
        } catch (Exception e) {

        }



        HashMap<String, Object> data = new HashMap<>();
        data.put("packageName", packageName);
        data.put("className", accessibilityNodeInfo.getClassName());
        data.put("capturedText", accessibilityNodeInfo.getText() == null ? null :
                accessibilityNodeInfo.getText().toString());


        if (windowInfo != null) {
            // Check if the window is in picture-in-picture mode.
            data.put("isActive", windowInfo.isActive());
            data.put("isFocused", windowInfo.isFocused());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                data.put("isPip", windowInfo.isInPictureInPictureMode());
            }
            data.put("windowType", windowInfo.getType());
        }

        try {
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            data.put("screenBounds", getBoundingPoints(rect));
        } catch (Exception e) {
            Log.e("AccessibilityNodeInfoMethod", "object2String screenBounds: " + e);
        }

        // 1.序列化
        Parcel p = Parcel.obtain();
        accessibilityNodeInfo.writeToParcel(p, 0);
        byte[] bytes = p.marshall();
        p.recycle();

        // 2.编码
        String source = Base64.encodeToString(bytes, Base64.DEFAULT);
        data.put("source", source);
        return data;
    }

    private HashMap<String, Integer> getBoundingPoints(Rect rect) {
        HashMap<String, Integer> frame = new HashMap<>();
        frame.put("centerY", rect.centerY());
        frame.put("centerX", rect.centerX());
        frame.put("left", rect.left);
        frame.put("right", rect.right);
        frame.put("top", rect.top);
        frame.put("bottom", rect.bottom);
        frame.put("width", rect.width());
        frame.put("height", rect.height());
        return frame;
    }

    public  Parcel stringToObject(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // this is extremely important!
        return parcel;
    }

    public  <T> T stringToObject(String str, Parcelable.Creator<T> creator) {
        // 1.解码
        byte[] bytes = Base64.decode(str, Base64.DEFAULT);
        // 2.反序列化
        Parcel parcel = stringToObject(bytes);
        return creator.createFromParcel(parcel);
    }
}
