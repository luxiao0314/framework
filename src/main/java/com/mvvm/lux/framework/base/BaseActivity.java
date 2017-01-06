package com.mvvm.lux.framework.base;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mvvm.lux.framework.BaseApplication;
import com.mvvm.lux.framework.config.MarkAble;
import com.mvvm.lux.framework.utils.Logger;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Description: BaseActivity
 * Creator: yxc
 * date: 2016/9/7 11:45
 */
public abstract class BaseActivity<T extends BaseViewModel> extends SupportActivity implements MarkAble {

    protected T mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onCreate");
        init();
        initView();
    }

    protected void initView() {
    }

    private void init() {
        setTranslucentStatus(true);
        BaseApplication.getAppContext().registerActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(this.getClass().getName(), this.getClass().getName() + "------>onDestroy");
        BaseApplication.getAppContext().unregisterActivity(this);
        if (mViewModel != null)
            mViewModel.detachView();
    }

    @Override
    public String getInstanceTag() {
        return this.getClass().getSimpleName() + this.hashCode();
    }

    /**
     * 设置沉浸式
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }
}
