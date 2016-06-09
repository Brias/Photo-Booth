package de.braeuer.matthias.photo_booth;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import usbcamera.BaselineInitiator;
import usbcamera.PTPException;
import usbcamera.Session;
import usbcamera.eos.EosInitiator;
import usbcamera.nikon.NikonInitiator;

public class CameraViewActivity extends Activity {

    private static final String TAG = "CAMERA_VIEW_ACTIVITY";

    public Thread thread; //From USBCameraTest.java

    private UsbManager mUsbManager; //From USBCameraTest.java
    private BaselineInitiator bi; //From USBCameraTest.java

    private ImageView liveViewHolder; //From USBCameraTest.java

    private boolean isCanon = false; //From USBCameraTest.java
    private boolean isNikon = false; //From USBCameraTest.java

    /*
       From USBCameraTest.java change some stuff
    */
    // receive Broadcasts
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "mUsbReceiver  onReceive");
            String action = intent.getAction();
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action) /*|| (device != null)*/) {
                initDevice(device);
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (bi != null && bi.getSession() != null) {
                    bi.getSession().close();
                }
                detachDevice();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); //From USBCameraTest.java

        liveViewHolder = (ImageView) findViewById(R.id.liveViewHolder); //From USBCameraTest.java

        IntentFilter filter = new IntentFilter(); //From USBCameraTest.java
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED); //From USBCameraTest.java
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED); //From USBCameraTest.java
        registerReceiver(mUsbReceiver, filter); //From USBCameraTest.java
    }

    @Override
    public void onResume() {
        super.onResume();

        openLiveView();
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera_view_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.initLiveView:
                openLiveView();
                break;
        }

        return true;
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
            Toast.makeText(this, "DEVICE IS THERE", Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(this, "in First TRY: ", Toast.LENGTH_SHORT).show();

                bi = new BaselineInitiator(device, mUsbManager.openDevice(device));

                Toast.makeText(this, "new BAselineInitiator: " + bi.getInfo(), Toast.LENGTH_SHORT).show();

                if (bi.getDevice().getVendorId() == EosInitiator.CANON_VID) {
                    Toast.makeText(this, "EOS Device", Toast.LENGTH_SHORT).show();
                    try {
                        bi.getClearStatus();
                        bi.close();
                        Toast.makeText(this, "CLosed baseInitiator", Toast.LENGTH_SHORT).show();
                    } catch (PTPException e) {
                        e.printStackTrace();
                    }
                    isCanon = true;
                    isNikon = false;
                    bi = new EosInitiator(device, mUsbManager.openDevice(device));

                    Toast.makeText(this, "new EOSInitiator: " + bi.getInfo(), Toast.LENGTH_SHORT).show();

                } else if (device.getVendorId() == NikonInitiator.NIKON_VID) {
                    try {
                        bi.getClearStatus();
                        bi.close();
                    } catch (PTPException e) {
                        e.printStackTrace();
                    }
                    isCanon = false;
                    isNikon = true;
                    bi = new NikonInitiator(device, mUsbManager.openDevice(device));
                }

                bi.openSession();

                Toast.makeText(this, "Session should be opened: " + bi.getSession().isActive(), Toast.LENGTH_SHORT).show();

            } catch (PTPException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "Outer Catch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "DEVICE IS NOTTTTTT THERE", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        From USBCameraTest.java
     */
    public void detachDevice() {
        if (bi != null) {
            if (bi.getDevice() != null)
                try {
                    bi.close();
                } catch (PTPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    /*
        From USBCameraTest.java changed some stuff
     */
    public boolean initLiveView(Session session) {
        if(session == null){
            Toast.makeText(this, "Init Live View Session is null", Toast.LENGTH_LONG).show();
            return false;
        }

        boolean result = session.isActive();

        Toast.makeText(this, "Session active: " + result, Toast.LENGTH_LONG).show();

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

    /*
        From USBCameraTest.java changed some stuff
     */
    public boolean startLiveView(Session session) {
        boolean result = session.isActive();

        if (bi.getDevice() == null) {
            Toast.makeText(this, "bi.device == null", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!bi.isSessionActive())
            try {
                bi.openSession();
                Toast.makeText(this, "Session not active but could be opened", Toast.LENGTH_LONG).show();
            } catch (PTPException e) {
                e.printStackTrace();
                Toast.makeText(this, "Session not active and could not be opened", Toast.LENGTH_LONG).show();
                result = false;
            }
        if (bi.isSessionActive()) {
            bi.getLiveView(liveViewHolder);
            Toast.makeText(this, "Session is active", Toast.LENGTH_LONG).show();
            result = true;
        }

        Toast.makeText(this, "return: " + result, Toast.LENGTH_LONG).show();
        return result;
    }

    private void openLiveView() {
        if (bi == null || bi.getInfo() == null) {
            initDevice(searchDevice());
        }

        if(bi != null && bi.getInfo() != null) {
            Toast.makeText(this, "BaseInitiaitor not null", Toast.LENGTH_LONG).show();

            Session session = bi.getSession();

            boolean isLiveViewInitialized = initLiveView(session);

            Toast.makeText(this, "LiveView Init: " + isLiveViewInitialized, Toast.LENGTH_LONG).show();

            if (bi != null && isLiveViewInitialized) {
                startLiveView(bi.getSession());
            } else {
                Toast.makeText(this, "Could not start LiveView", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void test() {
        UsbDevice device = null;

        for (UsbDevice lDevice : mUsbManager.getDeviceList().values()) {
            if (lDevice.getDeviceProtocol() == 0) device = lDevice;
        }

        if (device == null) {
            Toast.makeText(this, "No Device Found", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "DEVICE IS THERE", Toast.LENGTH_SHORT).show();

        try {
            bi = new BaselineInitiator(device, mUsbManager.openDevice(device));

            if (bi.getDevice().getVendorId() == EosInitiator.CANON_VID) {
                try {
                    bi.getClearStatus();
                    bi.close();
                } catch (PTPException e) {
                    e.printStackTrace();
                }
                isCanon = true;
                isNikon = false;
                bi = new EosInitiator(device, mUsbManager.openDevice(device));
            } else if (device.getVendorId() == NikonInitiator.NIKON_VID) {
                try {
                    bi.getClearStatus();
                    bi.close();
                } catch (PTPException e) {
                    e.printStackTrace();
                }
                isCanon = false;
                isNikon = true;
                bi = new NikonInitiator(device, mUsbManager.openDevice(device));
            }

            bi.openSession();

            Toast.makeText(this, "Session should be opened: " + bi.getSession().isActive(), Toast.LENGTH_SHORT).show();

        } catch (PTPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        boolean result = bi.getSession().isActive();

        if (bi.getDevice() == null) {
            Toast.makeText(this, "Device is null", Toast.LENGTH_SHORT).show();
            return;
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

        if (bi == null || bi.getInfo() == null) {
            initDevice(searchDevice());
        }

        boolean isLiveViewInitialized = initLiveView(bi.getSession());

        Toast.makeText(this, "LiveView Init: " + isLiveViewInitialized, Toast.LENGTH_LONG).show();

        if (bi != null && isLiveViewInitialized) {
            startLiveView(bi.getSession());
        } else {
            Toast.makeText(this, "Could not start LiveView", Toast.LENGTH_LONG).show();
        }
    }
}
