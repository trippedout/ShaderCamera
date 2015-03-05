package net.trippedout.android.shadercamerasamples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.SeekBar;

import net.trippedout.android.shadercamera.fragments.CameraFragment;
import net.trippedout.android.shadercamera.gl.CameraRenderer;
import net.trippedout.android.shadercamera.view.AutoFitTextureView;
import net.trippedout.android.shadercamerasamples.gl.DepthBlurRenderer;
import net.trippedout.android.shadercamerasamples.gl.LipServiceRenderer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Depth blurrrrrr
 */
public class DepthBlurActivity extends FragmentActivity implements CameraFragment.CameraTextureListener.OnRendererCreatedListener {
    private static final String TAG_CAMERA_FRAGMENT = "camera_fragment";

    @InjectView(R.id.texture) AutoFitTextureView mAutoFitTextureView;
    @InjectView(R.id.seekbar) SeekBar mSeekBar;

    private CameraFragment mCameraFragment;
    private CameraFragment.CameraTextureListener mCameraTextureListener;

    private DepthBlurRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depth_blur);

        ButterKnife.inject(this);

        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        setupCameraFragment();
    }

    private void setupCameraFragment()
    {
        mCameraFragment = CameraFragment.newInstance();
        mCameraFragment.setTextureView(mAutoFitTextureView);
        mCameraFragment.setCameraToUse(CameraFragment.CAMERA_FORWARD);

        //pass a reference to the renderer u want to use. we will use a listener to get
        //a reference to that renderer once it is created
        mCameraTextureListener = new CameraFragment.CameraTextureListener(this, mCameraFragment, DepthBlurRenderer.class);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mCameraFragment, TAG_CAMERA_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onRendererCreated(CameraRenderer renderer)
    {
        mRenderer = (DepthBlurRenderer)renderer;

    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mRenderer.setBlurSize(progress * .1f);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @OnClick(R.id.btn_record)
    public void onRecordClicked()
    {
        mCameraFragment.toggleRecording();
    }
}
