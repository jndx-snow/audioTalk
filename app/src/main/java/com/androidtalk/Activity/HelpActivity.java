package com.androidtalk.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.androidtalk.R;

public class HelpActivity extends Activity {
    public TextView tv_help;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.help);

        tv_help = (TextView) findViewById(R.id.tv_help);

    }

}
