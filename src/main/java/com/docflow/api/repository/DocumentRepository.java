package com.docflow.api.repository;

import com.docflow.api.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository <Document, Long> {
}
