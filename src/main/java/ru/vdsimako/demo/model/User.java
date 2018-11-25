package ru.vdsimako.demo.model;


import lombok.*;
import ru.vdsimako.demo.model.enums.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private List<Request> requestList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userStatus=" + userStatus +
                ", requestList=" + requestList +
                '}';
    }
}
