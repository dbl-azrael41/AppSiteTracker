/*
Класс: ConnectionToServer
Язык: Java
Краткое описание:
Класс, необходимый для подключения к серверу.

Переменные, используемые в классе:
socket - сокет, по которому осуществляется подключение;
reader - переменная, необходимая для получения данных от сервера;
writer - переменная, необходимая для отправки запроса на сервер.

Функции, используемые в классе:
ConnectionToServer - конструктор класса ConnectionToServer;
send_req - функция, отправляющая запрос на сервер;
get_resp - функция, принимающая ответ от сервера;
getSite - функция получения данных о сайтах;
getSelectedSites - функция получения данных о избранных сайтах данного пользователя;
getStat - функция получения статистических данных сайтов;
getSelectedSitesStat - функция получения статистических данных для избранных сайтов;
getUsers - функция получения информации о пользователях.
*/

package com.example.apptracker;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ConnectionToServer {
    public final Socket socket;
    public final BufferedReader reader;

    public final BufferedWriter writer;

    /*
    ConnectionToServer - конструктор класса ConnectionToServer.
    Формальные параметры:
    ip - ip, по кторому будет осуществляться подключение к серверу;
    port - порт, по которому будет осуществляться подключение к серверу.
    */
    public ConnectionToServer(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    send_req - функция, отправляющая запрос на сервер.
    Формальный параметр:
    request - отправляемый на сервер запрос.
    */
    public void send_req(String request) throws IOException {
        writer.write(request);
        writer.newLine();
        writer.flush();
    }

    /*
    get_resp - функция, принимающая ответ от сервера.
    Локальные переменные:
    jsonResponse - переменная, хранаящая в себе ответ от сервера в формате JSON;
    in - переменная, принимающая от сервера ответ в виде байтов;
    recBytes - массив байт, в который считываются данные, принимаемые от сервера;
    count - переменная, хранящая количество байт, считанное за итерацию.
    */
    public String get_resp() throws IOException {
        String jsonResponse = "";
        DataInputStream in = new DataInputStream(socket.getInputStream());
        byte[] recBytes = new byte[1024];

        //Считывание байтов

        do {
            int count = in.read(recBytes);

            //Преобразование полученных байт к кодировке UTF-8

            jsonResponse += new String(recBytes, 0, count, StandardCharsets.UTF_8);
        } while(in.available() > 0);
        return jsonResponse;
    }

    /*
    getSite - функция получения данных о сайтах.
    Используемые функции:
    send_req - функция, отправляющая запрос на сервер;
    get_resp - функция, принимающая ответ от сервера.
    Локальные переменные:
    sites_list - полученный в результате извлечения данных из JSON файла список сайтов;
    resp - переменная, хранящая в себе файл с данными, полученными от сервера;
    parser - переменная, необходимая для извлечения данных из JSON файла;
    jsonObject - переменная, хранящая в себе объект типа JSON;
    arrayJson - массив с объектами типа JSON;
    item - переменная цикла, необходимая для итерации по элементам массива arrayJson;
    site - переменная, хранящая в себе результат приведения значения из переменной item к типу JSONObject.
    */
    public ArrayList<Activity> getSite() throws IOException, ParseException {
        ArrayList<Activity> sites_list = new ArrayList<>();

        // Отправка запроса на сервер

        send_req("select * from site");

        // Получение ответа от сервера

        String resp = get_resp();

        // Преобразование полученного от сервера ответа в формат JSON

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(resp);

        // Извлечение из ответа списка с сайтами

        JSONArray arrayJson = (JSONArray) jsonObject.get("response");

        // Преобразование массива JSON в массив с элементами типа Activity

        for (Object item: arrayJson) {
            JSONObject site = (JSONObject) item;
            sites_list.add(new Activity(((Long)site.get("ID_site")).intValue(),
                    (String)site.get("Url"),
                    false,
                    ((Long)site.get("Activity") == 1)));
        }

        return sites_list;
    }

    /*
    getSelectedSites - функция получения данных о избранных сайтах данного пользователя.
    Формальный параметр:
    ID_user - id пользователя, список избранных сайтов которого необходимо получить.
    Используемые функции:
    send_req - функция, отправляющая запрос на сервер;
    get_resp - функция, принимающая ответ от сервера.
    Локальные переменные:
    selected_sites - полученный в результате извлечения данных из JSON файла список избранных сайтов;
    resp - переменная, хранящая в себе файл с данными, полученными от сервера;
    parser - переменная, необходимая для извлечения данных из JSON файла;
    jsonObject - переменная, хранящая в себе объект типа JSON;
    arrayJson - массив с объектами типа JSON;
    item - переменная цикла, необходимая для итерации по элементам массива arrayJson;
    site - переменная, хранящая в себе результат приведения значения из переменной item к типу JSONObject.
    */
    public ArrayList<Activity> getSelectedSites (int ID_user) throws IOException, ParseException {
        ArrayList<Activity> selected_sites = new ArrayList<>();

        // Отправка запроса на сервер

        send_req(String.format("select S.* from site S inner join selected_sites SS on S.ID_site = SS.ID_s where SS.ID_us = %s;", ID_user));

        // Получение ответа от сервера

        String resp = get_resp();

        // Преобразование полученного от сервера ответа в формат JSON

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(resp);

        // Извлечение из ответа списка с сайтами

        JSONArray arrayJson = (JSONArray) jsonObject.get("response");

        // Преобразование  массива JSON в массив с элементами типа Activity

        for (Object item: arrayJson) {
            JSONObject site = (JSONObject) item;
            selected_sites.add(new Activity(((Long)site.get("ID_site")).intValue(),
                    (String)site.get("Url"),
                    true,
                    ((Long)site.get("Activity") == 1)));
        }

        return selected_sites;
    }

    /*
    getStat - функция получения статистических данных сайтов.
    Используемые функции:
    send_req - функция, отправляющая запрос на сервер;
    get_resp - функция, принимающая ответ от сервера.
    Локальные переменные:
    selected_sites - полученный в результате извлечения данных из JSON файла список избранных сайтов;
    resp - переменная, хранящая в себе файл с данными, полученными от сервера;
    parser - переменная, необходимая для извлечения данных из JSON файла;
    jsonObject - переменная, хранящая в себе объект типа JSON;
    arrayJson - массив с объектами типа JSON;
    item - переменная цикла, необходимая для итерации по элементам массива arrayJson;
    stat - переменная, хранящая в себе результат приведения значения из переменной item к типу JSONObject.
    */
    public ArrayList<Statistics> getStat() throws IOException, ParseException {
        ArrayList<Statistics> statistics = new ArrayList<>();

        // Отправка запроса на сервер

        send_req("select S.url, St.Period, St.Visitors, St.New_visitors, St.Failures, St.Viewing_depth, St.Time_on_site from site S inner join stat St on S.ID_site = St.ID_s;");

        // Получение ответа от сервера

        String resp = get_resp();

        // Преобразование полученного от сервера ответа в формат JSON

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(resp);

        // Извлечение из ответа списка с сайтами

        JSONArray arrayJson = (JSONArray) jsonObject.get("response");

        // Преобразование  массива JSON в массив с элементами типа Statistics

        for (Object item: arrayJson) {
            JSONObject stat = (JSONObject) item;
            statistics.add(new Statistics((String)stat.get("url"),
                    (String)stat.get("Period"),
                    ((Long)stat.get("Visitors")).intValue(),
                    ((Long)stat.get("New_visitors")).intValue(),
                    Double.parseDouble((String)stat.get("Failures")),
                    Double.parseDouble((String)stat.get("Viewing_depth")),
                    (String)stat.get("Time_on_site")));
        }
        return statistics;
    }

    /*
    getSelectedSitesStat - функция получения статистических данных для избранных сайтов.
    Формальный параметр:
    ID_user - id пользователя, для списока избранных сайтов которого необходимо получить статистику.
    Используемые функции:
    send_req - функция, отправляющая запрос на сервер;
    get_resp - функция, принимающая ответ от сервера.
    Локальные переменные:
    selected_sites - полученный в результате извлечения данных из JSON файла список избранных сайтов;
    resp - переменная, хранящая в себе файл с данными, полученными от сервера;
    parser - переменная, необходимая для извлечения данных из JSON файла;
    jsonObject - переменная, хранящая в себе объект типа JSON;
    arrayJson - массив с объектами типа JSON;
    item - переменная цикла, необходимая для итерации по элементам массива arrayJson;
    stat - переменная, хранящая в себе результат приведения значения из переменной item к типу JSONObject.
    */
    public ArrayList<Statistics> getSelectedSitesStat(int ID_user) throws IOException, ParseException {
        ArrayList<Statistics> statistics = new ArrayList<>();

        // Отправка запроса на сервер

        send_req(String.format("select S.url, St.Period, St.Visitors, St.New_visitors, St.Failures, St.Viewing_depth, St.Time_on_site from selected_sites SS inner join site S on SS.ID_s = S.ID_site inner join stat St on S.ID_site = St.ID_s where SS.ID_us = %s;", ID_user));

        // Получение ответа от сервера

        String resp = get_resp();

        // Преобразование полученного от сервера ответа в формат JSON

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(resp);

        // Извлечение из ответа списка с сайтами

        JSONArray arrayJson = (JSONArray) jsonObject.get("response");

        // Преобразование  массива JSON в массив с элементами типа Statistics

        for (Object item: arrayJson) {
            JSONObject stat = (JSONObject) item;
            statistics.add(new Statistics((String)stat.get("url"),
                    (String)stat.get("Period"),
                    ((Long)stat.get("Visitors")).intValue(),
                    ((Long)stat.get("New_visitors")).intValue(),
                    Double.parseDouble((String)stat.get("Failures")),
                    Double.parseDouble((String)stat.get("Viewing_depth")),
                    (String)stat.get("Time_on_site")));
        }
        return statistics;
    }

    /*
    getUsers - функция получения информации о пользователях.
    Используемые функции:
    send_req - функция, отправляющая запрос на сервер;
    get_resp - функция, принимающая ответ от сервера.
    Локальные переменные:
    selected_sites - полученный в результате извлечения данных из JSON файла список избранных сайтов;
    resp - переменная, хранящая в себе файл с данными, полученными от сервера;
    parser - переменная, необходимая для извлечения данных из JSON файла;
    jsonObject - переменная, хранящая в себе объект типа JSON;
    arrayJson - массив с объектами типа JSON;
    item - переменная цикла, необходимая для итерации по элементам массива arrayJson;
    user - переменная, хранящая в себе результат приведения значения из переменной item к типу JSONObject.
    */
    public ArrayList<User> getUsers() throws IOException, ParseException {
        ArrayList<User> users_list = new ArrayList<>();

        // Отправка запроса на сервер

        send_req("select * from users;");

        // Получение ответа от сервера

        String resp = get_resp();

        // Преобразование полученного от сервера ответа в формат JSON

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(resp);

        // Извлечение из ответа списка с сайтами

        JSONArray arrayJson = (JSONArray) jsonObject.get("response");

        // Преобразование  массива JSON в массив с элементами типа User

        for (Object item: arrayJson) {
            JSONObject user = (JSONObject) item;
            users_list.add(new User(((Long)user.get("ID_user")).intValue(),
                    (String)user.get("Login"),
                    (String)user.get("Pass"),
                    (String)user.get("Status_us"),
                    (String)user.get("Tg_id")));
        }

        return users_list;
    }
}
