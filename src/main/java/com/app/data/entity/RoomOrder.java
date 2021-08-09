package com.app.data.entity;

import com.app.data.entity.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "room_order")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class RoomOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "check_in_date")
    private Date cInDate;

    @Column(name = "check_out_date")
    private Date cOutDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;


}
