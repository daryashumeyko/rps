CREATE TABLE lab02.owner (
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name char(50) NOT NULL,
  address char(50)
);

create table lab02.pet (
  id           int auto_increment primary key,
  name         char(25) not null,
  birthDate    date NOT NULL,
  type         char(25) not null,
  pet_owner_id int      not null,
  constraint pet_owner_id
  foreign key (pet_owner_id) references owner (id)
    on delete cascade
);