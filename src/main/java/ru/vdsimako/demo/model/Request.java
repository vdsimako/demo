package ru.vdsimako.demo.model;

import lombok.*;
import ru.vdsimako.demo.model.enums.RequestStatus;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "requests")
public class Request implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "request_id")
    private Long id;

    @Column(name = "req_name")
    private String requestName;

    @Column(name = "req_desc")
    private String requestDesc;

    @Column(name = "user_id")
    private Long responsibleUser;

    @Column(name = "req_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
