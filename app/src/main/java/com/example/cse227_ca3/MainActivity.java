package com.example.cse227_ca3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    static ImageView imageView;
    String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        
        image_url = "https://i.imgur.com/NOuamqv.jpg";

        findViewById(R.id.download_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTask myTask = new MyAsyncTask();
                myTask.execute(image_url);
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                MainActivity.imageView.setImageBitmap(bitmap);
                MainActivity.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        private Bitmap downloadImage(String path) {
            Bitmap bitmap = null;
            try{
                URL url = new URL(path);

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setReadTimeout(5000);
                httpsURLConnection.setConnectTimeout(5000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();

                int code = httpsURLConnection.getResponseCode();
                if(code == HttpURLConnection.HTTP_OK){
                    InputStream stream = httpsURLConnection.getInputStream();
                    if(stream != null){
                        bitmap = BitmapFactory.decodeStream(stream);
                    }
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}
