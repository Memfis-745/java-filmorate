# java-filmorate
Template repository for Filmorate project.
Репозиторий шаблонов для проекта Filmorate.

БД состоит из нескольких таблиц, связанных между собой:

1. хранения пользователей (Users) 
2. хранения фильмов (Films) 
3. друзей (Friends) 
4. лайков к фильмам (FilmUserLikes) 
5. хранения рейтингов (Mpa) 
6. хранения жанров (Genre) 
7. таблицы-связки (Film-genre)

Запросы в БД:

Получить все фильмы: SELECT * FROM FILMS AS f LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID;
Получить фильм по id: SELECT * FROM FILMS AS f LEFT JOIN MPA AS m ON  f.MPA_ID = m.MPA_ID LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID
WHERE f.FILM_ID = film_id;

Получить всех пользователей: SELECT * FROM users;
Получить пользователя по id: SELECT * FROM users WHERE user_id = id; SELECT *
Получить друзей определенного user-а по id: SELECT * FROM user_friends WHERE user_id = id
Получить общих друзей пользователя user1 (user_id) с user2 по id (friend_id): SELECT * FROM USERS WHERE USER_ID IN ( SELECT f.friend_id FROM USERS AS u LEFT JOIN FRIENDS AS f ON u.user_id = f.user_id
WHERE u.user_id = :user_id AND f.friend_id IN ( SELECT fr.friend_id FROM USERS AS us LEFT JOIN FRIENDS AS fr ON us.user_id = fr.user_id WHERE us.user_id = otherId

![img.png](schema.pdf)

