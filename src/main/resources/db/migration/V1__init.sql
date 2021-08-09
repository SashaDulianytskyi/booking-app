create table client
(
    id           bigint primary key
        auto_increment        not null,
    name         varchar(255) not null,
    last_name    varchar(255),
    phone_number varchar(15)  not null,
    constraint UK_phone_number unique (phone_number)
);

create table room
(
    id           bigint primary key
        auto_increment       not null,
    number       int         not null,
    total_places integer     not null,
    price        integer     not null,
    type         varchar(25) not null,
    renter_id    bigint,
    constraint client_id
        foreign key (renter_id) references client (id),
    UNIQUE (number)
);

create table room_order
(
    id             bigint primary key
        auto_increment         not null,
    payment_method varchar(25) not null,
    cIn_date       date        not null,
    cOut_date      date        not null,
    client_id      bigint      not null,
    room_id        bigint      not null,
    constraint UK_client_id
        foreign key (client_id) references client (id),
    constraint UK_room_id
        foreign key (room_id) references room (id)

)