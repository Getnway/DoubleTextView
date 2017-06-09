package com.getnway.doubletextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private DoubleTextView dtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dtv = (DoubleTextView) findViewById(R.id.dtv);
        dtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rightText = dtv.getTextRight().getText().toString().trim();
                if ("".equals(rightText)) {
                    dtv.getTextRight().setText("再点击一次");
                } else {
                    dtv.getTextRight().setText("");
                }
            }
        });
    }
}
