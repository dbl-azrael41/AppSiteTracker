/*
Класс: User
Язык: Java
Краткое описание:
Класс необходимый для того, чтобы структурировать информацию о пользователях.

Локальная переменная:
id - id пользователя;
login - логин пользователя;
pass - пароль пользователя;
status - статус пользователя.
tg_id - id пользователя в телеграмме.

Функциия, используемая в классе:
User - конструктор класса User.
*/
package com.example.apptracker;

public class User {
    public  int id;
    public  String login;
    public  String pass;
    public  String status;
    public  String tg_id;

    /*
    User - конструктор класса User.
    */
    public User() {}

    /*
    User - конструктор класса User.
    Формальные параметры:
    id - id пользователя;
    login - логин пользователя;
    pass - пароль пользователя;
    status - статус пользователя.
    tg_id - id пользователя в телеграмме.
    */
    public User(int id, String login, String pass, String status, String tg_id) {
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.status = status;
        this.tg_id = tg_id;
    }
}
