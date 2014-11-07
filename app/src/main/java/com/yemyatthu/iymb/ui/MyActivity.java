package com.yemyatthu.iymb.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.otto.Subscribe;
import com.yemyatthu.iymb.R;
import com.yemyatthu.iymb.event.BusProvider;
import com.yemyatthu.iymb.event.RefreshEvent;
import com.yemyatthu.iymb.model.Fact;
import com.yemyatthu.iymb.ui.widget.SecretTextView;
import com.yemyatthu.iymb.util.JsonService;
import java.io.File;
import java.util.List;
import java.util.Random;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by yemyatthu on 11/7/14.
 */
public class MyActivity extends ActionBarActivity {
  @InjectView(R.id.fact_text) SecretTextView mFactText;
  @InjectView(R.id.mind_blown_btn) Button mMindBlownBtn;
  @InjectView(R.id.not_yet_btn) Button mNotYetBtn;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;

  private RestAdapter restAdapter;
  private ConnManager connManager;
  private List<Fact> mFactList;
  private boolean mGoodToGo;
  private boolean mLoading;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);
    mLoading = false;
    mGoodToGo = false;
    setRandomText();

    restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.api))
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();

    connManager = new ConnManager(MyActivity.this);
    if(connManager.isConnected()){
      getFactFromApi();
      mGoodToGo = true;
      BusProvider.getInstance().post(new RefreshEvent());
    }
    else{
      File file = new File(getFilesDir()+"/"+"facts.json");
      if(file.exists()){
        mFactList =JsonService.convertToJava(JsonService.loadData(MyActivity.this, "facts.json"));
        mGoodToGo =true;
        int position = randInt(0,mFactList.size()-1);
        mFactText.setText(mFactList.get(position).getText());
        mFactText.show();
        setRandomText();
      }
      else{
        mFactText.setText(getString(R.string.no_net));
        mFactText.show();
      }
    }
    mNotYetBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        BusProvider.getInstance().post(new RefreshEvent());

      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    BusProvider.getInstance().unregister(this);
  }

  @Subscribe public void onRefresh(RefreshEvent refreshEvent){
    if(!mLoading){
      Toast.makeText(MyActivity.this,getString(R.string.loading),Toast.LENGTH_LONG).show();
      return;
    }
    if(!mGoodToGo){
      mFactText.setText(getString(R.string.no_net));
      mFactText.show();
    }
    if(mGoodToGo && mLoading){
      int position = randInt(0,mFactList.size()-1);
      mFactText.setText(mFactList.get(position).getText());
      mFactText.show();
      setRandomText();
    }
  }

  public void getFactFromApi(){
    FactService factService = restAdapter.create(FactService.class);
    factService.getFact(new Callback<List<Fact>>() {
      @Override public void success(List<Fact> facts, Response response) {
        mFactList = facts;
        JsonService.saveData(MyActivity.this,JsonService.convertToJson(facts),"facts.json");
        mLoading = true;
        int position = randInt(0,mFactList.size()-1);
        mFactText.setText(mFactList.get(position).getText());
        mFactText.show();
        setRandomText();

      }

      @Override public void failure(RetrofitError error) {
        Log.d("Error",error.getMessage());

      }
    });
  }

  public int randInt(int min, int max) {

    // NOTE: Usually this should be a field rather than a method
    // variable so that it is not re-seeded every call.
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
  }

  public void setRandomText(){
    String[] topTexts = getResources().getStringArray(R.array.mind_blown_array);
    String[] bottomTexts = getResources().getStringArray(R.array.not_yet_array);

    String topText = topTexts[randInt(0,topTexts.length-1)];
    String bottomText = bottomTexts[randInt(0,bottomTexts.length-1)];
    mMindBlownBtn.setText(topText);
    mNotYetBtn.setText(bottomText);
  }

  public class ConnManager {
    private final Context mContext;

    public ConnManager(Context context) {
      this.mContext = context;
    }

    public boolean isConnected() {
      ConnectivityManager connectivity =
          (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

      if (connectivity != null) {
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
          for (NetworkInfo anInfo : info) {
            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
              return true;
            }
          }
        }
      }
      return false;
    }
  }
}
