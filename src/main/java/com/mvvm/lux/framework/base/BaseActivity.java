package com.mvvm.lux.framework.base;

import android.os.Bundle;
import android.view.KeyEvent;

import com.mvvm.lux.framework.config.MarkAble;
import com.mvvm.lux.framework.manager.router.Router;

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
        initView();
    }

    protected void initView() {
    }

    @Override
    public String getInstanceTag() {
        return this.getClass().getSimpleName() + this.hashCode();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Router.pop(this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
