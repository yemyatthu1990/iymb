package com.yemyatthu.iymb.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.yemyatthu.iymb.util.Utils.randInt;

/**
 * Created by yemyatthu on 11/7/14.
 */
public class MyActivity extends ActionBarActivity {
  public static final String TAG = "com.yemyatthu.iymb.ui.MyActivity.TAG";
  @InjectView(R.id.fact_text) SecretTextView mFactText;
  @InjectView(R.id.mind_blown_btn) Button mMindBlownBtn;
  @InjectView(R.id.not_yet_btn) Button mNotYetBtn;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;
  private RestAdapter restAdapter;
  private ConnManager connManager;
  private List<Fact> mFactList;
  private File file;
  private boolean mGoodToGo;
  private boolean mLoading;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    setSupportActionBar(toolbar);
    ButterKnife.inject(this);
    mLoading = false;
    mGoodToGo = false;
    setRandomText();

    restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.api))
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();

    connManager = new ConnManager(MyActivity.this);
    file = new File(getFilesDir() + "/" + "facts.json");
    if (file.exists()) {
      mFactList = JsonService.convertToJava(JsonService.loadData(MyActivity.this, "facts.json"));
      mGoodToGo = true;
      mLoading = true;
      int position = randInt(0, mFactList.size() - 1);
      mFactText.setText(mFactList.get(position).getText());
      mFactText.show();
      setRandomText();
    } else {
      if (connManager.isConnected()) {
        getFactFromApi();
        mGoodToGo = true;
      } else {
        mFactText.setText(getString(R.string.no_net));
        mFactText.show();
      }
    }

    mMindBlownBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent i = new Intent(MyActivity.this, MindBlownActivity.class);
        i.putExtra(TAG, mFactText.getText().toString());
        startActivity(i);
      }
    });
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

  @Subscribe public void onRefresh(RefreshEvent refreshEvent) {
    if (!file.exists() && connManager.isConnected()) {
      getFactFromApi();
      mGoodToGo = true;
    }
    if (!mLoading && mGoodToGo) {
      Toast.makeText(MyActivity.this, getString(R.string.loading), Toast.LENGTH_LONG).show();
      return;
    }
    if (!mGoodToGo) {
      mFactText.setText(getString(R.string.no_net));
      mFactText.show();
    }
    if (mGoodToGo && mLoading) {
      int position = randInt(0, mFactList.size() - 1);
      mFactText.setText(mFactList.get(position).getText());
      mFactText.show();
      setRandomText();
    }
  }

  public void getFactFromApi() {
    FactService factService = restAdapter.create(FactService.class);
    factService.getFact(new Callback<List<Fact>>() {
      @Override public void success(List<Fact> facts, Response response) {
        mFactList = facts;
        JsonService.saveData(MyActivity.this, JsonService.convertToJson(facts), "facts.json");
        mLoading = true;
        int position = randInt(0, mFactList.size() - 1);
        mFactText.setText(mFactList.get(position).getText());
        mFactText.show();
        setRandomText();
      }

      @Override public void failure(RetrofitError error) {
        Log.d("Error", error.getMessage());
      }
    });
  }

  public void setRandomText() {
    String[] topTexts = getResources().getStringArray(R.array.mind_blown_array);
    String[] bottomTexts = getResources().getStringArray(R.array.not_yet_array);

    String topText = topTexts[randInt(0, topTexts.length - 1)];
    String bottomText = bottomTexts[randInt(0, bottomTexts.length - 1)];
    mMindBlownBtn.setText(topText);
    mNotYetBtn.setText(bottomText);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.about_menu:
        Intent i = new Intent(this, PopupActivity.class);
        startActivity(i);
    }
    return super.onOptionsItemSelected(item);
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
