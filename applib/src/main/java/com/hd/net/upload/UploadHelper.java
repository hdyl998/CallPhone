package com.hd.net.upload;

import com.hd.utils.log.impl.LogUitls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liugd on 2019/1/7.
 */

public class UploadHelper {

    public static final String TAG = "UploadHelper";
    private static final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();

    public String upload(String imageType, String userPhone, File file) throws Exception {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
///                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + fileName + "\""),
//                        RequestBody.create(MEDIA_TYPE_PNG, file))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"imagetype\""),
//                        RequestBody.create(null, imageType))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"userphone\""),
//                        RequestBody.create(null, userPhone))

                .addFormDataPart("file", "headimg", RequestBody.create(MEDIA_TYPE_IMG, file))
                .addFormDataPart("imagetype", imageType)
                .addFormDataPart("userphone", userPhone)
                .build();

        Request request = new Request.Builder()
                .url("http://api2.topspeedcloud.com/index.php?s=index/user/dealimg/")
                .post(requestBody)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            LogUitls.print(TAG, " upload jsonString =" + jsonString);

            if (!response.isSuccessful()) {
                throw new Exception("upload error code " + response);
            } else {
                JSONObject jsonObject = new JSONObject(jsonString);
                int errorCode = jsonObject.getInt("errorCode");
                if (errorCode == 0) {
                    LogUitls.print(TAG, " upload data =" + jsonObject.getString("data"));
                    return jsonObject.getString("data");
                } else {
                    throw new Exception("upload error code " + errorCode + ",errorInfo=" + jsonObject.getString("errorInfo"));
                }
            }

        } catch (IOException e) {
            LogUitls.print(TAG, "upload IOException " + e);
        } catch (JSONException e) {
            LogUitls.print(TAG, "upload JSONException " + e);
        }
        return null;
    }
}