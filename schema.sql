create table Album (id integer not null auto_increment, albumTitle varchar(255), label varchar(255), releaseDate date, primary key (id))
create table Musician (id integer not null auto_increment, Name varchar(255), primary key (id))
create table Song (id integer not null auto_increment, albumId integer not null, genre varchar(255), songLength integer not null, songTitle varchar(255), primary key (id))
