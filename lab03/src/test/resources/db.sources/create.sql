CREATE TABLE owner
(
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name char(50) NOT NULL,
  birthDate date NOT NULL,
  address char(50),
  iq int NOT NULL
);

create table pet
(
  id           int auto_increment
    primary key,
  name         char(25) not null,
  age          int      not null,
  type         char(25) not null,
  pet_owner_id int      not null,
  constraint pet_owner_id
  foreign key (pet_owner_id) references owner (id)
    on delete cascade
);