package com.innee.czyhInterface.util.publicImage.common;

import com.innee.czyhInterface.util.publicImage.util.Json;
import com.innee.czyhInterface.util.publicImage.util.UrlSafeBase64;

/**
 * Created by Simon on 23/12/2016.
 */
public class ZoneReqInfo {
    private final String accessKey;
    private final String bucket;

    public ZoneReqInfo(String token) throws QiniuException {
        // http://developer.qiniu.com/article/developer/security/upload-token.html
        // http://developer.qiniu.com/article/developer/security/put-policy.html
        try {
            String[] strings = token.split(":");
            accessKey = strings[0];
            String policy = new String(UrlSafeBase64.decode(strings[2]), Constants.UTF_8);
            bucket = Json.decode(policy).get("scope").toString().split(":")[0];
        } catch (Exception e) {
            throw new QiniuException(e, "token is invalid");
        }
    }

    public ZoneReqInfo(String accessKey, String bucket) {
        this.accessKey = accessKey;
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getBucket() {
        return bucket;
    }

}