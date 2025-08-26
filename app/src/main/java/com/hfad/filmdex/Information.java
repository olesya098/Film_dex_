package com.hfad.filmdex;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Information extends AppCompatActivity {
    ArrayList<FilmInformation> filmInformations = new ArrayList<>();
    private String film_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            ImageView imageView = findViewById(R.id.imageViewStrelka);
            imageView.setOnClickListener(v -> finish());

            // Выполнение GetFilmTask с обработкой исключений
            try {
                int filmId = getIntent().getIntExtra("film_id", 0);
                new GetFilmTask().execute("https://film-dex.vercel.app/api/v1/films/" + filmId);
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка при загрузке фильма: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
        private class GetFilmTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream responseStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                TextView text = findViewById(R.id.Inf_film_Infor);
                text.setText(result);
                JSONObject jsonObject = new JSONObject(result);

                TextView titleText = findViewById(R.id.Inf_film_title);
                titleText.setText(jsonObject.getString("title"));


                TextView year = findViewById(R.id.Inf_film_year);
                year.setText(String.format(jsonObject.getString("year")));

                TextView age = findViewById(R.id.Inf_film_age);
                age.setText(String.format(jsonObject.getString("age")));

                TextView reiting = findViewById(R.id.Inf_Reiting_film);
                reiting.setText(jsonObject.getString("rate"));

                TextView country = findViewById(R.id.Inf_film_country);
                country.setText(jsonObject.getString("country"));

                TextView category = findViewById(R.id.Inf_film_category);
                category.setText(jsonObject.getString("category_film"));

                TextView descriptionText = findViewById(R.id.Inf_film_Infor);
                descriptionText.setText(jsonObject.getString("description"));

                TextView link = findViewById(R.id.Inf_film_link);
                String url = jsonObject.getString("link");

// Устанавливаем текст ссылки
                link.setText(url);
                link.setLinkTextColor(Color.BLUE); // Устанавливаем цвет текста для ссылки

// Делаем текст кликабельным
                link.setMovementMethod(LinkMovementMethod.getInstance());

// Устанавливаем обработчик клика
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Создаём Intent для открытия ссылки
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });


                ImageView imageView2 = findViewById(R.id.image_Inf);
                try {
                    Picasso.get()
                            .load(jsonObject.getString("image"))
                            .into(imageView2);
                } catch (Exception e) {
                }
            } catch (JSONException ex) {}
        }
    }

}