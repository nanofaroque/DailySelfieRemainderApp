package finalproject.part2.coursera.com.dailyselfieremainderapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    ListView photoList;
    ArrayList<String> listOfImages;
    private static final long TWO_MINUTES = 40 * 1000L;
    private AlarmManager mAlarmManager;
    private PendingIntent mSelfiePendingIntent;
    private Intent mSelfieNotificationIntent;

    private static final int CAMERA_CAPTURE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoList=(ListView)findViewById(R.id.photoList);
        DisplayCapturedImagesFromCamera();

    }

    private void DisplayCapturedImagesFromCamera() {
        // TODO Auto-generated method stub
        File myPath = getExternalFilesDir(null);
        listOfImages = new ArrayList<String>();

        try
        {

            for(File f: myPath.listFiles()) {
                listOfImages.add(f.getAbsolutePath());
            }

            PhotoAdapter adapter = new PhotoAdapter(MainActivity.this,listOfImages);
            photoList.setAdapter(adapter);
        }
        catch(Exception ex)
        {
            Log.w("Error", ex.getMessage());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            startCameraCapture();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCameraCapture() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = CreateImageFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(photoFile != null)
            {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CAMERA_CAPTURE);
            }
        }
    }

    private File CreateImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Image_" + timeStamp + "_";

        File storageDirectory = getExternalFilesDir("");
        File image = File.createTempFile(imageFileName, ".jpg",storageDirectory);
        return image;

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

        switch(requestCode)
        {
            case CAMERA_CAPTURE:
                if(resultCode == RESULT_OK)
                {
                    DisplayCapturedImagesFromCamera();
                }
                break;
        }

        photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap = BitmapFactory.decodeFile(listOfImages.get(position));
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog);
                ImageView imageView=(ImageView)dialog.findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                dialog.show();

            }
        });


        createPendingIntents();

        createSelfieReminders();

    }

    private void createSelfieReminders() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Broadcast the notification intent at specified intervals
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP
                , System.currentTimeMillis() + TWO_MINUTES
                , TWO_MINUTES
                , mSelfiePendingIntent);

    }

    private void createPendingIntents() {
        // Create the notification pending intent
        mSelfieNotificationIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        mSelfiePendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mSelfieNotificationIntent, 0);
    }

}
