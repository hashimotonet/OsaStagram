package yokohama.osm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.android.camera2basic.R;

public class SpreadImageActivity extends Activity {
    public SpreadImageActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spread_image);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Uri uri = getImageURI(url);

        @SuppressLint("ResourceType") ImageView ivImage = findViewById(R.id.spreadImageView);
        ivImage.setImageURI(uri);
    }

    private Uri getImageURI(String url) {
        Uri uri = Uri.parse(url);
        return uri;
    }
}
