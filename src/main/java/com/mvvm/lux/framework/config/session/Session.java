package com.mvvm.lux.framework.config.session;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mvvm.lux.framework.config.FrameWorkConfig;
import com.mvvm.lux.framework.config.FrameworkConstants;
import com.mvvm.lux.framework.config.LocalBroadcast;
import com.mvvm.lux.framework.config.UnProguard;
import com.mvvm.lux.framework.utils.SharePreferencesUtil;

/**
 * Created by iceman on 16/3/18 14:17
 * 邮箱：xubin865@pingan.com.cn
 * 关于islogin和isRegister:
 * 所有手机号码+收到的短信验证码都可以登录.
 * 但是只有后台绑定了地区id的才代表正式注册的医生账号.
 * 对用户而言,两个流程是一体的.
 * 登录的账号如果没有绑定地区,那么最终也不能算登录状态.
 */
public class Session {

    private static User user;

    private static ThirdUser thirdUser;

    private static LoginManager loginManager = new LoginManager();

    public static User getUser() {
        if (user == null) {
            readFromSp();
        }
        return user;
    }

    public static ThirdUser getThirdUser(){
        if (null == thirdUser){
            readThirdUserFromSp();
        }

        return thirdUser;
    }

    public void setUser() {
        SharePreferencesUtil.saveString(FrameworkConstants.SaveKeys.USER_TOKEN, user.token);
    }

    public static String getToken() {
        if (isLogin()) {
            return user.token;
        } else {
            return "";
        }
    }

    public static String getZoneCode() {
        User user = getUser();
//        if (user.isBindCard) {
        return user.zoneCode;
//        } else {
//            return "440300";
//        }
    }
    public static String getZoneName() {
        User user = getUser();
        if (user.isBindCard) {
            return user.zoneName;
        } else {
            return "";
        }
    }

    public static void startLogin(FragmentActivity activity, LoginManager.LoginCallback loginCallback) {
        loginManager.setLoginCallback(loginCallback);
        FrameWorkConfig.frameworkSupport.goToActivity(activity,10708,null,null);
    }

    /**
     * 用户是否登录.包含未绑卡的情况
     *
     * @return
     */
    public static boolean isLogin() {
        User user = getUser();
        return user.isLogin;
//        return user.isLogin && user.isRegister;
    }

    /**
     * 用户是否已经绑卡
     *
     * @return
     */
    public static boolean isBindCard() {
        User user = getUser();
        return user.isLogin && user.isBindCard;
    }

    public static void saveUserinfo() {
        SharePreferencesUtil.saveString(FrameworkConstants.SaveKeys.SESSION_KEY, new Gson().toJson(user));
    }

    public static void saveThirdUserinfo() {
        SharePreferencesUtil.saveString(FrameworkConstants.SaveKeys.THIRD_USER_INFO, new Gson().toJson(thirdUser));
    }

    private static void readFromSp() {
        String saveUserString = SharePreferencesUtil.getString(FrameworkConstants.SaveKeys.SESSION_KEY, "");
        if (!TextUtils.isEmpty(saveUserString)) {
            user = new Gson().fromJson(saveUserString, User.class);
        } else {
            user = new User();
        }
    }

    private static void readThirdUserFromSp(){
        String thirdUserString = SharePreferencesUtil.getString(FrameworkConstants.SaveKeys.THIRD_USER_INFO, "");

        if (!TextUtils.isEmpty(thirdUserString)) {
            thirdUser = new Gson().fromJson(thirdUserString, ThirdUser.class);
        } else {
            thirdUser = new ThirdUser();
        }
    }

    public static void logout() {
        user = new User();
        FrameWorkConfig.frameworkSupport.onTokenInvalid();
        Session.thirdLogout();
        SharePreferencesUtil.saveString(FrameworkConstants.SaveKeys.SESSION_KEY, "");
        LocalBroadcast.sendLocalBroadcast(FrameworkConstants.ActionKeys.LOG_OUT);
    }

    public static void thirdLogout() {
        thirdUser = new ThirdUser();

        SharePreferencesUtil.saveString(FrameworkConstants.SaveKeys.THIRD_USER_INFO, "");
    }

    public static void bindout() {
        User user = getUser();
        user.isBindCard = false;
        user.siCard = null;
        user.birthday = null;
        user.hiredate = null;
        user.retired = null;
        user.sex = null;

        saveUserinfo();
        LocalBroadcast.sendLocalBroadcast(FrameworkConstants.ActionKeys.LOGIN);

    }

    /**
     * 是否suum认证
     * @return
     */
    public static boolean isSuumed() {
        if (null !=user && !TextUtils.isEmpty(user.suumUserId)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isV2Level() {
        if (user == null) {
            return false;
        } else if ("V2".equals(user.certLevel) || "V3".equals(user.certLevel)) {
            return true;
        }
        return false;
    }

    public static boolean isV3Level() {
        if (user == null) {
            return false;
        } else if ("V3".equals(user.certLevel)) {
            return true;
        }
        return false;
    }

    public static class User implements UnProguard {
        /**
         * 是否登录
         */
        public boolean isLogin = false;
        /**
         * 是否已绑定卡号
         */
        public boolean isBindCard = false;
        public String systemCategory;
        public String phoneNumber;
        public String siCard;
        public String idCard;
        public String zoneCode = "440300";
        public String zoneName;
        public String depart;
        public String hiredate;
        public String retired;
        public String regionCode;
        public String deviceid;
        public String completePhoneNumber;//不脱敏的手机号信息

        public String avatarUrl;    //微信头像,微信登录时才返回
        public boolean bindBankCard;    //是否绑定银行卡
        public boolean bindSiCard;    //是否绑定社保卡
        public String birthday; //出生日期
        public String certLevel; //账户认证等级
        public String pinganfuToken; //平安付token
        public String realName; //姓名
        public String secondToken; //第2方token
        public String sex;  //性别
        public String suumUserId;  //对接suum平台的Id
        public String token;  //用户唯一标示
        public String userId;  //用户id,暂时使用,以后会去掉,目前需要登录才能操作的接口,同时传userId和token
        public String userName;  //用户名(opt登录为手机号)   脱敏之后
        public String thirdToken;  //传给suum平台的token
        public String userInfo;  //用户信息加密
        public String idType;  //"L1"
        public String unionid;  //微信登录授权id
        public String email;
        public String password;

        User() {
        }

        @Override
        public String toString() {
            return isLogin + "-" + userName + "-" + token + "-" + phoneNumber;
        }

    }

    public static class ThirdUser implements UnProguard {
        /**
         * 第三方用户token，作为请求的唯一凭证
         * 对应成都的aac001字段
         */
        public String thirdUserToken;
        /**
         * 第三方用户姓名
         * 对应成都的aac003字段
         */
        public String thirdUserName;
        /**
         * 第三方用户手机号
         * 对应成都的tel字段
         */
        public String thirdUserPhoneNumber;
        /**
         * 第三方用户卡号
         * 对应成都的aac002字段
         */
        public String thirdUserIdCard;

        ThirdUser() {

        }

        @Override
        public String toString() {
            return thirdUserName + "-" + thirdUserName + "-" + thirdUserToken + "-" + thirdUserToken;
        }

    }


}
