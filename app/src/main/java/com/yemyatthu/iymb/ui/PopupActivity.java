package com.yemyatthu.iymb.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.TextView;
import com.yemyatthu.iymb.R;

/**
 * Created by yemyatthu on 11/8/14.
 */
public class PopupActivity extends ActionBarActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    TextView tv = new TextView(this);
    tv.setText(getString(R.string.about_text));
    tv.setGravity(Gravity.CENTER);
    tv.setPadding(20, 20, 20, 20);
    setContentView(tv);
  }
}
