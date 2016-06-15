package de.braeuer.matthias.photobooth;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import de.braeuer.matthias.photobooth.dialogs.ImageDialogFragment;
import de.braeuer.matthias.photobooth.dialogs.KeepEmailAddressesDialogFragment;
import de.braeuer.matthias.photobooth.listener.OnDialogFragmentClosedListener;
import de.braeuer.matthias.photobooth.listener.OnHttpRequestDoneListener;
import usbcamera.BaselineInitiator;
import usbcamera.PTPException;
import usbcamera.Session;
import usbcamera.eos.EosEvent;
import usbcamera.eos.EosInitiator;
import usbcamera.nikon.NikonInitiator;

public class CameraViewActivity extends Activity implements OnDialogFragmentClosedListener, OnHttpRequestDoneListener {

    public static final String SERVER = "http://homepages.uni-regensburg.de/~brm08652/photo_booth/upload.php";

    private static final String IMAGE_DIALOG_FRAGMENT = "ImageDialogFragment";

    public Thread thread; //From USBCameraTest.java

    private UsbManager mUsbManager; //From USBCameraTest.java
    private BaselineInitiator bi; //From USBCameraTest.java

    private ImageView liveViewHolder; //From USBCameraTest.java

    private Bitmap currentBitmap;

    private boolean liveViewTurnedOn = false; //From USBCameraTest.java

    private int liveViewFetchFailCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_camera_view_layout);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); //From USBCameraTest.java

        liveViewHolder = (ImageView) findViewById(R.id.liveViewHolder); //From USBCameraTest.java
    }

    @Override
    public void onResume() {
        super.onResume();
        openLiveView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdatingLiveView();
        detachDevice();
    }

    /*
        From USBCameraTest.java changed some stuff
     */
    // search connected devices, returns only protocol 0 devices
    public UsbDevice searchDevice() {
        UsbDevice device = null;

        for (UsbDevice lDevice : mUsbManager.getDeviceList().values()) {
            if (lDevice.getDeviceProtocol() == 0) device = lDevice;
        }
        if (device == null) {
            Toast.makeText(this, "No Device Found", Toast.LENGTH_LONG).show();
        }

        return device;
    }

    /*
        From USBCameraTest.java changed some stuff
     */
    public void initDevice(UsbDevice device) {
        if (device != null) {
            try {
                bi = new BaselineInitiator(device, mUsbManager.openDevice(device));

                if (bi.getDevice().getVendorId() == EosInitiator.CANON_VID) {
                    try {
                        bi.getClearStatus();
                        bi.close();
                    } catch (PTPException e) {
                        Toast.makeText(this, getResources().getString(R.string.replug_camera), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    bi = new EosInitiator(device, mUsbManager.openDevice(device));

                } else if (device.getVendorId() == NikonInitiator.NIKON_VID) {
                    try {
                        bi.getClearStatus();
                        bi.close();
                    } catch (PTPException e) {
                        Toast.makeText(this, getResources().getString(R.string.replug_camera), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    bi = new NikonInitiator(device, mUsbManager.openDevice(device));
                }

                bi.openSession();

            } catch (PTPException e) {
                Toast.makeText(this, getResources().getString(R.string.replug_camera), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /*
        From USBCameraTest.java
     */
    public void detachDevice() {
        if (bi != null && bi.getDevice() != null) {
            try {
                bi.close();
            } catch (PTPException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        From USBCameraTest.java changed some stuff
     */
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

    /*
        From USBCameraTest.java changed some stuff
     */
    private Bitmap getCurrentViewBitmap() {
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

    private boolean updateLiveView() {
        final Bitmap bm = getCurrentViewBitmap();

        if (bm != null) {
            if (bm.getWidth() != 1 && bm.getHeight() != 1) {
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

            return true;
        }

        return false;
    }

    private void getTakenPicture() {
        Bitmap bm = null;
        int counter = 0;

        while (bm == null) {
            bm = getCurrentViewBitmap();

            if(counter >= 420){
                bm = currentBitmap;
            }

            if (bm != null) {
                showImageFragmentDialog(bm);
            }

            if(bm != null && counter > 420){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CameraViewActivity.this, "Could not get Picture from Camera", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            counter++;
        }
    }

    private void showImageFragmentDialog(Bitmap bm) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ImageDialogFragment idf = ImageDialogFragment.newInstance(bm);

        idf.show(ft, IMAGE_DIALOG_FRAGMENT);
    }

    private void startUpdatingLiveView() {
        stopUpdatingLiveView();

        thread = new LiveViewThread();

        thread.start();
    }

    private void stopUpdatingLiveView() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void onDialogFragmentClosed() {
        startUpdatingLiveView();
    }

    @Override
    public void onHttpRequestError(Bitmap bm, String errorMsg) {
        errorMsg = errorMsg != null ? errorMsg : getResources().getString(R.string.http_request_error);

        showImageFragmentDialog(bm);

        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHttpRequestSuccess() {
        Toast.makeText(this, getResources().getString(R.string.http_request_success), Toast.LENGTH_LONG).show();

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        KeepEmailAddressesDialogFragment kdf = KeepEmailAddressesDialogFragment.newInstance();

        kdf.show(ft, KeepEmailAddressesDialogFragment.KEEP_EMAIL_ADDRESSES_DIALOG_FRAGMENT);
    }

    private class LiveViewThread extends Thread {
        @Override
        public void run() {
            if (!liveViewTurnedOn) {
                try {
                    Thread.sleep(1500);
                    liveViewTurnedOn = updateLiveView();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (liveViewTurnedOn) {
                liveViewTurnedOn = updateLiveView();
            }

            getTakenPicture();
        }
    }

    private void restartApplication() {
        Intent mStartActivity = new Intent(this, CameraViewActivity.class);
        int mPendingIntentId = 0b10000001; // random id

        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}