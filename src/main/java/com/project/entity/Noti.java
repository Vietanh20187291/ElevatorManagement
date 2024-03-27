package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "noti")
public class Noti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient", referencedColumnName = "user_id")
    private User recipient;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Điều chỉnh định dạng ngày giờ
    private Date timestamp;

    @Column(name = "unread")
    private boolean unread;
    public Noti() {
    }

    public Noti(User recipient, String content, Date timestamp) {
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
    }

}
