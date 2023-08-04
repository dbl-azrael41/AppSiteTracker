# Практическая работа по модулю МДК 03.01 "Технология разработки программного обеспечения"
# Название: Server
# Разработал: Забнев Константин Андреевич
# Группа: ТМП-81
# Дата: 27.03.2023
# Версия: 1.0
# Язык: Python
#
# Краткое описание:
# Серверное приложение для обработки данных веб-ресурсов и отправки их приложению клиенту.
# Задание:
# Разработать серверное приложение. Приложение должно в определенные промежутки времени проверять
# активность веб-ресурсов и записывать результаты проверки в базу данных, а также обеспечивать подключение
# с приложением клиентом, для отправки ему информации из базы данных.
#
# Класс, используемый в основной программе:
# Server - используется для создания и запуска сервера.



# Класс: Server
# Язык: Python
# Краткое описание:
# Класс, необходимый для создания и запуска сервера.
#
# Функции, используемые в классе:
# __init__ - функция инициализации;
# server_loop - функция, принимающая подключения приложений клиентов;
# checking_loop - функция, проверяющая активность веб-ресурсов;
# start_server - функция, запускающая сервер;
# send_msg - функция, отправляющая клиенту JSON объект;
# get_msg - функция, принимающая запрос от пользователя;
# collection_of_statistics_loop - функция сбора статистики.

import os
import re
import socket
import traceback
from socket import *
import json
from threading import Thread
from time import sleep

import pdfplumber
import pymysql
import urllib3
from notifiers import get_notifier
from config import *


class Server:
    # __init__ - функция инициализации.
    # Формальные параметры:
    # ip - ip адрес сервера;
    # port - port сервера, необходимый подключения к нему.
    def __init__(self, ip, port):
        print(f'SERVER IP: {ip}\nSERVER PORT: {port}\n')

        # Создание сокета, по которому будет подключаться пользователь
        self.server = socket(AF_INET, SOCK_STREAM)
        self.server.bind((ip, port))
        self.ip = ip
        self.port = port
        self.server.listen(10)

    # server_loop - функция, принимающая подключения приложений клиентов.
    # Локальные переменные:
    # user - подключившийся пользователь;
    # address - ip и порт подключившегося пользователя.
    def server_loop(self):
        while True:
            # Подключение пользователя к серверу
            user, address = self.server.accept()
            print(f'\n\nCLIENT CONNECTED:\n\tIP: {address[0]}\n\tPORT: {address[1]}\n\n')

            # Создание отдельного потока для данного пользователя
            Thread(target=self.get_msg, args=(user,)).start()

    # collection_of_statistics_loop - функция сбора статистики.
    # Локальные переменные:
    # file - файл, в котором хранится статистика сайта;
    # path - путь к файлу, в котором хранится статистика;
    # pdf - переменная, в которую записываются данные из файла при его открытии;
    # data - словарь, в котором хранятся статистические данные;
    # page_1 - переменная, необходимая для хранения первой страницы со статистикой;
    # page_2 - переменная, необходимая для хранения второй страницы со статистикой;
    # re_period - регулярное выражение, необходимое для извлечения периода, за который собрана статистика;
    # re_visitors - регулярное выражение, необходимое для извлечения количества посетителей;
    # re_new_visitors - регулярное выражение, необходимое для извлечения количества новых посетителей;
    # re_failures - регулярное выражение, необходимое для извлечения количества отказов;
    # re_viewing_depth - регулярное выражение, необходимое для извлечения глубины просмотра веб страницы;
    # re_time_on_site - регулярное выражение, необходимое для извлечения среднего времени на сайте;
    # conn - переменная, необходимая для подлкючения к базе данных;
    # curs - переменная необходимая для отправки запроса и получения данных из базы данных;
    # id_site - id сайта, для которого была собрана статистика.
    def collection_of_statistics_loop(self):
        while True:
            for file in os.listdir('stat'):
                if file.endswith('.pdf'):
                    path = f'stat/{file}'

                    with pdfplumber.open(path) as pdf:
                        data = dict()

                        # Пирсинг файла

                        # Парсинг первой страницы
                        page_1 = pdf.pages[0].extract_text(codec='utf-8')

                        # Извлечение url сайта, для которого собирается статистика
                        data['URL'] = page_1.split('\n')[2].split('•')[0][:-1].split()[-1]

                        # Извлечение периода, за который собиралась статистика
                        re_period = re.compile(r'\d{2} \w{3}.\d{2} \w{3} \d{4}').findall(page_1)
                        data['Период'] = re_period[0]

                        # Извлечение количества посетителей
                        re_visitors = re.compile(r'Посетители [\d ]{2,}').findall(page_1)
                        data['Посетители'] = int(re_visitors[0][11:].replace(' ', ''))

                        # Извлечение количества новых посетителей
                        re_new_visitors = re.compile(r'Новые посетители[\d ]{2,}').findall(page_1)
                        data['Новые посетители'] = int(re_new_visitors[0][17:].replace(' ', ''))

                        # Извлечение количества отказов
                        re_failures = re.compile(r'Отказы\n.+').findall(page_1)
                        data[re_failures[0].split('\n')[0]] = float(re_failures[0].split('\n')[1][:-1].replace(',', '.'))

                        # Парсинг второй страницы
                        page_2 = pdf.pages[1].extract_text(codec='utf-8')

                        # Извлечение глубины просмотра страниц сайта
                        re_viewing_depth = re.compile(r'\n\d+,\d{2}').findall(page_2)
                        data['Глубина просмотра'] = float(re_viewing_depth[0][1:].replace(',', '.'))

                        # Извлечение среднего времени на сайте
                        re_time_on_site = re.compile(r'\d+\x00\d\d ').findall(page_2)
                        data['Время на сайте'] = re_time_on_site[0][:-1].replace('\x00', ':')

                        # Подключение к базе данных
                        try:
                            conn = pymysql.connect(
                                host='localhost',
                                port=3306,
                                user=DB_LOGIN,
                                password=DB_PASS,
                                database=DB_NAME
                            )

                            # Получение ID сайта, для которого собиралась статистика
                            with conn:
                                curs = conn.cursor()
                                curs.execute(f'select ID_site from Site where LOCATE(\"http://{data.get("URL")}\", Url) > 0 or LOCATE(\"https://{data.get("URL")}\", Url) > 0;')
                                conn.commit()

                                id_site = curs.fetchall()
                                if len(id_site) > 0:

                                    # Добавление статистики сайта в базу данных
                                    curs.execute(f'insert into Stat(ID_s, Period, Visitors, New_visitors, Failures, Viewing_depth, Time_on_site) values ({id_site[0][0]}, \"{data.get("Период")}\", {data.get("Посетители")}, {data.get("Новые посетители")}, {data.get("Отказы")}, {data.get("Глубина просмотра")}, \"{data.get("Время на сайте")}\");')
                                    conn.commit()
                                else:

                                    # Добавление в базу данных сайта, для которого собиралась статистика в случае, если до этого его там не было
                                    curs.execute(f'insert into Site(Url, Activity) values (\"https://{data.get("URL")}\", 0);')
                                    conn.commit()

                                    # Получение ID добавленного сайта
                                    curs.execute(f'select ID_site from Site where LOCATE(\"http://{data.get("URL")}\", Url) > 0 or LOCATE(\"https://{data.get("URL")}\", Url) > 0;')
                                    conn.commit()
                                    id_site = curs.fetchall()

                                    # Добавление в базу данных статистики, собранной для этого сайта
                                    curs.execute(f'insert into Stat(ID_s, Period, Visitors, New_visitors, Failures, Viewing_depth, Time_on_site) values ({id_site[0][0]}, \"{data.get("Период")}\", {data.get("Посетители")}, {data.get("Новые посетители")}, {data.get("Отказы")}, {data.get("Глубина просмотра")}, \"{data.get("Время на сайте")}\");')
                                    conn.commit()

                        except Exception as e:
                            print(traceback.format_exc())
                            break
                    # Удаление файлов, из которых была извлечена статистика
                    os.remove(path)
            sleep(86400)


    # checking_loop - функция, проверяющая активность веб-ресурсов.
    # Локальные переменные:
    # conn - переменная, необходимая для подключения к базе данных;
    # cur_select - переменная, необходимая для отправки запроса на извлечение данных из базы;
    # cur_update - переменная, необходимая для обновления данных в базе данных;
    # row - строка из таблицы, полученной из базы данных;
    # status - код статус страницы;
    # is_act - переменная, показывающая активность веб страницы;
    # notification - переменная, хранящая в себе сообщение, которое будет отправлено в чат в телеграмме.
    def checking_loop(self):
        while True:
            print('Start of checks')

            # Подключение к базе данных
            try:
                conn = pymysql.connect(
                    host='localhost',
                    port=3306,
                    user=DB_LOGIN,
                    password=DB_PASS,
                    database=DB_NAME
                )

                # Отправка запроса о получении информации о веб-ресурсах
                with conn:
                    cur_select = conn.cursor()
                    cur_select.execute('select ID_site, Url from Site;')

                    # Проверка активности веб-ресурсов
                    for row in cur_select.fetchall():
                        try:
                            status = urllib3.PoolManager(retries = urllib3.Retry()).request("GET", row[1]).status
                            is_act = 0 if status != 200 else 1
                        except urllib3.exceptions.MaxRetryError:
                            is_act = 0

                        # Запрос на изменение данных в базе
                        cur_update = conn.cursor()
                        cur_update.execute(f'update Site set Activity = {is_act} where ID_site = {row[0]};')
                        conn.commit()

                # Отправка уведомления о том, что проверка завершена
                notification = 'Проверка завершена.\nЧтобы посмотреть список сайтов, к которым в данный момент нельзя подключиться используйте команду /inaccessible'
                self.send_notification(USERS_CHAT_ID, notification)
            except Exception as e:
                print(traceback.format_exc())

            print('End of ckecks')
            sleep(3600)

    # start_server - функция, запускающая сервер.
    def start_server(self):
        # Создание потока для проверки веб-ресурсов
        Thread(target=self.checking_loop).start()
        # Создание потока, принимающего подключения клиентов
        Thread(target=self.server_loop).start()
        # Создание потока для сбора статистики веб-ресурсов
        Thread(target=self.collection_of_statistics_loop()).start()




    # send_notification - функция, отправляющая уведомления.
    # Формальные параметры:
    # chat_id - id чата, в который будет отправлено уведомление;
    # msg - сообщение, которое будет отправлено в чат.
    # Локальная переменная:
    # telegram - чат в телеграмме, куда будет отправлено сообщение.
    def send_notification(self, chat_id, msg):
        # print(f'Отправка сообщения в чат: {chat_id}\nСообщение: {msg}')
        telegram = get_notifier('telegram')
        telegram.notify(token=TOKEN, chat_id=chat_id, message=msg)

    # send_msg - функция, отправляющая клиенту JSON объект.
    # Формальные параметры:
    # user - пользователь, которому будут отправлены данные;
    # msg - данные, которые будут отправлены пользователю.
    def send_msg(self, user, msg):
        print(json.dumps(msg, ensure_ascii=False).encode('utf-8'))
        user.send(json.dumps(msg, ensure_ascii=False).encode('utf-8'))

    # get_msg - функция, принимающая запрос от пользователя.
    # Формальный параметр:
    # user - пользователь, запрос которого будет принимать сервер.
    # Локальные переменные:
    # is_work - переменная, показывающая продолжает ли пользователь присылать запросы;
    # data - запрос, полученный от пользователя;
    # query - запрос пользователя, приведенный к кодировке utf-8;
    # conn - переменная для подключения к базе данных;
    # curs - переменная, необходимая для отправки запросов и получения ответов от базы данных;
    # column_names - наименования колонок таблицы, из данных которой была сделана выборка;
    # jsonResponse - JSON объект, который будет отправляться в качестве ответа пользователю;
    # lines - строка из таблицы, полученной из базы данных;
    # line - определенный столбец из строки lines;
    # name - наименование колонки line;
    # ans - закодированный для отправки ответ в формате JSON.
    def get_msg(self, user):
        is_work = True
        while is_work:

            # Получение запроса от пользователя
            try:
                data = user.recv(1024)
            except Exception as e:
                print(traceback.format_exc())
                data = ''
                is_work = False

            if len(data) > 0:

                # Декодирование запроса в формат utf-8
                query = data.decode('utf-8').strip()
                print(query)

                # Отключение пользователя в случае получения соответствующего запроса
                if query == 'disconnect':
                    user.close()
                    is_work = False
                else:

                    # Подключение к базе данных
                    conn = pymysql.connect(
                        host='localhost',
                        port=3306,
                        user=DB_LOGIN,
                        password=DB_PASS,
                        database=DB_NAME
                    )

                    # Отправка базе данных запроса пользователя
                    with conn:
                        curs = conn.cursor()
                        curs.execute(query)
                        conn.commit()

                        # Получение данных из базы
                        data = curs.fetchall()
                        if len(data) > 0:

                            # Получение наименований колонок из соответствующей таблицы базы данных
                            column_names = []
                            for j in curs.description:
                                column_names.append(j[0])

                            # Формирование JSON объекта
                            jsonResponse = '{\n"empty_result": false,\n"response": [{\n'

                            for lines in data:
                                for name, line in zip(column_names, lines):
                                    if type(line) == int:
                                        jsonResponse += '\t"{}": {},\n'.format(name, line)
                                    else:
                                        jsonResponse += '\t"{}": "{}",\n'.format(name, line)
                                jsonResponse = jsonResponse[:-2] + "\n}, {\n"
                            jsonResponse = jsonResponse[:-4] + "\n]}"
                        else:
                            jsonResponse = '{\n"empty_result": true,\n"response": []\n}'
                            print('empty result')

                        # Отправка данных пользователю
                        if query.split()[0] == 'select':
                            ans = json.loads(jsonResponse)
                            self.send_msg(user, ans)
            else:
                is_work = False
        else:
            print('\n\nCLIENT DISCONNECTED\n\n')


if __name__ == '__main__':

    # Получение ip адреса сервера
    s = socket(AF_INET, SOCK_DGRAM)
    s.connect(("8.8.8.8", 8000))

    # Создание и запуск сервера
    server = Server(s.getsockname()[0], 8000)
    server.start_server()

    # server = Server('92.53.106.110', 8000, '')

    # получение ip основного сервера
    # print(gethostbyname('base2.tw1.ru'))
