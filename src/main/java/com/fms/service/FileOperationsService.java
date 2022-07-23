package com.fms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fms.model.ExcelFile;

public interface FileOperationsService {


	List<ExcelFile> fetchAllUploadedExcelAttachments();

	String reviewRecordsOfFile(Long fileId);


	ExcelFile checkProgressOfFile(Long fileId);

	ExcelFile saveFileAttachment(MultipartFile file, String filepath);
	void deleteFileDetails(String filePath);

}
