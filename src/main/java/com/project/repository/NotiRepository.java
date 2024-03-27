package com.project.repository;
import com.project.entity.Noti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotiRepository extends JpaRepository<Noti, Integer> {
    @Query("SELECT n FROM Noti n WHERE n.recipient.id = :recipientId")
    List<Noti> findAllByRecipientId(int recipientId);

    @Query("UPDATE Noti n SET n.unread = false WHERE n.recipient.id = :recipientId")
    @Modifying
    @Transactional
    void markAllAsReadByRecipientId(int recipientId);
}

