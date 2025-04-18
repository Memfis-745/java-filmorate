# java-filmorate

## Filmorate
### Описание приложения
Сервис Filmorate представляет RESTful API, позволяющее пользователям оценивать фильмы и на этом основании возвращает картины, рекомендованные к просмотру. Разработка приложения велась на Spring Boot с использованием JdbcTemplate и базы данных – H2.
### Модели
Модели хранятся в разделе model. К ним относятся классы:
•	Film – фильм,
•	Genre – жанр фильма,
•	Mpa – категория MPA по рейтингу Американской Киноассоциации,
•	User – пользователь
#### Film 
Класс Film имеет следующие поля:
1.	идентификатор – id  (long);
2.	название – name (String);
3.	описание – description (String);
4.	дата релиза – releaseDate (LocalDate);
5.	продолжительность фильма – duration (int);
6.	категория фильма – mpa (Mpa);
7.	жанры – genres (LinkedHashSet);
#### Genre
Класс Genre имеет поля:
1.	идентификатор – id (int);
2.	название – name (String).
      Поля класса MPA:
1.	идентификатор – id (int);
2.	название – name (String).
      Поля класса User:
1.	идентификатор – id (long);
2.	электронная почта – email (String);
3.	логин пользователя – login (String);
4.	имя - name (String);
5.	дата рождения  - birthday (LocalDate).
### Контроллеры
Контроллеры хранятся в разделе controllers
#### FilmController
- обрабатывает запросы к фильмам,
1.	GET /films - получение всех фильмов.
2.	GET /films/{filmId} – получение фильма по идентификатору.
3.	GET /films/popular - возвращает список из первых 10 наиболее популярных фильмов.
4.	POST /films - добавление фильма.
5.	PUT /films - обновление фильма.
6.	PUT /films/{filmId}/like/{userId} — обновление лайка к фильму.
7.	DELETE /films/{filmId}/like/{userId} — пользователь удаляет лайк.
#### GenreController
- обрабатывает запросы по жанрам
1.	GET /genres - получение списка всех жанров.
2.	GET /genres/{id} – получение жанра по идентификатору.
#### MpaController
- обрабатывает запросы по рейтингу MPA
1.	GET /mpa – получение всех категорий MPA.
2.	GET /mpa/{id} – получение категории по идентификатору.
#### UserController
- обрабатывает запросы по пользователям.
1.	GET /users - получение списка всех пользователей.
2.	GET /users/{userId} - получение данных о пользователе по id.
3.	GET /users/{userId}/friends - список друзей пользователя.
4.	GET /users/{userId}/friends/common/{otherId} — список общих друзей.
5.	POST /users - создание пользователя.
6.	DELETE /users/{userId} – удаление пользователя.
7.	PUT /users - обновление пользователя.
8.	PUT /users/{userId}/friends/{friendId} — добавление в друзья.
9.	DELETE /users/{userId}/friends/{friendId} — удаление из друзей.
### Сервисный блок
Расположен в разделе service и содержит логику работы приложения.
#### FilmService.
Этот сервис является центральным компонентом, который управляет данными о фильмах, обеспечивает валидацию и предоставляет API для доступа к этой информации. Он использует репозитории для взаимодействия с базой данных и логирование для отслеживания событий.

•	  `getAllFilms()`: Возвращает список всех фильмов.
•	  `getFilm(long id)`:  Возвращает фильм по его ID. Выбрасывает исключение `NotFoundException`, если фильм не найден.
•	 `getPopularFilms(Integer count)`: Возвращает список самых популярных фильмов. Если `count` не указан, возвращает 10 самых популярных.
•	  `createFilm(Film film)`: Создает новый фильм.  Проверяет существование MPA, сохраняет фильм в репозиторий, сохраняет жанры фильма. Выбрасывает исключения, если MPA не существует или не удалось сохранить данные.
•	  `updateFilm(Film film)`: Обновляет информацию о существующем фильме. Проверяет существование фильма и MPA, обновляет фильм и его жанры. Выбрасывает исключение `NotFoundException`, если фильм не найден.
•	  `addLike(Long filmId, Long userId)`: Добавляет лайк фильму от пользователя.
•	 `deleteLike(Long filmId, Long userId)`: Удаляет лайк фильму от пользователя.

#### GenreService и MpaService
Представляют собой классы, отвечающие за управление жанрами и рейтингами фильмов. Они предоставляют методы для получения списка всех жанров или рейтингов и получение конкретного жанра или рейтинга по его ID.

#### Класс UserService
Является компонентом, отвечающим за бизнес-логику управления пользователями в приложении. Он предоставляет методы для получения, создания, обновления и удаления пользователей, а также для управления их друзьями.

* Логирует информацию об обновлении. 
* Вызывает методы валидации для проверки корректности данных пользователя.
* Обновляет информацию о пользователе в репозитории.

`getAllUsers()`:
•   Возвращает список всех пользователей.
•   Логирует информацию о получении всех пользователей.

•   `getUser(long id)`:
•   Возвращает пользователя по его ID.
•   Логирует информацию о получении пользователя.
•   Вызывает метод валидации для проверки корректности ID.
•   Если пользователь не найден, выбрасывает исключение `NotFoundException`.

•   `getUserFriends(long id)`:
•   Возвращает список друзей пользователя по его ID.
•   Вызывает методы валидации для проверки корректности ID и существования пользователя.

•   `getCommonFriends(Long userId, Long friendId)`:
•   Возвращает список общих друзей между двумя пользователями.
•   Вызывает методы валидации для проверки корректности ID и существования обоих пользователей.

•   `create(User user)`:
•   Создает нового пользователя.
•   Логирует информацию о создании пользователя.
•   Проверяет наличие пользователя с таким же e-mail, выбрасывает исключение `DuplicatedDataException`, если такой пользователь уже существует.
•   Вызывает метод валидации для проверки корректности ID нового пользователя.

•   `delete(Long id)`:
•   Удаляет пользователя по его ID.
•   Вызывает методы валидации для проверки корректности ID и существования пользователя.
•   Возвращает `null` (возможно, стоит реализовать логику удаления).

•   `update(User user)`:
•   Обновляет данные пользователя.
•   Логирует информацию об обновлении.
•   Вызывает методы валидации для проверки корректности данных пользователя.
•   Обновляет информацию о пользователе в репозитории.

•   `addFriend(long id, long friendId)`:
•   Добавляет пользователя в друзья.
•   Вызывает методы валидации для проверки корректности ID и существования обоих пользователей.
•   Логирует информацию о добавлении в друзья.

•   `removeFriend(long id, long friendId)`:
•   Удаляет пользователя из друзей.
•   Вызывает методы валидации для проверки корректности ID и существования обоих пользователей.
•   Логирует информацию об удалении из друзей.

### Валидация

Валидация осуществляется с помощью заданных в моделях аннотаций, а также посредством класса Validation, который предназначен для централизованного выполнения проверок и валидаций данных, связанных с пользователями.
Ограничения для класса Film:
* название не может быть пустым;
* максимальная длина описания — 200 символов;
* дата релиза — не раньше 28 декабря 1895 года;
* продолжительность фильма должна быть положительной.
Для User:
* электронная почта не может быть пустой и должна содержать символ @;
* логин не может быть пустым и содержать пробелы;
* имя для отображения может быть пустым — в таком случае будет использован логин;
* дата рождения не может быть в будущем.
Приложение возвращает HTTP-коды:
•	400 — если ошибка валидации;
•	404 — для всех ситуаций, если искомый объект не найден;
•	500 — если возникло исключение.

**Методы: класса Validation**

* `validId(Long id)`:
* Проверяет, не является ли ID равным `null`. Если ID равен `null`, выбрасывает исключение `ConditionsNotMetException` с соответствующим сообщением.
* `validUser(Long id)`: Проверяет, существует ли пользователь с указанным ID в репозитории. Если пользователь не найден (метод `userRepository.getUser(id)` возвращает `Optional.empty()`), выбрасывает исключение `NotFoundException` с соответствующим сообщением.
* `userName(User user)`: Проверяет, является ли имя пользователя (`user.getName()`) равным `null` или пустым (isBlank). Если имя пользователя не задано, присваивает полю `name` значение логина пользователя (`user.getLogin()`).
Логирует информацию о проверке имени нового пользователя на уровне `TRACE`.
* `validUserId(User user)`: Проверяет, присвоен ли пользователю ID (`user.getId()`). Если ID равен `null`, выбрасывает исключение `InternalServerException` с сообщением о неудачной попытке сохранения данных. Также проверяет, существует ли пользователь с данным ID в репозитории.
Если пользователь с таким ID не найден, выбрасывает исключение `NotFoundException` с сообщением о том, что пользователь не создан.

Класс использует исключения (`ConditionsNotMetException`, `NotFoundException`, `InternalServerException`) для индикации различных типов ошибок валидации.

### Хранение данных

В блоке repository расположены классы-интерфейсы и их реализации и предназначен для работы с данными в БД H2 используя JDBC (Java Database Connectivity).

* `FilmRepository`:  Репозиторий для работы с информацией о фильмах.
* `UserRepository`:  Репозиторий для работы с информацией о пользователях.
* `MpaRepository`:  Репозиторий для работы с рейтингами MPA (Motion Picture Association).
* `GenreRepository`: Репозиторий для работы с жанрами фильмов.

#### Класс JdbcFilmRepository
**Методы:**

*   `findAllFilms()`:
    *   Возвращает `List<Film>`:  Список всех фильмов из базы данных.
    *   Использует `filmsExtractor` для преобразования результатов запроса в список объектов `Film`.

*   `getFilm(Long id)`:
    *   Возвращает `Optional<Film>`:  Фильм с указанным ID, обернутый в `Optional`, чтобы обработать случай, когда фильм не найден.
    *   Использует `filmExtractor` для преобразования результата запроса в объект `Film`.
    *   Использует `Optional.ofNullable()` чтобы вернуть `Optional`, который может содержать `null` если запрос не вернул результатов.

*   `createFilm(Film film)`:
    *   Создает новую запись о фильме в таблице `FILMS`.

    *   Выполняет SQL запрос `INSERT` для добавления данных фильма (название, описание, дата релиза, продолжительность, ID MPA).
    *   Использует `SqlParameterSource`, `MapSqlParameterSource` и `GeneratedKeyHolder` для безопасной передачи параметров в SQL запрос и автоматической генерации ID.
    *  Использует для получения автоматически сгенерированного ID новой записи.

•   `updateFilm(Film film)`:
•   Обновляет существующую запись о фильме в таблице `FILMS`.

•   `addLike(Long id, Long userId)`:
•   Добавляет лайк фильму от пользователя.

•   `deleteLike(Long id, Long userId)`:
•   Удаляет лайк фильма от пользователя.


#### Класс JdbcUserRepository

Класс `JdbcUserRepository` является реализацией интерфейса `UserRepository` и предназначен для работы с данными пользователей в базе данных, используя JDBC. Он предоставляет методы для доступа, создания, обновления и управления друзьями пользователей.

*   `getAllUsers()`:
    *   Возвращает `List<User>`: Список всех пользователей из базы данных.

*   `getUser(Long id)`:
    *   Возвращает `Optional<User>`: Пользователя с указанным ID, обернутого в `Optional`.
    *   Возвращает `Optional<User>`, чтобы корректно обработать ситуацию, когда пользователь с указанным ID не найден в базе данных.

*   `create(User user)`:
    *   Создает нового пользователя в базе данных.

•   `update(User user)`:
•   Обновляет информацию о существующем пользователе в базе данных.
Использует `SqlParameterSource`, `MapSqlParameterSource` и `GeneratedKeyHolder` для безопасной передачи параметров в SQL запрос и автоматической генерации ID.

•   `addFriends(Long id, Long friendId)`:
•   Добавляет связь дружбы между двумя пользователями.

•   `removeFriends(Long id, Long friendId)`:
•   Удаляет связь дружбы между двумя пользователями.

•   `findAllFriends(Long id)`:
•   Возвращает список друзей пользователя с указанным ID.

### Валидация
Данные, которые приходят в запросе на добавление нового фильма или пользователя проверяются. Эти данные должны соответствовать определённым критериям.
#### Для Film:
* название не может быть пустым;
* максимальная длина описания — 200 символов;
* дата релиза — не раньше 28 декабря 1895 года;
* продолжительность фильма должна быть положительной.
#### Для User:
* электронная почта не может быть пустой и должна содержать символ @; 
* логин не может быть пустым и содержать пробелы;
* имя для отображения может быть пустым — в таком случае будет использован логин;
* дата рождения не может быть в будущем.

Приложение возвращает HTTP-коды:
•	400 — если ошибка валидации;
•	404 — для всех ситуаций, если искомый объект не найден;
•	500 — если возникло исключение.

### Описание базы данных
БД состоит из нескольких таблиц, связанных между собой:

1. хранения пользователей (Users)
2. хранения фильмов (Films)
3. друзей (Friends)
4. лайков к фильмам (FilmUserLikes)
5. хранения рейтингов (Mpa)
6. хранения жанров (Genre)
7. таблицы-связки (Film-genre)

#### FILMS
1.	film_id (PK) – уникальный идентификатор фильма
2.	name – наименование фильма
3.	description - описание фильма
4.	release_date – дата выхода на экраны (YYYY-MM-DD)
5.	duration - продолжительность в минутах
6.	mpa_id (FK) – идентификатор рейтинга
#### USERS
1. user_id (PK) – идентификатор пользователя
2. email – эл. почта пользователя
3. login – логин пользователя
4. name – имя пользователя 
5. birthday – дата рождения пользователя
#### LIKES
1.	film_id (FK) – идентификатор фильма
2.	user_id (FK) – идентификатор пользователя, лайкнувшего фильм
#### FILM_GENRE
1.	film_id (FK) – идентификатор фильма
2.	genre_id (FK) – идентификатор жанра
#### GENRE
1.	genre_id (PK) – идентификатор жанра
2.	genre_name – наименование жанра
#### MPA
1.	mpa_id (PK) – идентификатор рейтингов
2.	mpa_name – наименование рейтинга
#### FRIENDS
1.	user_id (FK) – идентификатор пользователя
2.	friend_id (FK) – идентификатор друга пользователя

### пример запросов в БД:
#### Получить список из 10 самых популярных фильмов:
SELECT COUNT(l.user_id) AS sum_likes,
      f.FILM_ID,
      f.NAME,
      f.DESCRIPTION,
      f.RELEASE,
      f.DURATION,
      f.MPA_ID,
      m.MPA_NAME,
      fg.GENRE_ID,
      g.GENRE_NAME
      FROM FILMS AS f
      LEFT JOIN FILMUSERLIKES AS l ON f.film_id=l.film_id
      LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
      LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID
      LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
      GROUP BY f.NAME, f.FILM_ID, f.DESCRIPTION, f.RELEASE,
      f.DURATION, f.MPA_ID, m.MPA_NAME, fg.GENRE_ID, g.GENRE_NAME
      ORDER BY COUNT(l.user_id) DESC
      LIMIT :count;


#### Получить все фильмы:
SELECT * FROM FILMS AS f LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID;
#### Получить фильм по id:
SELECT * FROM FILMS AS f LEFT JOIN MPA AS m ON  f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
WHERE f.FILM_ID = film_id;

#### Получить всех пользователей:
SELECT * FROM users;
#### Получить пользователя по id:
SELECT * FROM users WHERE user_id = id; SELECT *
#### Получить друзей определенного user-а по id:
SELECT * FROM user_friends WHERE user_id = id
#### Получить общих друзей пользователя user1 (user_id) с user2 по id (friend_id):
SELECT * FROM USERS WHERE USER_ID IN ( SELECT f.friend_id FROM USERS AS u LEFT JOIN FRIENDS AS f ON u.user_id = f.user_id
WHERE u.user_id = :user_id AND f.friend_id IN ( SELECT fr.friend_id FROM USERS AS us LEFT JOIN FRIENDS AS fr ON us.user_id = fr.user_id WHERE us.user_id = otherId

### Запросы к серверу:
#### Пользователи
* POST /users - создание пользователя
* PUT /users - редактирование пользователя
* GET /users - получение списка всех пользователей
* GET /users/{id} - получение информации о пользователе по его id
* PUT /users/{id}/friends/{friend-Id} — добавление в друзья
* DELETE /users/{id}/friends/{friend-Id} — удаление из друзей
* GET /users/{id}/friends — возвращает список пользователей, являющихся его друзьями
* GET /users/{id}/friends/common/{other-Id} — список друзей, общих с другим пользователем 
#### Фильмы
* POST /films - создание фильма
* PUT /films - редактирование фильма
* GET /films - получение списка всех фильмов
* PUT /films/{film-id}/like/{user-id} — пользователь ставит лайк фильму
* DELETE /films/{film-id}/like/{user-id} — пользователь удаляет лайк
* GET /films/popular — возвращает список из 10 самых популярных лайков
  
### Тестирование
  Тесты показывают, что программа работает на граничных условиях.









