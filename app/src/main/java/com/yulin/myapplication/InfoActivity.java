package com.yulin.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageView thumbImageView = findViewById(R.id.drama_thumb);
        TextView name = findViewById(R.id.drama_name);
        TextView rating = findViewById(R.id.drama_rating_value);
        TextView createdAt = findViewById(R.id.drama_createdAt_value);
        TextView totalViews = findViewById(R.id.drama_totalViews_value);

        DramaBean dramaBean = getIntent().getParcelableExtra("Drama");
        name.setText(dramaBean.getName());
        rating.setText(String.format("%s", dramaBean.getRating()));
        createdAt.setText(Utils.parserDateFormat(dramaBean.getCreated_at()));
        totalViews.setText(String.format("%s", dramaBean.getTotal_views()));

        Picasso.with(this)
                .load(dramaBean.getThumb())
                .fit()
                .centerInside()
                .into(thumbImageView);
    }
}
