package com.clt.upload;
/**
 * 下载监听器
 */
public interface OnUploadListener
{
    void onUploadprogress(long percentSize,long totalSize);
}
