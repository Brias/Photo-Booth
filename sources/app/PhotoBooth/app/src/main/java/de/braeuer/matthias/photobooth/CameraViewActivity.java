package de.braeuer.matthias.photobooth;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import de.braeuer.matthias.photobooth.dialogs.ImageDialogFragment;
import usbcamera.BaselineInitiator;
import usbcamera.PTPException;
import usbcamera.Session;
import usbcamera.eos.EosInitiator;
import usbcamera.nikon.NikonInitiator;

public class CameraViewActivity extends Activity implements DialogInterface.OnDismissListener {

    private static final String TAG = "CAMERA_VIEW_ACTIVITY";
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

            if(liveViewFetchFailCounter == 5){
                liveViewFetchFailCounter = 0;
                startUpdatingLiveView();
            }

            return true;
        }

        return false;
    }

    private void getTakenPicture() {
        if (currentBitmap != null) {
            showImageFragmentDialog(currentBitmap);
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CameraViewActivity.this, "BITMAP IS NULL", Toast.LENGTH_SHORT).show();
                }
            });
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
    public void onDismiss(DialogInterface dialog) {
        startUpdatingLiveView();
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

            //try {
                //Thread.sleep(1000);
                getTakenPicture();
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
        }
    }
}