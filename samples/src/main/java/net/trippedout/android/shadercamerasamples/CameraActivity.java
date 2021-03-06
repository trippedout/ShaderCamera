package net.trippedout.android.shadercamerasamples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import net.trippedout.android.shadercamera.fragments.CameraFragment;
import net.trippedout.android.shadercamera.gl.CameraRenderer;
import net.trippedout.android.shadercamera.view.AutoFitTextureView;
import net.trippedout.android.shadercamerasamples.gl.CameraBasicRenderer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Doesn't really do much except display the camera
 */
public class CameraActivity extends FragmentActivity implements CameraFragment.CameraTextureListener.OnRendererCreatedListener {
    private static final String TAG_CAMERA_FRAGMENT = "camera_fragment";

    @InjectView(R.id.texture)
    AutoFitTextureView mAutoFitTextureView;

    private CameraFragment mCameraFragment;
    private CameraFragment.CameraTextureListener mCameraTextureListener;

    private CameraBasicRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lip_service);

        ButterKnife.inject(this);

        setupCameraFragment();
    }

    private void setupCameraFragment()
    {
        mCameraFragment = CameraFragment.newInstance();
        mCameraFragment.setTextureView(mAutoFitTextureView);
        mCameraFragment.setCameraToUse(CameraFragment.CAMERA_FORWARD);

        //pass a reference to the renderer u want to use. we will use the observer pattern to get
        //a reference to that renderer once it is created
        mCameraTextureListener = new CameraFragment.CameraTextureListener(this, mCameraFragment, CameraBasicRenderer.class);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mCameraFragment, TAG_CAMERA_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onRendererCreated(CameraRenderer renderer) {
        mRenderer = (CameraBasicRenderer) renderer;
    }

    @OnClick(R.id.btn_record)
    public void onRecordClicked()
    {
        mCameraFragment.toggleRecording();
    }
}
