package com.hfad.filmdex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Poisk extends AppCompatActivity {
    ArrayList<FilmInformation> film_informations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_poisk);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            ImageView imageViewGlavna = findViewById(R.id.Glavna_image);
            imageViewGlavna.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(Poisk.this, "Главная", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Poisk.this, MainActivity5.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        // Вывод сообщения об ошибке, если произошла ошибка
                        Toast.makeText(Poisk.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ImageView imageViewPoisk = findViewById(R.id.Poisk_image);
            imageViewPoisk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(Poisk.this, "Поиск", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // Обработка исключения
                        Toast.makeText(Poisk.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            EditText findText1 = findViewById(R.id.Poisk_films);
            findText1.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        try {
                            String searchText = findText1.getText().toString();

                            findByTitle(searchText);
                        } catch (Exception e) {
                            // Обработка исключения
                            Toast.makeText(v.getContext(), "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                }
            });
            new GetFilmTask().execute("https://film-dex.vercel.app/api/v1/films");
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
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                LinearLayout film = findViewById(R.id.film);

                // Проверка на пустой результат
                if (jsonArray.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Не найдено данных в базе данных",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int film_id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String category = jsonObject.getString("category_film");
                    String film_year = jsonObject.getString("year");
                    String film_age = jsonObject.getString("age");
                    String film_image = jsonObject.getString("image");
                    String rate = jsonObject.getString("rate");
                    String description = jsonObject.getString("description");
                    String link = jsonObject.getString("link");

                    FilmInformation information = new FilmInformation(
                            film_id,
                            title,
                            category,
                            film_year,
                            film_age,
                            film_image,
                            rate,
                            description,
                            link
                    );
                    film_informations.add(information);
                }
            } catch (JSONException e) {
                TextView text = findViewById(R.id.Poisk_films);
                text.setText(e.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Ошибка при обработке данных: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                TextView text = findViewById(R.id.Poisk_films);
                text.setText(e.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Произошла ошибка: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void findByTitle(String text) {
        try {
            LinearLayout filmLayout = findViewById(R.id.find_films);
            filmLayout.removeAllViews();

            boolean found = false;
            for (FilmInformation films : film_informations) {
                if (films.title.toLowerCase().contains(text.toLowerCase())) {
                    found = true;
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View card = inflater.inflate(R.layout.film_card, null);

                    TextView cardTitle = card.findViewById(R.id.film_title);
                    cardTitle.setText(films.title);

                    TextView year_film = card.findViewById(R.id.film_year);
                    year_film.setText(String.format(films.film_year));

                    TextView reiting_film = card.findViewById(R.id.film_reiting);
                    reiting_film.setText(String.format(films.rate));

                    TextView age_Text = card.findViewById(R.id.film_age);
                    age_Text.setText(String.format(films.film_age));

                    ImageView imageView = card.findViewById(R.id.film_image);
                    try {
                        Picasso.get()
                                .load(films.film_image)
                                .into(imageView);
                    } catch (Exception e) {
                        // Обработка ошибки загрузки изображения
                        Toast.makeText(getApplicationContext(),
                                "Ошибка загрузки изображения",
                                Toast.LENGTH_SHORT).show();
                    }
                    card.setOnClickListener(view -> {
                        Intent intent = new Intent(view.getContext(), Information.class);
                        intent.putExtra("film_id", films.film_id);
                        view.getContext().startActivity(intent);
                    });

                    filmLayout.addView(card);
                }
            }

            // Проверка, были ли найдены фильмы
            if (!found) {
                Toast.makeText(getApplicationContext(),
                        "Фильмы по запросу \"" + text + "\" не найдены",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            EditText text1 = findViewById(R.id.Poisk_films);
            text1.setText(e.getMessage());
            Toast.makeText(getApplicationContext(),
                    "Произошла ошибка при поиске: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}