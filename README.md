# voterest
Realise of app as a service of vote for restaurant`s lunch. Spring Boot 2.4.x

К сожалению, из-за нехватки времени проект написан не "с нуля", а с примера Г. Кислина,
взятого из ветки https://github.com/JavaWebinar/topjava/tree/spring-boot

Проект разработан по технологии ДТО:
Сущности (model):
-  User - пользователь,
-  Role - роль: {ADMIN, VOTER},
-  Restaurant - ресторан,
-  Dish - ресторанные блюда,
-  Vote - голос, отданный пользователем за ресторан.

Все операции посредством контроллеров по изменению и добавлению информации, кроме фиксирования отданного 
пользователем своего голоса за определенный ресторан, выделены в защищенные 
авторизацией пути.
Например, доступные только для администратора: /rest/admin/
Также имеются общедоступные части API - /rest/profile/register - регистрация пользователя,
/rest/dishs/ - просмотр меню ресторанов и самих ресторанов /rest/restaurants.

Проект разворачивается по URL: http://localhost:8080
В проект внедрено OpenAPI http://localhost:8080/swagger-ui.html 

Тесты не успел закончить. В наличии только один тест.

# Некоторые команды:
* Добавление ресторана (Админ):
curl -X 'POST' \
'http://localhost:8080/rest/admin/restaurants' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{"name": "Блок","dateIns": "2021-05-17T02:00:39.318Z"}'
  

* Добавление меню (Админ):
curl -X 'POST' \
'http://localhost:8080/rest/admin/dishs' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{"title": "Лобстер","dateMenu": "2021-05-17T02:07:58.779Z","price": 254,"restaurant": {"id": 2}}'


* Добавление голоса (авторизованный пользователь):
  curl -X 'POST' \
  'http://localhost:8080/rest/profile/votes' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"dateVote": "2021-05-17T02:11:43.623Z","restaurant": {"id": 2}}'
  
Администратор может вести меню только того ресторана, который он ввел в БД
Если пользователь голосует несколько раз, то в сутки и только в отведенное время 
фиксируется только один, последний голос. Т.е. предыдущее голосование замещается.
Если интервал времени, отведенного на голосование закончился - ПО выдает 
соответствующее предупреждение, голос при этом не учитывается.

Интервал регулируется константой VOTE_BORDER_TIME = 11 (DateTimeUtil.class)

Результат голосования доступен по URL:
http://localhost:8080/rest/profile/votes/result

Вид результата:
{"Mr. Bo": 11,
"Art-Caviar": 9 }

где перечислены рестораны и количество полученных голосов.

БД H2:mem и доступна во время исполнения ПО
jdbc:h2:tcp://localhost:9092/mem:voterest

Была попытка сделать доп. валидацию на имя ресторана, но обошелся только сознанием уникатьного индекса.
Для кеширования выбрана сущность - Ресторан.

Приятной проверки.
Спасибо.
