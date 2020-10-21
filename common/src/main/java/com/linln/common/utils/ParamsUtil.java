package com.linln.common.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(  prefix = "prop" )
public class ParamsUtil {
    private String uploadImg;

    private String ytlAppid;

    private String ytlAppsec;

    private String ytlUrl;

    private String hostUrl;

    private String clientUrl;

    private String smsId;

    private String smsKey;

    private String smsTemplate;

    private String smsSignname;

    private String btcPriceUrl;

    private String rjKey;

    private String endpoint;

    private String bucket;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String imgDir;

    private String ethUrl;

    public String getYtlAppid() {
        return ytlAppid;
    }

    public void setYtlAppid(String ytlAppid) {
        this.ytlAppid = ytlAppid;
    }

    public String getYtlAppsec() {
        return ytlAppsec;
    }

    public void setYtlAppsec(String ytlAppsec) {
        this.ytlAppsec = ytlAppsec;
    }

    public String getUploadImg() {
        return uploadImg;
    }

    public void setUploadImg(String uploadImg) {
        this.uploadImg = uploadImg;
    }

    public String getYtlUrl() {
        return ytlUrl;
    }

    public void setYtlUrl(String ytlUrl) {
        this.ytlUrl = ytlUrl;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSmsKey() {
        return smsKey;
    }

    public void setSmsKey(String smsKey) {
        this.smsKey = smsKey;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public String getSmsSignname() {
        return smsSignname;
    }

    public void setSmsSignname(String smsSignname) {
        this.smsSignname = smsSignname;
    }

    public String getBtcPriceUrl() {
        return btcPriceUrl;
    }

    public void setBtcPriceUrl(String btcPriceUrl) {
        this.btcPriceUrl = btcPriceUrl;
    }

    public String getRjKey() {
        return rjKey;
    }

    public void setRjKey(String rjKey) {
        this.rjKey = rjKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getImgDir() {
        return imgDir;
    }

    public void setImgDir(String imgDir) {
        this.imgDir = imgDir;
    }

    public String getEthUrl() {
        return ethUrl;
    }

    public void setEthUrl(String ethUrl) {
        this.ethUrl = ethUrl;
    }
}
