package com.yemyatthu.iymb.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.yemyatthu.iymb.R;
import java.io.IOException;

import static com.yemyatthu.iymb.util.Utils.randInt;

public class MindBlownActivity extends ActionBarActivity {
  @InjectView(R.id.gif_image) WebView mGifImage;
  @InjectView(R.id.fact_text_detail) TextView mFactTextDetail;
  @InjectView(R.id.share_btn) ImageButton mShareBtn;
  @InjectView(R.id.back_btn) ImageButton mBackBtn;
  private String mFactText;
  private String[] mGifList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mind_blown);
    ButterKnife.inject(this);
    readImages(this);
    setBitmap();
    mFactText = getIntent().getStringExtra(MyActivity.TAG);
    mFactTextDetail.setText(mFactText);
    mShareBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        share();
      }
    });
    mBackBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        finish();
      }
    });
  }

  public void share() {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT,
        String.format(getString(R.string.share_text), mFactText));
    sendIntent.setType("text/plain");
    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_to)));
  }

  public void readImages(Context context) {
    AssetManager am = context.getAssets();
    try {
      mGifList = am.list("gifs");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setBitmap() {
    mGifImage.loadUrl(
        "file:///android_asset/gifs" + "/" + mGifList[randInt(0, mGifList.length - 1)]);
  }
}
