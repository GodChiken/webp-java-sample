package com.leekyoungil.webp.sample.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kellin on 3/25/16.
 */
public class ResponseFileUploadResult {

    private Integer status = 200;
    private String message;

    volatile private ConcurrentHashMap<String, CopyOnWriteArrayList<ImageInfo>> resultData = new ConcurrentHashMap<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConcurrentHashMap<String, CopyOnWriteArrayList<ImageInfo>> getResultData() {
        return resultData;
    }

    public void setResultData(ConcurrentHashMap<String, CopyOnWriteArrayList<ImageInfo>> resultData) {
        this.resultData = resultData;
    }
}
