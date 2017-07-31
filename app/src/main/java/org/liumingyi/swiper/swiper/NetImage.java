package org.liumingyi.swiper.swiper;

public class NetImage {

  private String picUrl;
  private String linkUrl;
  private String title;

  public NetImage(String picUrl, String linkUrl, String title) {
    this.picUrl = picUrl;
    this.linkUrl = linkUrl;
    this.title = title;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
