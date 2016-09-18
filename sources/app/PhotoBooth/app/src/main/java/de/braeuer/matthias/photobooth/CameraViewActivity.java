package de.braeuer.matthias.photobooth;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import de.braeuer.matthias.photobooth.dialogs.ErrorDialogFragment;
import de.braeuer.matthias.photobooth.dialogs.ImageDialogFragment;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;
import usbcamera.BaselineInitiator;
import usbcamera.PTPException;
import usbcamera.Session;
import usbcamera.eos.EosInitiator;


public class CameraViewActivity extends Activity implements OnDialogFragmentClosedListener {

    private class LiveViewThread extends Thread {
        @Override
        public void run() {
            liveViewTurnedOn = true;

            try {
                Thread.sleep(1500);
                while (liveViewTurnedOn) {
                    if (!pictureTaken) {
                        try {
                            updateLiveView();
                        } catch (PictureTakenException e) {
                            pictureTaken = true;
                            getTakenPicture();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static final String SERVER = "http://urwalking.ur.de/photobooth/upload.php";
    private static final String IMAGE_DIALOG_FRAGMENT = "ImageDialogFragment";
    public Thread thread = null;
    private UsbManager mUsbManager;
    private BaselineInitiator bi;
    private ImageView liveViewHolder;
    private boolean liveViewTurnedOn = false;
    private Bitmap currentBitmap;
    private int liveViewFetchFailCounter = 0;
    private boolean pictureTaken = false;

    @Override
    public void onResume() {
        super.onResume();

        openLiveView();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopUpdatingLiveView();
        detachDevice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EmailAddressManager.reset();
        stopUploadLocalImageService();
    }

    // search connected devices, returns only protocol 0 devices
    public UsbDevice searchDevice() {
        UsbDevice device = null;

        for (UsbDevice lDevice : mUsbManager.getDeviceList().values()) {
            if (lDevice.getDeviceProtocol() == 0) device = lDevice;
        }
        if (device == null) {
            showErrorDialog(getResources().getString(R.string.no_device_found_title), getResources().getString(R
                    .string.no_device_found), false);
        }

        return device;
    }

    public void initDevice(UsbDevice device) {
        if (device != null) {
            try {
                bi = new BaselineInitiator(device, mUsbManager.openDevice(device));

                if (bi.getDevice().getVendorId() == EosInitiator.CANON_VID) {
                    try {
                        bi.getClearStatus();
                        bi.close();
                    } catch (PTPException e) {
                        showErrorDialog(getResources().getString(R.string.replug_camera_title), getResources()
                                .getString(R.string.replug_camera), false);
                        e.printStackTrace();
                    }

                    UsbDeviceConnection connection = mUsbManager.openDevice(device);

                    bi = new EosInitiator(device, connection);
                }

                bi.openSession();

            } catch (PTPException e) {
                showErrorDialog(getResources().getString(R.string.replug_camera_title), getResources().getString(R
                        .string.replug_camera), false);
                e.printStackTrace();
            }
        }
    }

    public void detachDevice() {
        if (bi != null && bi.getDevice() != null) {
            try {
                bi.close();
            } catch (PTPException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean initLiveView(Session session) {
        if (session == null) {
            return false;
        }

        boolean result = session.isActive();

        if (bi.getDevice() == null) {
            return false;
        }

        if (!bi.isSessionActive()) {
            try {
                bi.openSession();
            } catch (PTPException e) {
                e.printStackTrace();
                result = false;
            }
        }

        if (bi.isSessionActive()) {
            try {
                bi.setupLiveview();
                result = true;
            } catch (PTPException e) {
                e.printStackTrace();
                result = false;
            }
        }

        return result;
    }

    @Override
    public void onDialogFragmentClosed() {
        if (BitmapHolder.bm != null) {
            BitmapHolder.bm.recycle();
        }

        pictureTaken = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullscreen();

        setContentView(R.layout.activity_camera_view_layout);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); //From USBCameraTest.java

        liveViewHolder = (ImageView) findViewById(R.id.liveViewHolder); //From USBCameraTest.java

        startUploadLocalImageService();
    }

    private void setFullscreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void startUploadLocalImageService() {
        if (Connection.isWifiConnection(getApplicationContext())) {
            Intent intent = new Intent(this, UploadLocalImagesService.class);
            startService(intent);
        }
    }

    private void stopUploadLocalImageService() {
        Intent intent = new Intent(this, UploadLocalImagesService.class);
        stopService(intent);
    }

    private void showErrorDialog(String title, String errorMsg, boolean callOnClosedListener) {
        ErrorDialogFragment edf = ErrorDialogFragment.newInstance(title, errorMsg, callOnClosedListener);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        edf.show(ft, ErrorDialogFragment.ERROR_DIALOG_FRAGMENT);
    }

    private void openLiveView() {
        if (bi == null || bi.getInfo() == null) {
            initDevice(searchDevice());
        }

        if (bi != null && bi.getInfo() != null) {
            Session session = bi.getSession();

            boolean isLiveViewInitialized = initLiveView(session);

            if (bi != null && isLiveViewInitialized) {
                startUpdatingLiveView();
            }
        }
    }

    private Bitmap getCurrentViewBitmap() throws PictureTakenException {
        if (bi.getDevice() == null) {
            return null;
        }

        if (!bi.isSessionActive())
            try {
                bi.openSession();
            } catch (PTPException e) {
                e.printStackTrace();
                return null;
            }

        if (bi.isSessionActive()) {
            return bi.getLiveView();
        }

        return null;
    }

    private void updateLiveView() throws PictureTakenException {
        final Bitmap bm = getCurrentViewBitmap();

        if (bm != null) {
            liveViewFetchFailCounter = 0;

            liveViewHolder.post(new Runnable() {
                @Override
                public void run() {
                    currentBitmap = bm;
                    liveViewHolder.setImageBitmap(bm);
                    liveViewHolder.invalidate();
                }
            });
        } else {
            liveViewFetchFailCounter++;
        }

        if (liveViewFetchFailCounter >= 5) {
            restartApplication();
        }
    }

    private void getTakenPicture() {
        try {
            Thread.sleep(1000);

            Bitmap bm = currentBitmap;

            if (bm != null) {
                BitmapHolder.bm = bm;
                showImageFragmentDialog(bm);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorDialog(getResources().getString(R.string.get_picture_error_title), getResources()
                                .getString(R.string.get_picture_error), true);
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showImageFragmentDialog(Bitmap thumb) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ImageDialogFragment idf = ImageDialogFragment.newInstance(thumb);

        idf.show(ft, IMAGE_DIALOG_FRAGMENT);
    }

    private void startUpdatingLiveView() {
        stopUpdatingLiveView();

        if (thread == null) {
            thread = new LiveViewThread();

            if (thread.getState() == Thread.State.NEW) {
                thread.start();
            }
        }
    }

    private void stopUpdatingLiveView() {
        liveViewTurnedOn = false;

        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    private void restartApplication() {
        Intent mStartActivity = new Intent(this, CameraViewActivity.class);
        int mPendingIntentId = 0b10000001; // random id

        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}