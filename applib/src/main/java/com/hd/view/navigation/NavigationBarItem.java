package com.hd.view.navigation;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Administrator on 2018/3/23.
 */

public class NavigationBarItem {

    public String normalUrl;//普通图片
    public String checkedUrl;//选中图片


    public int normalRes;//没选中的资源id
    public int checkedRes;//选中的资源id

    public boolean isNetImg = true;

    private boolean isRedDot = false;//小红点

    private int msgNum;

    public String tvName;//文字

    public int itemId;//条目ID

    private boolean isClick;//是否是点击跳转的tab


    private Object tag;

    public boolean isClick() {
        return isClick;
    }

    public String getMsgText() {
        if (msgNum > 99) {
            return "99+";
        }
        return  String.valueOf(msgNum);
    }

    public boolean isShowMsgNum(){
        return msgNum!=0;
    }

    public boolean isRedDot() {
        return isRedDot;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @JSONField(serialize = false)
    public Object getTag() {
        return tag;
    }

    public void setRedDot(boolean redDot) {
        this.isRedDot = redDot;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public int getMsgNum() {
        return msgNum;
    }



    //    public final static class HomeTabsItem extends BaseAppGoItem {
//        public int isclick = 0;
//        public boolean isClick() {
//            return isclick == 1;
//        }
//    }


//    public NavigationBarItem(HomeTabsItem homeTabs, int itemId, Object tag) {
//        this.tvName = homeTabs.getTitle();
//        String imgs[] = homeTabs.getImg().split(",");
//        if (imgs.length < 2) {
//            this.checkedUrl = this.normalUrl = homeTabs.getImg();
//        } else {
//            this.normalUrl = imgs[0];//没选中,选中这种格式
//            this.checkedUrl = imgs[1];
//        }
//        this.itemId = itemId;
//        this.tag = tag;
//        this.isNetImg = true;
//        this.isClick = homeTabs.isClick();
//    }

    public NavigationBarItem(String tagName, int itemId, int checkedRes, int normalRes, Object tag) {
        this.tvName = tagName;
        this.itemId = itemId;
        this.checkedRes = checkedRes;
        this.normalRes = normalRes;
        this.tag = tag;
        this.isNetImg = false;
    }

}
