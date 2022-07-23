package com.fms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fms.model.ExcelFile;

@Repository
public interface FileOperationsRepository extends JpaRepository<ExcelFile,Long>{
	
	@Transactional
	@Query(value = "select * from files f where f.path = :filePath", nativeQuery = true)
	List<ExcelFile> findByFileName(@Param("filePath") String filePath);
}
