package com.mvvm.lux.framework.config;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mvvm.lux.framework.utils.FileUtil;
import com.mvvm.lux.framework.utils.OkLogger;

/**
 * Created by Tamic on 2016-11-07.
 */

public class ConfigLoader {

    private static Config config;

    private final static String CONFIG_NAME = "novate-config.json";

    public static boolean checkSucess(Context context, int code) {
        loadConfig(context);
        OkLogger.v("retrofitHelpter", "web :" + code + ">>>>>>>>>>>>isOk：" + config.getSucessCode().contains(String.valueOf(code)));
        return config.getSucessCode().contains(String.valueOf(code));
    }

    public static Config loadConfig(Context context) {

        if (config != null) {
            return config;
        }
        String jsonStr = FileUtil.loadFromAssets(context, CONFIG_NAME);
        if (jsonStr == null) {
            return null;
        }
        return config = new Gson().fromJson(jsonStr, Config.class);
    }

    public static boolean isFormat(Context context) {
        loadConfig(context);
        return TextUtils.equals(config.getIsFormat(), "ture");
    }

}
