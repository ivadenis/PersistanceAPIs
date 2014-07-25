create table Album (id integer not null auto_increment, albumTitle varchar(255), label varchar(255), releaseDate date, primary key (id))
create table Musician (id integer not null auto_increment, Name varchar(255), primary key (id))
create table Song (id integer not null auto_increment, genre varchar(255), songLength integer not null, songTitle varchar(255), album_id integer, primary key (id))
create table album_musician (album_id integer not null, musician_id integer not null)
alter table Song add constraint FK_gdku75qsqamvm22yckhicp180 foreign key (album_id) references Album (id)
alter table album_musician add constraint FK_t24ek9bv0lcq0m2fe6incvdd foreign key (musician_id) references Musician (id)
alter table album_musician add constraint FK_m70ccrud30jq4pi8r17p266cp foreign key (album_id) references Album (id)
