package com.app.data.entity;

import com.app.data.entity.enums.RoomType;
import com.app.data.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "room")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "number", unique = true)
    private Integer number;

    @Column(name = "total_places")
    private Integer totalPlaces;

    @Column(name = "price")
    private Integer price;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    @JsonIgnore
    private Client renter;

    @OneToOne(mappedBy = "room")
    @JsonIgnore
    private RoomOrder roomOrder;


}
