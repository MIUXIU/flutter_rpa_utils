package fun.tszsy.flutter_rpa_utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterRpaUtilsPlugin */
public class FlutterRpaUtilsPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private RPAManager rpaManager;

  private Gson gson;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_rpa_utils");
    channel.setMethodCallHandler(this);
    gson = new Gson();
    Context applicationContext = flutterPluginBinding.getApplicationContext();
    rpaManager = new RPAManager(applicationContext);
    rpaManager.init();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    switch (call.method){//根据方法名进行处理
      case "findText":
        rpaManager.findText(call.argument("content"));

        String gsonString = gson.toJson(RPAManager.rootNode);
        Log.d("ZZZS", "gsonString: "+gsonString);
        result.success(gsonString);
        break;
      default:
        result.notImplemented();
    }


  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
