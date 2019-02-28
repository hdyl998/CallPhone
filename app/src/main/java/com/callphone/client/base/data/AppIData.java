//package com.callphone.client.base.data;
//
//
//import com.hd.cache.SpUtils;
//import com.hd.utils.Utils;
//import com.topsc.appconfig.AppConfigFactory;
//import com.topsc.base.SPConstants;
//import com.topsc.utils.cache.ImDataCache;
//
///**
// * Created by Administrator on 2018/1/6.
// */
//
//public class AppIData {
//    private String suid;//机器特征码
//    private String channelLocal;//机器本地渠道
//    private final String channelXml;//机器真实渠道
//    private String channelXmlCheat;//欺骗真实渠道,用于测试用查看某个渠道是否隐藏
//
//    private String version;//本地获取的版本号
//    private final String versionXml;
//
//    public final static String KEY_CHANNEL = "datachannel_sfty";
//    public final static String KEY_SUID = "datauidfaffaa_sfty";
//    public final static String KEY_SUPERFUN_VERSION_CODE = "super_fun_version";
//    public final static String KEY_SUPERFUN_CHANNEL = "super_fun_channel";
//
//
//    private final static AppIData instance = new AppIData();
//
//    public static AppIData getInstance() {
//        return instance;
//    }
//
//
//    private AppIData() {
//        channelXml = Utils.getAppMetaData("TD_CHANNEL_ID");//去取xml渠道
//        versionXml = Utils.getVersionName();
//        init();
//    }
//
//
//    private void init() {
//        ImDataCache cache = new ImDataCache();
//        suid = cache.getString(KEY_SUID);
//        channelLocal = cache.getString(KEY_CHANNEL);
////        restoreHistoryData();//恢复历史数据
//        if (channelLocal == null) {
//            setChannelLocal(channelXml);
//        }
//        version = SpUtils.getString(SPConstants.FILE_developer, KEY_SUPERFUN_VERSION_CODE);
//        if (version == null) {
//            setVersion(versionXml);
//        }
//        channelXmlCheat = SpUtils.getString(SPConstants.FILE_developer, KEY_SUPERFUN_CHANNEL);
//        if (channelXmlCheat == null) {
//            setChannelXmlCheat(channelXml);
//        }
//    }
//
//    private static final String TAG = "AppIData";
//
//
//    public void setVersion(String version) {
//        this.version = version;
//        SpUtils.putString(SPConstants.FILE_developer, KEY_SUPERFUN_VERSION_CODE, version);
//    }
//
//    public String getVersion() {
//        if (AppConfigFactory.getConfig().isOnline()) {
//            return versionXml;
//        }
//        return version;
//    }
//
//
//    public String getVersionXml() {
//        return versionXml;
//    }
//
//    public String getSuid() {
//        return suid;
//    }
//
//    public void setSuid(String suid) {
//        this.suid = suid;
//        ImDataCache cache = new ImDataCache();
//        cache.putString(KEY_SUID, suid);
//    }
//
//
//    private void setChannelLocal(String channel) {
//        this.channelLocal = channel;
//        ImDataCache cache = new ImDataCache();
//        cache.putString(KEY_CHANNEL, channel);
//    }
//
//    public String getChannelLocal() {
//        return channelLocal;
//    }
//
//    public String getChannelXml() {
//        return channelXml;
//    }
//
//    public String getChannelXmlCheat() {
//        return channelXmlCheat;
//    }
//
//    public void setChannelXmlCheat(String channelXmlCheat) {
//        this.channelXmlCheat = channelXmlCheat;
//        SpUtils.putString(SPConstants.FILE_developer, KEY_SUPERFUN_CHANNEL, channelXmlCheat);
//    }
//}
