package com.yemyatthu.iymb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Fact {

  @Expose
  private String text;
  @Expose
  private String video;
  @Expose
  private String twitter;
  @Expose
  private String number;
  @SerializedName("social_tag") @Expose
  private String socialTag;

  /**
   * @return The text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text The text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @return The video
   */
  public String getVideo() {
    return video;
  }

  /**
   * @param video The video
   */
  public void setVideo(String video) {
    this.video = video;
  }

  /**
   * @return The twitter
   */
  public String getTwitter() {
    return twitter;
  }

  /**
   * @param twitter The twitter
   */
  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  /**
   * @return The number
   */
  public String getNumber() {
    return number;
  }

  /**
   * @param number The number
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * @return The socialTag
   */
  public String getSocialTag() {
    return socialTag;
  }

  /**
   * @param socialTag The social_tag
   */
  public void setSocialTag(String socialTag) {
    this.socialTag = socialTag;
  }
}