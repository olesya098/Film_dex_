package com.hfad.filmdex;

public class FilmInformation {
        int film_id;
        String title;
      String category;
        String film_year;
        String film_age;
        String film_image;
        String rate;
        String description;
        String link;


        FilmInformation(int film_id, String title,
                        String category,
                        String film_year,
               String film_age, String film_image , String rate,String description,String link) {
            this.film_id = film_id;
            this.title = title;
            this.category = category;
            this.film_year = film_year;
            this.film_age = film_age;
            this.film_image = film_image ;
            this.description = description;
            this.rate = rate;
            this.link = link;

        }
    }

