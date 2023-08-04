/*
Класс: Activity
Язык: Java
Краткое описание:
Класс необходимый для того, чтобы структурировать информацию о сайтах.

Локальная переменная:
id - id сайта;
isFavorite - переменная, показывающая находится ли сайт во вкладке "Избранное";
url - url адрес сайта;
isActive - переменная, показывающая активность сайта.

Функция, используемая в классе:
Activity - конструктор класса.
*/

package com.example.apptracker;

public class Activity {
    public int id;
    public boolean isFavorite;
    public String url;
    public boolean isActive;

    /*
    Activity - констуктор класса.
    */
    public Activity() {}

    /*
    Activity - констуктор класса.
    Формальные параметры:
    id - id сайта;
    isFavorite - переменная, показывающая находится ли сайт во вкладке "Избранное";
    url - url адрес сайта;
    isActive - переменная, показывающая активность сайта.
    */
    public Activity(int id, String url, boolean isFavorite, boolean isActive) {
        this.id = id;
        this.url = url;
        this.isFavorite = isFavorite;
        this.isActive = isActive;
    }
}
