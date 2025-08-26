package com.hfad.filmdex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity5 extends AppCompatActivity {
    ArrayList<FilmInformation> film_informations = new ArrayList<>();
    private GetFilmWithGenreTypeTask currentTask;
    FilmService filmService = new FilmService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);

        TextView filmsTextView = findViewById(R.id.Films);
        filmsTextView.setOnClickListener(v -> {
            try {
                filterFilmsByCategory("Фильм");
            } catch (Exception e) {
                // Обработка исключения
                Toast.makeText(MainActivity5.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        TextView seriesTextView = findViewById(R.id.Serials);
        seriesTextView.setOnClickListener(v -> {
            try {
            filterFilmsByCategory("Сериал");
            } catch (Exception e) {
                // Обработка исключения
                Toast.makeText(MainActivity5.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        TextView kidsTextView = findViewById(R.id.Kids);
        kidsTextView.setOnClickListener(v -> {
            try {
            filterFilmsByCategory("Детям");
        } catch (Exception e) {
            // Обработка исключения
            Toast.makeText(MainActivity5.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });

        ImageView imageViewVse = findViewById(R.id.ochi);
        imageViewVse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    filmService.fetchFilms();
                    Toast.makeText(MainActivity5.this, "Кинематограф", Toast.LENGTH_SHORT).show();
                    new GetFilmTask().execute("https://film-dex.vercel.app/api/v1/films");
                } catch (Exception e) {
                    // Вывод сообщения об ошибке, если произошла ошибка
                    Toast.makeText(MainActivity5.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView imageViewPoisk = findViewById(R.id.Poisk_image);
        imageViewPoisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Toast.makeText(MainActivity5.this, "Поиск", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity5.this, Poisk.class);
                startActivity(intent);
                } catch (Exception e) {
                    // Вывод сообщения об ошибке, если произошла ошибка
                    Toast.makeText(MainActivity5.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView imageViewGlavna = findViewById(R.id.Glavna_image);
        imageViewGlavna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Здесь выполняем действие, которое может вызвать исключение
                    Toast.makeText(MainActivity5.this, "Главная", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Обработка исключения
                    Toast.makeText(MainActivity5.this, "Обнаружена ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        new GetGenreTask().execute("https://film-dex.vercel.app/api/v1/genres");
        new GetFilmTask().execute("https://film-dex.vercel.app/api/v1/films");
    }


    private class GetFilmTask extends AsyncTask<String, Void, String> {//класс для получения всех фильмов


        @Override
        protected String doInBackground(String... urls) {
            try {
                // Преобразуем первый URL из массива urls в объект URL
                URL url = new URL(urls[0]);

                // Открываем соединение по указанному URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"); // Устанавливаем метод запроса GET

                // Получаем поток ответа от сервера
                InputStream responseStream = connection.getInputStream();
                // Создаем BufferedReader для чтения данных из потока
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

                StringBuilder result = new StringBuilder(); // Объект для накопления строки результата
                String line;
                // Читаем строки из ответа
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString(); // Возвращаем итоговую строку
            } catch (IOException e) {
                // Обрабатываем исключение ввода-вывода и возвращаем сообщение об ошибке
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Преобразуем строку результата в JSON массив
                JSONArray jsonArray = new JSONArray(result);
                LinearLayout films = findViewById(R.id.film);
                // Находим LinearLayout для отображения фильмов

                // Проходим по всем элементам JSON массива
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i); // Получаем текущий JSON объект

                    // Извлекаем данные о фильме из JSON объекта
                    int film_id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String category = jsonObject.getString("category_film");
                    String film_year = jsonObject.getString("year");
                    String film_age = jsonObject.getString("age");
                    String film_image = jsonObject.getString("image");
                    String rate = jsonObject.getString("rate");
                    String description = jsonObject.getString("description");
                    String link = jsonObject.getString("link");

                    // Добавляем карточку фильма в LinearLayout
                    films.addView(createCard(
                            film_id,
                            title,
                            film_year,
                            film_age,
                            film_image,
                            rate
                    ));

                    // Создаем объект FilmInformation и добавляем его в список film_informations
                    film_informations.add(new FilmInformation(film_id, title, category, film_year, film_age, film_image, rate, description, link));
                }
                // Обработка исключений
            } catch (JSONException e) {

            } catch (Exception e) {

            }
        }}

        public class FilmService {
        //для возврата ко всем фильмам
        // Метод, который выполняет вызов API для получения фильмов
        public void fetchFilms() {
            LinearLayout films = findViewById(R.id.film);
            films.removeAllViews();
            new GetFilmTask().execute("https://film-dex.vercel.app/api/v1/films");
        }

    }
//для фильтрации по категориям
    private void filterFilmsByCategory(String category) {
        LinearLayout films = findViewById(R.id.film);
        films.removeAllViews(); // Очищаем предыдущие карты фильмов

        for (FilmInformation film : film_informations) {
            if (film.category.equals(category)) {
                films.addView(createCard(
                        film.film_id,
                        film.title,
                        film.film_year,
                        film.film_age,
                        film.film_image,
                        film.rate
                ));
            }
        }
    }


    protected View createCard(
            int film_id,
            String title,
            String film_year,
            String film_age,
            String film_image,
            String rate
    ) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View card = inflater.inflate(R.layout.film_card, null);

        TextView cardtitle = card.findViewById(R.id.film_title);
        cardtitle.setText(title);

        TextView year = card.findViewById(R.id.film_year);
        year.setText(String.format(film_year));

        TextView age = card.findViewById(R.id.film_age);
        age.setText(String.format(film_age));

        TextView reitingText = card.findViewById(R.id.film_reiting);
        reitingText.setText(String.format(rate));

        ImageView imageView = card.findViewById(R.id.film_image);
        try {
            Picasso.get()
                    .load(film_image)
                    .into(imageView);
        } catch (Exception e) {

        }

        card.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(view.getContext(), Information.class);
                intent.putExtra("film_id", film_id);
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                // Обработка исключения
                Toast.makeText(view.getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return card;
    }

    //карты для жанров и их получение
    private class GetGenreTask extends AsyncTask<String, Void, String> {

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
                LinearLayout genres = findViewById(R.id.genre_card);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int genre_id = jsonObject.getInt("id");
                    String title_g = jsonObject.getString("title");
                    String genre_image = jsonObject.getString("image");
                    genres.addView(createCard(
                            title_g,
                            genre_image,
                            genre_id
                    ));
                }
            }
            catch (JSONException e) {}
            catch (Exception e) {}
        }

    }

    protected View createCard(
            String title_g,
            String genre_image,
            int genre_id
    ) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View card = inflater.inflate(R.layout.genres_cards, null);

        TextView cardtitlegenre = card.findViewById(R.id.title_genre);
        cardtitlegenre.setText(title_g);


        ImageView imageView2 = card.findViewById(R.id.image_genre);
        try {
            Picasso.get()
                    .load(genre_image)
                    .into(imageView2);
        } catch (Exception e) {
        }

        card.setOnClickListener(view -> {
            new GetFilmWithGenreTypeTask().execute("https://film-dex.vercel.app/api/v1/film_with_genre/" + genre_id);
        });


        return card;
    }
    //фильтрация фильмов по жанрам
    private class GetFilmWithGenreTypeTask extends AsyncTask<String, Void, String> {

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
                JSONObject jsonObject = new JSONObject(result);

                ArrayList<Integer> films = new ArrayList<>();
                JSONArray filmsArray = jsonObject.getJSONArray("films");
                for (int i = 0; i < filmsArray.length(); i++) {
                    films.add(filmsArray.getInt(i));
                }
                createFilmCardsWithType(films);

            } catch (JSONException e) {}
        }
    }

    protected void createFilmCardsWithType(ArrayList<Integer> filmsIds) {
        try {
            LinearLayout filmLayout = findViewById(R.id.film);
            filmLayout.removeAllViews();
            for (FilmInformation film : film_informations) {
                if (filmsIds.contains(film.film_id)) {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View card = inflater.inflate(R.layout.film_card, null);

                    TextView cardtitle = card.findViewById(R.id.film_title);
                    cardtitle.setText(film.title);

                    TextView year = card.findViewById(R.id.film_year);
                    year.setText(String.format(film.film_year));

                    TextView age = card.findViewById(R.id.film_age);
                    age.setText(String.format(film.film_age));

                    TextView reitingText = card.findViewById(R.id.film_reiting);
                    reitingText.setText(String.format(film.rate));

                    ImageView imageView = card.findViewById(R.id.film_image);
                    try {
                        Picasso.get()
                                .load(film.film_image)
                                .into(imageView);
                    } catch (Exception e) {}

                    card.setOnClickListener(view -> {
                        Intent intent = new Intent(view.getContext(), Information.class);
                        intent.putExtra("film_id", film.film_id);
                        view.getContext().startActivity(intent);
                    });

                    filmLayout.addView(card);
                }
            }
        }
        catch (Exception e) {
        }
    }



}

