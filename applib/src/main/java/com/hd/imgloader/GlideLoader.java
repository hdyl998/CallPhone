package com.hd.imgloader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hd.R;
import com.hd.base.HdApp;
import com.hd.view.CircularImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * https://blog.csdn.net/qq_19711823/article/details/51243918
 * Created by liugd on 2018/12/23.
 */

public class GlideLoader extends AbsLoader {
    private Context context = HdApp.getContext();

    @Override
    public void loadImage(LoaderBuilder params) {
        if (interceptor != null && interceptor.intercept(params)) {
            return;
        }
//        if(params.isGif){
//            GlideApp.with(context).asGif().load(params.url);
//            return;
//        }
//        RequestOptions requestOptions = RequestOptions.circleCropTransform();
//        Glide.with(context).load(params.url)/*.apply(requestOptions)*/.into(params.imageView);


        RequestOptions options = new RequestOptions();
        if (params.isCircle) {
            options = options.circleCrop()
                    .autoClone();
        } else if (params.isRoundRect) {
            RoundedCorners roundedCorners = new RoundedCorners(params.roundRectRadius);
            options = options.transform(roundedCorners);
        }
        if (params.isBlur) {
            options = options.transform(new BlurTransformation(context, 25));
        }
        options= options.error(params.errorId)
                .placeholder(params.loadingId);
        RequestBuilder<Drawable> requestBuilder;

        if (params.file != null) {
            requestBuilder = Glide.with(context).load(params.file);
        } else {
            requestBuilder = Glide.with(context).load(params.url);
        }
        requestBuilder= requestBuilder.apply(options);

        //自定义的这个view无法加动画
//        if(!(params.imageView instanceof CircularImageView)){
//            requestBuilder=  requestBuilder.transition(withCrossFade());//需要导包
//        }
//                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()))
        requestBuilder .into(params.imageView);
//        DrawableTypeRequest request = Glide.with(context)
//                .load(params.url).crossFade();
//
//
//        if (params.isGif) {
//            request.asGif();
//        }
//        if (params.isPerson) {
//            request.error(R.drawable.ic_pull_arrow_down)
//                    .placeholder(R.drawable.ic_pull_arrow_down);
//        }
//
//        params.crossFade();
//
//
//
//
//                .into(params.target);

//
//

//        DrawableTypeRequest request;
//
//        if (params.file != null) {
//            request = Glide.with(context).load(params.file);
//        } else {
//            request = Glide.with(context).load(params.url);
//        }
//
//        GenericRequestBuilder genericRequestBuilder;
//        if (params.isGif) {
//            genericRequestBuilder = request.asGif().crossFade();
//        } else {
//            genericRequestBuilder = request.crossFade();
//        }
//
//        if (params.isPerson) {
//            genericRequestBuilder=  genericRequestBuilder.error(R.mipmap.ic_pull_arrow_down)
//                    .placeholder(R.mipmap.ic_pull_arrow_down);
//        } else {
//            genericRequestBuilder= genericRequestBuilder.error(R.mipmap.ic_pull_arrow_down)
//                    .placeholder(R.mipmap.ic_pull_arrow_down);
//        }
//        if (params.isCircle) {
//            genericRequestBuilder=  genericRequestBuilder.transform(new CropCircleTransformation(context));
//        } else if (params.isRoundRect) {
//            genericRequestBuilder=genericRequestBuilder.transform(new RoundedCornersTransformation(context, params.roundRectRadius, 0));
//        }
//        if (params.isBlur) {
//            genericRequestBuilder=  genericRequestBuilder.transform(new BlurTransformation(context));
//        }
//
//
//
//        genericRequestBuilder.into(params.imageView);

//        RequestOptions options = new RequestOptions();
//
//        if (params.isCircle) {
//            options.optionalTransform(new CropCircleTransformation(context));
//        } else if (params.isRoundRect) {
//            options.optionalTransform(new RoundedCornersTransformation(context, params.roundRectRadius, 0));
//        }
//        if (params.isBlur) {
//            options.optionalTransform(new BlurTransformation(context));
//        }
//        if (params.isPerson) {
//            options.error(R.drawable.ic_pull_arrow_down)
//                    .placeholder(R.drawable.ic_pull_arrow_down);
//        } else {
//            options.error(R.drawable.ic_pull_arrow_down)
//                    .placeholder(R.drawable.ic_pull_arrow_down);
//        }
//
//
//        GlideApp.with(context)
//                .load(params.url).apply(options)
//                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()))
//                .into(params.imageView);
    }


    public void loaderTest(LoaderBuilder params) {
//        Glide.with(context).load(params.url).transform(new GlideCircleTransUtils(context)).into(params.imageView);
    }

    @Override
    public void clearMemoryCache() {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(context).clearDiskCache();
    }
}
