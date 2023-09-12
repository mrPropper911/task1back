create table users
    (
        id bigint not null auto_increment,
        name varchar(20),
        surname varchar(20),
        email varchar(255) NOT NULL UNIQUE,
        uri_image varchar(255),
        timestamp timestamp,
        active bool,
        primary key (id)
    );
create table file_data
    (
        id bigint not null auto_increment,
        name varchar(255),
        type varchar(255),
        file_path varchar(255),
        primary key (id)
    );
