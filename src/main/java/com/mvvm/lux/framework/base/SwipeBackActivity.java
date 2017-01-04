package com.mvvm.lux.framework.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.mvvm.lux.framework.R;
import com.mvvm.lux.framework.widget.SwipeBackLayout;


/**
 * @Description 1、想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 *                只需要调用SwipeBackLayout的setViewPager()方法即可
 *              2、设置activity的主题为android:theme="@style/CustomTransparent
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2017/1/4 15:02
 * @Version
 */
public abstract class SwipeBackActivity extends BaseActivity {
    private SwipeBackLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
