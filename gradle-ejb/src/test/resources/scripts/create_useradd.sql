create table useradd (
    id int not null,
    firstname varchar(20),
    lastname  varchar(20),
    username varchar(20),
    password varchar(20),
    nickname varchar(20) null,
    primary key(id)
);
