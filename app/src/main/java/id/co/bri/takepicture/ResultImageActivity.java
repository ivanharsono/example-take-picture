package id.co.bri.takepicture;

import android.graphics.Bitmap;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class ResultImageActivity extends AppCompatActivity {

    private final int MAX_IMAGE_SIZE = 1000;

    private String imageFilePath;
    private ImageView ivPreview;
    private TextView tvImageData;
    private TextView tvBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_image);

        ivPreview = findViewById(R.id.image_preview);
        tvImageData = findViewById(R.id.text_image_data);
        tvBase64 = findViewById(R.id.text_base64);
        imageFilePath = getIntent().getStringExtra("imageFilePath");

        if(imageFilePath != null && !imageFilePath.isEmpty()) {
            Glide.with(this)
                    .asBitmap()
                    .load(imageFilePath)
                    .apply(new RequestOptions().override(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            processBitmap(resource);
                            return true;
                        }
                    }).submit();
        }
        else {
            Toast.makeText(this, "No image data", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void processBitmap(Bitmap bitmap) {
        String imageData = "";

        ivPreview.setImageBitmap(bitmap);
        imageData += String.format("Image Size : (%sx%spx)\n",bitmap.getWidth(),bitmap.getHeight());
        imageData += String.format("Image Path : %s", imageFilePath);

        tvImageData.setText(imageData);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

//        tvBase64.setText(encoded);
        Log.d("Base64", encoded);



    }
}
