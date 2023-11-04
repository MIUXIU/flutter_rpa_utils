package fun.tszsy.flutter_rpa_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import fun.tszsy.flutter_rpa_utils.service.AccessibilityListener;
import fun.tszsy.flutter_rpa_utils.service.AccessibilityReceiver;
import fun.tszsy.flutter_rpa_utils.service.Utils;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/**
 * FlutterAccessibilityServicePlugin
 */
public class FlutterAccessibilityServicePlugin implements FlutterPlugin, ActivityAware, MethodCallHandler,
        PluginRegistry.ActivityResultListener, EventChannel.StreamHandler {

    private static final String CHANNEL_TAG = "flutter_rpa_utils";
    private static final String EVENT_TAG = "x-slayer/accessibility_event";

    private MethodChannel channel;
    private AccessibilityReceiver accessibilityReceiver;
    private EventChannel eventChannel;
    private Context context;
    private Activity mActivity;

    private Result pendingResult;
    final int REQUEST_CODE_FOR_ACCESSIBILITY = 167;

    private RPAManager rpaManager;

    private Gson gson;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_TAG);
        channel.setMethodCallHandler(this);
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), EVENT_TAG);
        eventChannel.setStreamHandler(this);

        gson = new Gson();
        Context applicationContext = flutterPluginBinding.getApplicationContext();
        rpaManager = new RPAManager(applicationContext);
        rpaManager.init();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        pendingResult = result;
        if (call.method.equals("isAccessibilityPermissionEnabled")) {
            result.success(Utils.isAccessibilitySettingsOn(context));
        } else if (call.method.equals("requestAccessibilityPermission")) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            mActivity.startActivityForResult(intent, REQUEST_CODE_FOR_ACCESSIBILITY);
        } else if (call.method.equals("clickButtonByText")) {

            try {
                String text = call.argument("text");
                int index = call.argument("index");
                if (TextUtils.isEmpty(text)) {
                    result.success(false);
                    return;
                }
                List<AccessibilityNodeInfo> accessibilityNodeInfoList =
                        RPAManager.rootNode.findAccessibilityNodeInfosByText(text);
                if (accessibilityNodeInfoList == null || accessibilityNodeInfoList.size() == 0) {
                    result.success(false);
                    return;
                }
                RPAToolsUtils.clickButton(RPAManager.accessibilityService,accessibilityNodeInfoList.get(index));
            } catch (Exception e) {
                result.error("error",e.toString(),"");
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        eventChannel.setStreamHandler(null);
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        if (Utils.isAccessibilitySettingsOn(context)) {
            /// Set up receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AccessibilityListener.ACCESSIBILITY_INTENT);

            accessibilityReceiver = new AccessibilityReceiver(events);
            context.registerReceiver(accessibilityReceiver, intentFilter);

            /// Set up listener intent
            Intent listenerIntent = new Intent(context, AccessibilityListener.class);
            context.startService(listenerIntent);
            Log.i("AccessibilityPlugin", "Started the accessibility tracking service.");
        }
    }

    @Override
    public void onCancel(Object arguments) {
        context.unregisterReceiver(accessibilityReceiver);
        accessibilityReceiver = null;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_ACCESSIBILITY) {
            if (resultCode == Activity.RESULT_OK) {
                pendingResult.success(true);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                pendingResult.success(Utils.isAccessibilitySettingsOn(context));
            } else {
                pendingResult.success(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.mActivity = binding.getActivity();
        binding.addActivityResultListener(this);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.mActivity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        this.mActivity = null;
    }
}
