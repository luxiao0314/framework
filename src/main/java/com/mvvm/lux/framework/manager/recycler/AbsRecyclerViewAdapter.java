package com.mvvm.lux.framework.manager.recycler;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcc on 16/8/4 14:12
 * 100332338@qq.com
 * <p/>
 * RecycleViewAdapter基类
 */
public abstract class AbsRecyclerViewAdapter extends
        RecyclerView.Adapter<AbsRecyclerViewAdapter.ClickableViewHolder> {


    private Context context;

    protected RecyclerView mRecyclerView;

    private List<RecyclerView.OnScrollListener> mListeners = new ArrayList<>();

    public AbsRecyclerViewAdapter(RecyclerView recyclerView) {

        this.mRecyclerView = recyclerView;
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {

                for (RecyclerView.OnScrollListener listener : mListeners) {
                    listener.onScrollStateChanged(rv, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {

                for (RecyclerView.OnScrollListener listener : mListeners) {
                    listener.onScrolled(rv, dx, dy);
                }
            }
        });
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {

        mListeners.add(listener);
    }

    public interface OnItemClickListener {

        public void onItemClick(int position, ClickableViewHolder holder);
    }

    interface OnItemLongClickListener {

        boolean onItemLongClick(int position, ClickableViewHolder holder);
    }

    private OnItemClickListener itemClickListener;

    private OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {

        this.itemLongClickListener = listener;
    }

    public void bindContext(Context context) {

        this.context = context;
    }

    public Context getContext() {

        return this.context;
    }

    @Override
    public void onBindViewHolder(final ClickableViewHolder holder, final int position) {

        holder.getParentView().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position, holder);
                }
            }
        });
        holder.getParentView().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return itemLongClickListener != null
                        && itemLongClickListener.onItemLongClick(position, holder);
            }
        });
    }

    public class ClickableViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding parentView;

        public ClickableViewHolder(ViewDataBinding itemView) {

            super(itemView.getRoot());
            this.parentView = itemView;
        }

        public ViewDataBinding getParentView() {

            return parentView;
        }
    }
}