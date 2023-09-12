DELETE FROM users WHERE users.id>0;
ALTER TABLE users AUTO_INCREMENT=1;
INSERT INTO users(id, name, surname, email, uri_image, active)
VALUES (1, 'Vadia', 'Belynovcki',  'vadia123bel@gmail', 'http://localhost:8080/api/v1/images/1694345683445-photo_2023-01-14_21-54-46.jpg',true),
       (2, 'Veron', 'Cherepash', 'veron11lose@mail.ru', 'http://localhost:8080/api/v1/images/1694345683445-photo_2023-01-14_21-54-46.jpg',false),
       (3, 'Igor', 'Lihten', 'igoridze@gmail.com', null,true),
       (4, 'Sergey', 'Pogorelov',  'sergeygey@gmail.com', null,false);