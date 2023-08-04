/*
Класс: Statistics
Язык: Java
Краткое описание:
Класс необходимый для того, чтобы структурировать информацию о статистике сайта.

Локальная переменная:
url - url сайта;
period - период, за который собрана статистика;
visitors - количество посетителей;
new_visitors - количество новых посетителей;
failures - количество отказов;
viewing_depth - глубина просмотра сайта;
time_on_site - среднее время на сайте.

Функция, используемая в классе:
Statistics - конструктор класса Statistics.
*/

package com.example.apptracker;

public class Statistics {
    public String url;
    public String period;
    public int visitors;
    public int new_visitors;
    public double failures;
    public double viewing_depth;
    public String time_on_site;

    /*
    Statistics - конструктор класса Statistics.
    */
    public Statistics() {}

    /*
    Statistics - конструктор класса Statistics.
    Формальные параметры:
    url - url сайта;
    period - период, за который собрана статистика;
    visitors - количество посетителей;
    new_visitors - количество новых посетителей;
    failures - количество отказов;
    viewing_depth - глубина просмотра сайта;
    time_on_site - среднее время на сайте.
    */
    public Statistics(String url, String period, int visitors, int new_visitors, double failures, double viewing_depth, String time_on_site) {
        this.url = url;
        this.period = period;
        this.visitors = visitors;
        this.new_visitors = new_visitors;
        this.failures = failures;
        this.viewing_depth = viewing_depth;
        this.time_on_site = time_on_site;
    }
}
