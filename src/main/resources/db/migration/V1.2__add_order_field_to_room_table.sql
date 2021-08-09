alter table room
    add order_id bigint,
    add constraint room_order_id foreign key (order_id) references room_order (id);
