package com.mvvm.lux.framework.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.mvvm.lux.framework.R;
import com.mvvm.lux.framework.widget.SwipeBackLayout;


/**
 * @Description 1、想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 * 2、设置activity的主题为android:theme="@style/CustomTransparent
 * @Author luxiao418
 * @Email luxiao418@pingan.com.cn
 * @Date 2017/1/4 15:02
 * @Version
 */
public abstract class SwipeBackActivity extends BaseActivity {
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        swipeBackLayout.attachToActivity(this);
    }

    //默认左滑
    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        swipeBackLayout.setDragEdge(dragEdge);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }
}
