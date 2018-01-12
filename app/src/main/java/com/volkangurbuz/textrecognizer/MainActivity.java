package com.volkangurbuz.textrecognizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    TextView text;
    private String selectedImagePath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        text = findViewById(R.id.recogWords);


        findViewById(R.id.textRecogButton)
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, SELECT_PICTURE);
                        }

                    }
                });

    }

    Uri fullPhotoUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
             fullPhotoUri = data.getData();

            // Do work with photo saved at fullPhotoUri
            try {
                textRecognition();
                imageView.setImageURI(fullPhotoUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void textRecognition() throws FileNotFoundException {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational())
            Log.e("ERROR", "Detector dependencies are not yet available");
        else {
            Scanner scanner = new Scanner();

            Bitmap bitmap = scanner.decodeBitmapUri(MainActivity.this, fullPhotoUri);

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                System.out.println(stringBuilder.append(item.getValue()));
                stringBuilder.append("\n");
            }
            text.setText(stringBuilder.toString());

        }
    }

}
