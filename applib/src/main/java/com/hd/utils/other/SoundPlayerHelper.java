package com.hd.utils.other;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.RawRes;

import com.hd.base.HdApp;
import com.hd.utils.log.impl.LogUitls;

import java.util.HashMap;

/**
 * 声音播放器，播放小音乐
 * Created by liugd on 2017/1/11.
 */

public class SoundPlayerHelper {


    private final static SoundPlayerHelper instance = new SoundPlayerHelper(HdApp.getContext());


    public static SoundPlayerHelper getInstance() {
        return instance;
    }

    /***
     * 音乐池
     */
    SoundPool soundPool;
    /***
     * 上下文
     */
    Context mContext;


    /***
     * 缓存池
     */
    HashMap<Integer, Integer> hashMap;

    public SoundPlayerHelper(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        // 参数详解(允许同时播放的声音的最大数量,音频类型:默认为AudioManager.STREAM_MUSIC,采样率:即播放质量,
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 100);
        hashMap = new HashMap<>();
    }


    /***
     * 初始音乐池
     *
     * @param rawId 声音资源ID
     */
    public void initSoundPool(int soundKey, @RawRes int rawId) {
        int id = soundPool.load(mContext, rawId, 1);// 参数详解(上下问对象,需要播放的音频的ID
        hashMap.put(soundKey, id);
    }

    /***
     * 初始化音乐池,用rawId代替soundKey
     * @param rawId
     */
    public void initSoundPool(@RawRes int rawId) {
        initSoundPool(rawId, rawId);
    }

    /****
     * 全部初始化
     * @param array
     */
    public void initSoundPoolAll(int array[]) {
        for (int res : array) {
            initSoundPool(res);
        }
    }

    /***
     * 播放声音
     *
     * @param soundKey 键值
     */
    public void playSound(int soundKey) {
        Integer key = hashMap.get(soundKey);
        if (key != null) {
//                AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//                float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
//                float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                float volume = streamVolumeCurrent / streamVolumeMax;
            // 参数：1、Map中取值 2、左声道 3、右声道 4、优先级:默认为1 5、重播次数 6、播放速度
            soundPool.play(key, 1, 1, 1, 0, 1.0f);
            LogUitls.print("soundplayer", soundKey);
        }
    }
}
