package com.example.intentbi.repository;

import com.example.intentbi.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Data, Long> {}
