package com.sboard.repository;

import com.sboard.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {

    public Optional<List<FileEntity>> findByAno(int Ano);

}
