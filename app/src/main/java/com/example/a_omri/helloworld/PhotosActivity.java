package com.example.a_omri.helloworld;

/**
 * Created by mdamri on 18.12.2015.
 */

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.content.res.TypedArray;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Matrix;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.util.Base64;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.WebView;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.Gallery;
        import android.widget.ImageView;
        import android.widget.Toast;

        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.util.EntityUtils;

        import java.io.ByteArrayOutputStream;
        import java.util.ArrayList;

public class PhotosActivity extends Activity {

    Button btpic, btnup;
    private Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;

    public static String URL = "http://bites.factorycampus.net/uploadimages.php";
    private String groupe_id;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);
        groupe_id=this.getIntent().getStringExtra("GroupId");
        btpic = (Button) findViewById(R.id.cpic);
        btpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickpic();
            }
        });

        btnup = (Button) findViewById(R.id.up);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://bites.factorycampus.net/loadImages.php?groupe_id=" + groupe_id);
    }

    private void upload() {
        // Image location URL
        Log.e("path", "?" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();




        float aspectRatio = bm.getWidth() /
                (float) bm.getHeight();
        int width = 480;
        int height = Math.round(width / aspectRatio);

        photo = Bitmap.createScaledBitmap(
                bm, width, height, false);



        final int w = photo.getWidth();
        final int h = photo.getHeight();
        if(w>h) {

            Bitmap portraitBitmap = Bitmap.createBitmap(h, w, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(portraitBitmap);
            c.rotate(90, 0, 0);
            c.drawBitmap(photo, 0, -h, null);
            photo = portraitBitmap;
        }

        photo.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba,0);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadToServer().execute();

    }
    private void clickpic() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, 100);

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }
    public void Actualiserphotos(View view){
        myWebView.reload();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {

            selectedImage = data.getData();
            photo = (Bitmap) data.getExtras().get("data");

            // Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if(photo != null)
                upload();
            //ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
            //imageView.setImageBitmap(photo);
        }
    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(PhotosActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            nameValuePairs.add(new BasicNameValuePair("groupid", groupe_id));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            myWebView.reload();
        }
    }
}
