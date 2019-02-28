package com.callphone.client.main.bean;

import java.io.Serializable;

/**
 * 广告条目的基类
 * Created by liugd on 2017/8/17.
 */

public class BaseAppGoItem implements Serializable {

    public String title;//标题展示
    public String img;//图片
    public String type;//类型
    public String url;//执行的结果

    public int linktype = TYPE_INT_UNDEFINE;//type的替换类型，如果有值优先取这个类型，无值取type类型


    public BaseAppGoItem() {
    }

    public BaseAppGoItem(String title) {
        this.title = title;
    }

    public BaseAppGoItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public final static String TYPE_WEB = "web";
    public final static String TYPE_APP = "app";
    public final static String TYPE_NONE = "none";


    public final static int TYPE_INT_WEB = 2;
    public final static int TYPE_INT_LOCAL = 1;
    public final static int TYPE_INT_NONE = 0;
    public final static int TYPE_INT_UNDEFINE = -1;

    /***
     * 修正type类型值，当type字段被占用时采用linktype传值
     */
    public void doCorrectType() {
        //linktype不为空，则优先取这个值
        if (linktype != TYPE_INT_UNDEFINE) {
            switch (linktype) {
                case TYPE_INT_LOCAL:
                    type = TYPE_APP;
                    break;
                case TYPE_INT_WEB:
                    type = TYPE_WEB;
                    break;
                default:
                    type = TYPE_NONE;
                    break;
            }
        }
    }


    public boolean isWebType() {
        return TYPE_WEB.equals(type);
    }

    public boolean isAppType() {
        return TYPE_APP.equals(type);
    }

    public void setAppType() {
        this.type = TYPE_APP;
    }

    public void setWebType() {
        this.type = TYPE_WEB;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

}
