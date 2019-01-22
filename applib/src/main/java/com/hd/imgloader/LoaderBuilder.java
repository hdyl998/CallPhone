package com.hd.imgloader;

import android.widget.ImageView;

import com.hd.R;
import com.hd.utils.DensityUtils;

import java.io.File;

/**
 * Created by liugd on 2018/12/23.
 */

public class LoaderBuilder {

    public String url;
    ImageView imageView;
    LoadListener listener;
    boolean isGif;
    boolean isRoundRect;
    int roundRectRadius;
    boolean isCircle;
    boolean isBlur;
    int blurVar=15;//1-25
    File file;
    int errorId= R.mipmap.nothing;
    int loadingId=R.mipmap.nothing;



    public LoaderBuilder asBlur(){
        this.isBlur = true;
        return this;
    }

    public LoaderBuilder asBlur(int blurVar){
        this.isBlur = true;
        if(blurVar<1||blurVar>25){
            throw new RuntimeException("blurVar值错误");
        }
        this.blurVar=blurVar;
        return this;
    }
    public LoaderBuilder asFile(File file){
        this.file=file;
        return this;
    }

    public LoaderBuilder asFile(String filePath){
        return asFile(new File(filePath));
    }


    public LoaderBuilder asRoundRect(int roundRectRadius){
        this.roundRectRadius = roundRectRadius;
        isRoundRect =true;
        return this;
    }

    public LoaderBuilder asRoundRect(){
        return asRoundRect(DensityUtils.getDimenPx(6));
    }

    public LoaderBuilder asCircle(){
        this.isCircle = true;
        return this;
    }

    public static LoaderBuilder get(){
        return new LoaderBuilder();
    }

    public LoaderBuilder asPerson() {
        errorId= R.mipmap.ic_default_person;
        loadingId=R.mipmap.ic_default_person;
        return this;
    }

    public LoaderBuilder setListener(LoadListener listener) {
        this.listener = listener;
        return this;
    }

    public LoaderBuilder asGif(){
        isGif=true;
        return this;
    }

    public void loadImage(ImageView imageView){
        this.imageView=imageView;
        LoaderFactory.getLoader().loadImage(this);
    }


    public void loadImage(ImageView imageView,String url){
        this.url=url;
        loadImage(imageView);
    }
}
