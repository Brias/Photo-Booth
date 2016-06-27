package de.braeuer.matthias.photobooth;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Matze on 21.06.2016.
 */
public class UploadLocalImagesService extends Service {

    private UploadLocalImagesThread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        thread = new UploadLocalImagesThread(getApplicationContext(), CameraViewActivity.SERVER);
    }

    @Override
    public synchronized void onDestroy() {
        thread.interrupt();
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (thread.getState() == Thread.State.NEW)
        {
            thread.start();
        }

        return START_STICKY;
    }

    class UploadLocalImagesThread extends Thread {

        private DBHelper dbHelper;
        private Context context;
        private String serverUrl;

        public UploadLocalImagesThread(Context context, String serverUrl) {
            this.context = context;
            this.serverUrl = serverUrl;
            dbHelper = new DBHelper(context);
        }

        @Override
        public void run() {
            try{
                Thread.sleep(10000);

                ArrayList<Image> images = dbHelper.getAllImages();

                for(Iterator<Image> iterator = images.iterator(); iterator.hasNext();){
                    Image image = iterator.next();

                    Bitmap bm = AccessStorage.getImageFromInternalStorage(context, image.getName());

                    HashMap<String, String> detail = new HashMap<>();

                    detail.put("image", image.toBase64(bm));
                    detail.put("email", image.getEmail());

                    try {
                        String dataToSend = UrlUtil.hashMapToUrl(detail);

                        final String request = Request.post(serverUrl, dataToSend);

                        if (request != null && request.equals("200")) {
                            if (dbHelper.deleteImage(image.getName())) {
                                AccessStorage.deleteImageFromInternalStorage(context, image.getName());
                                iterator.remove();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
