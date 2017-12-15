package org.marekchen.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by chenpei on 2017/12/15.
 */

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.set_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setAlarm();
            }
        });
    }
}
