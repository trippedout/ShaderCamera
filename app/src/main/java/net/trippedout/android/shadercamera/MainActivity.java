package net.trippedout.android.shadercamera;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.TextureView;

import net.trippedout.android.shadercamera.fragments.CameraFragment;
import net.trippedout.android.shadercamera.gl.SimpleCameraRenderer;
import net.trippedout.android.shadercamera.view.AutoFitTextureView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends FragmentActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_CAMERA_FRAGMENT = "camera_fragment";

    @InjectView(R.id.texture) AutoFitTextureView mAutoFitTextureView;

    private CameraFragment mCameraFragment;
    private SimpleCameraRenderer mRenderer;
    private boolean mToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setupCameraFragment();
    }

    private void setupCameraFragment()
    {
        mCameraFragment = CameraFragment.newInstance();
        mCameraFragment.setSurfaceTextureListener(mSurfaceTextureListener);
        mCameraFragment.setTextureView(mAutoFitTextureView);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mCameraFragment, TAG_CAMERA_FRAGMENT);
        transaction.commit();
    }

    @OnClick(R.id.btn_toggle_shader)
    public void onClickToggleShader()
    {
        if(mToggle)
            mRenderer.animateCircleClosed();
        else
            mRenderer.animateCircleOpen();

        mToggle = !mToggle;
    }

    @OnClick(R.id.btn_record)
    public void onRecordClicked()
    {
        mCameraFragment.toggleRecording();
    }
;
    /**
     * {@link android.view.TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link android.view.TextureView}.
     */
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height)
        {
            Log.d(TAG, "onSurfaceTextureAvailable() " + width + ", " + height);

            mRenderer = new SimpleCameraRenderer(MainActivity.this, surfaceTexture, width, height);
            mCameraFragment.setRenderer(mRenderer);
            mCameraFragment.configureTransform(width, height);
            mCameraFragment.openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height)
        {
            Log.d(TAG, "onSurfaceTextureSizeChanged() " + width + ", " + height);

            mCameraFragment.configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture)
        {
            Log.d(TAG, "onDestroy()");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture)
        {

        }

    };

}
