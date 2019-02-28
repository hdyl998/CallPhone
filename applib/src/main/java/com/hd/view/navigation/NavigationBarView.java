package com.hd.view.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.HdApp;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.FindViewAdapter;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.imgloader.LoaderBuilder;
import com.hd.utils.ViewUtils;
import com.hd.utils.log.impl.LogUitls;

import java.util.HashMap;
import java.util.List;

/**
 * 导航栏
 * Created by liugd on 2017/6/29.
 */
public class NavigationBarView extends LinearLayout implements View.OnClickListener {

    public int selectIndex = 0;//选中的索引
    private FindViewAdapter<NavigationBarItem> adapter;

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    List<NavigationBarItem> dataItems;
//    View centerView;

    public int getSelectIndex() {
        return selectIndex;
    }

    HashMap<Integer, Integer> itemId2IndexHashMap;

    public void setDataItems(List<NavigationBarItem> dataItems) {
        this.dataItems = dataItems;
        setOrientation(HORIZONTAL);
        int idArray[] = new int[dataItems.size()];

        itemId2IndexHashMap = new HashMap<>(dataItems.size());
        int index = 0;
        for (NavigationBarItem item : dataItems) {
            itemId2IndexHashMap.put(item.itemId, index);
            index++;
        }

        for (int i = 0; i < dataItems.size(); i++) {
            View view = ViewUtils.getInflateView(this, R.layout.item_navigation_bar);
            view.setId(1000 + i);
            idArray[i] = view.getId();
            this.addView(view);
        }

//        //在中间添加一个
//        centerView = ViewUtils.getInflateView(this, R.layout.item_navigation_bar);
//        centerView.setVisibility(View.GONE);
//        this.addView(centerView, dataItems.size() / 2);


        adapter = new FindViewAdapter<NavigationBarItem>(this, dataItems, idArray) {
            @Override
            public void onBind(BaseViewHolder holder, NavigationBarItem item, int position) {
                View viewRoot = holder.getItemView();
                viewRoot.setTag(position);
                viewRoot.setOnClickListener(NavigationBarView.this);


                TextView textView = holder.getView(R.id.textView);
                textView.setText(item.tvName);

                textView.setVisibility(item.tvName==null?GONE:VISIBLE);

                ImageView imageView = holder.getView(R.id.imageView);
                if(isClickAnimation) {
                    //用于动画
                    viewRoot.setTag(R.id.tagDefault, imageView);
                }
                //使用网络图片
                if (item.isNetImg) {
                    if (selectIndex == position) {
                        textView.setTextColor(selectedColor);
                        LoaderBuilder.get().loadImage(imageView, item.checkedUrl);
                    } else {
                        textView.setTextColor(unSelectedColor);
                        LoaderBuilder.get().loadImage(imageView, item.normalUrl);
                    }
                } else {
                    if (selectIndex == position) {
                        textView.setTextColor(selectedColor);
                        imageView.setImageResource(item.checkedRes);
                    } else {
                        textView.setTextColor(unSelectedColor);
                        imageView.setImageResource(item.normalRes);
                    }
                }
                holder.setVisibility(R.id.iv_dot, item.isRedDot && selectIndex != position ? View.VISIBLE : View.GONE);
            }
        };
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public NavigationBarItem getMineDataItem(int selectIndex) {
        return dataItems.get(selectIndex);
    }


    View lastAnimationView;

    Animation animation;

    @Override
    public void onClick(View v) {
        int newIndex = (int) v.getTag();
        if (newIndex != this.selectIndex) {//过虑掉相同的索引
            if (clickCallBacks != null) {
                NavigationBarItem item = dataItems.get(newIndex);
                clickCallBacks.call(item);
                LogUitls.print("点击", newIndex);
                LogUitls.print("点击", item);

                if(isClickAnimation) {
                    if (lastAnimationView != null) {
                        lastAnimationView.clearAnimation();
                    }
                    lastAnimationView = (View) v.getTag(R.id.tagDefault);
                    if (animation == null) {
                        animation = AnimationUtils.loadAnimation(getContext(), R.anim.small_2_big);
                    }
                    lastAnimationView.startAnimation(animation);
                }
            }
        }
    }

    boolean isClickAnimation=true;

    /*
    点击动画
    */
    public void setClickAnimation(boolean clickAnimation) {
        isClickAnimation = clickAnimation;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (lastAnimationView != null) {
            lastAnimationView.clearAnimation();
        }
    }

    /***
     * 通过ID设置索引
     */
    public void setSelectById(int itemId) {
        int index = getIndexById(itemId);
        if (index != -1) {
            setSelectIndex(index);
        }
    }

    /***
     * 通过ID查找TAG
     * @param itemId
     * @return
     */
    public <T> T getTagById(int itemId) {
        int index = getIndexById(itemId);
        if (index != -1) {
            return (T) dataItems.get(index).getTag();
        }
        return null;
    }

    /***
     * 通过索引查找TAG
     * @param index
     * @return
     */
    public <T> T getTagByIndex(int index) {
        if (index < dataItems.size()) {
            return (T) dataItems.get(index).getTag();
        }
        return null;
    }

    /****
     * 根据ID找索引
     * @param itemId
     */
    public int getIndexById(int itemId) {
        Integer integer = itemId2IndexHashMap.get(itemId);
        if (integer == null) {
            return -1;
        }
        return integer;
    }


    /***
     * 传入itemId 确定是否是当前页
     * @param itemId
     * @return
     */
    public boolean isCurrentPage(int itemId){
        return selectIndex==getIndexById(itemId);
    }


    /****
     * 设置选中页码
     * @param newIndex
     */
    public void setSelectIndex(int newIndex) {
        //修正页码问题
        if (this.selectIndex != newIndex && newIndex < dataItems.size()) {
            this.selectIndex = newIndex;
        }
        this.notifyDataSetChanged();
    }


    IComCallBacks<NavigationBarItem> clickCallBacks;


    public void setClickCallBacks(IComCallBacks<NavigationBarItem> clickCallBacks) {
        this.clickCallBacks = clickCallBacks;
    }

    public NavigationBarItem getDataItemById(int pageId) {
        int index = getIndexById(pageId);
        if (index == -1) {
            return null;
        }
        return dataItems.get(index);
    }


    public void setRedPoint(int pageId, boolean isRed) {
        NavigationBarItem mineItem = getDataItemById(pageId);
        if (mineItem != null) {
            mineItem.setRedDot(isRed);
        }

    }


    //颜色定制
    private int selectedColor = 0xff6395e0;
    private final int unSelectedColor = 0xffB7B7B7;

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }
}