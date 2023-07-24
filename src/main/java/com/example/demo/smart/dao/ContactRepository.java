package com.example.demo.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.smart.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    // Pagination....

    @Query("from Contact as c where c.user.id = :userId")
    //  pageable hase two things....
    // 1->currentPage - page
    // 2->Contact per page - 5
    public Page<Contact> findContactByUser(@Param("userId") int UserId,Pageable pageable);
}
