create table Player (id integer not null auto_increment, first_name varchar(255), jerseyNumber integer not null, last_name varchar(50) not null, team_id integer not null, primary key (id))
create table Team (id integer not null auto_increment, league varchar(255), team_name varchar(100) not null, primary key (id))
alter table Team add constraint UK_fpgo4ifc5uc2i3wjbv1k4cyr2  unique (team_name)
alter table Player add constraint FK_2w0ldfd2ytqf3gkh4q436fw5i foreign key (team_id) references Team (id)
