create table roles (
                       id serial primary key not null,
                       name text
);

create table room (
                      id serial primary key not null,
                      name text
);

create table person (
                        id serial primary key not null,
                        name text,
                        email text,
                        password text,
                        role_id int references roles(id)
);

create table messages (
                          id serial primary key not null,
                          body text,
                          person_id int references person(id),
                          room_id int references room(id)
);


insert into roles (name) values ('user');
insert into roles (name) values ('admin');
insert into room (name) values ('Main');
insert into room (name) values ('Second');
insert into person (name, email, password, role_id) values ('Oleg', 'sample@com', '123', 1);
insert into person (name, email, password, role_id) values ('Vasiliy', 'sample@com', '123', 1);
insert into messages(body, person_id, room_id) values ('Всем привет', 1, 1);