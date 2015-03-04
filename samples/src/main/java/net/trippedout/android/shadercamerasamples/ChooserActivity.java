package net.trippedout.android.shadercamerasamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Choose which sample to work with
 */
public class ChooserActivity extends FragmentActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_chooser);

        ButterKnife.inject(this);
    }
    
    @OnClick(R.id.btn_lip_service)
    public void onLipServiceClicked()
    {
        gotoSample(LipServiceActivity.class);
    }

    private <T extends FragmentActivity> void gotoSample(Class<T> activityClass)
    {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

}
