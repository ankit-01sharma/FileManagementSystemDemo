package com.fms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.fms.model.ExcelFile;
import com.fms.repository.FileOperationsRepository;
import com.fms.utils.FormatChecker;

@Service
public class FileOperationsServiceImpl implements FileOperationsService {
	
	@Autowired
	private FileOperationsRepository fileOperationsRepo;
	
	@Autowired
	FormatChecker formatChecker;

	private String fileContents[];
	
	@Override
	public ExcelFile saveFileAttachment(MultipartFile file,String filepath) {
	
		try (Workbook workBook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workBook.getSheetAt(0);
			fileContents = new String[sheet.getLastRowNum()+1];
			for (int r = 0; r <= sheet.getLastRowNum(); r++) {	
				Row row = sheet.getRow(r);
				if(row !=null) {
					List<String> fileRecord = new ArrayList<>();
					for (int c = 0; c < row.getLastCellNum(); c++) {
						Cell cell = row.getCell(c);
						switch (cell.getCellType()) {
						case STRING:
							if(cell.getStringCellValue() != null) {
								fileRecord.add(cell.getStringCellValue());
							}	
							break;
						case NUMERIC:
							Long longValue = (long)cell.getNumericCellValue();
							if(longValue != null) {
								fileRecord.add(longValue.toString());
							}
							break;
						case BOOLEAN:
							Boolean booleanValue = cell.getBooleanCellValue();
							fileRecord.add(booleanValue.toString());
							break;
						default:
							break;	
							
						}
					}
					fileContents[r]=fileRecord.toString();
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ExcelFile excelFile = new ExcelFile();
		if (formatChecker.excelFormatChecker(file)) {	
			excelFile.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			excelFile.setType(file.getContentType());
			excelFile.setPath(filepath+StringUtils.cleanPath(file.getOriginalFilename()));
			excelFile.setData(fileContents);
			excelFile.setLastAccessedOn(new Date(System.currentTimeMillis()));
		} else {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Please upload excel file only");
		}
		return fileOperationsRepo.save(excelFile);
	}
	
	@Override
	public List<ExcelFile> fetchAllUploadedExcelAttachments(){
		return fileOperationsRepo.findAll();
	}
	
	@Override
	public ExcelFile checkProgressOfFile(Long fileId){
		return fileOperationsRepo.findById(fileId)
				.orElseThrow(()->{throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ExcelFile not Uploaded");});
	}
	
	@Override
	public String reviewRecordsOfFile(Long fileId) {
		ExcelFile excelFile= fileOperationsRepo.findById(fileId)
				.orElseThrow(()->{throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ExcelFile not found");});
		excelFile.setLastAccessedOn(new Date(System.currentTimeMillis()));
		fileOperationsRepo.save(excelFile);
		List<String> content = new ArrayList<>();
		for(String row: excelFile.getData()) {
			content.add(row);
		}
		return content.toString();	
	}
	
	@Override
	public void deleteFileDetails(String filePath) {
		try {
			Files.delete(Paths.get(filePath));
			List<ExcelFile> filesToDelete = fileOperationsRepo.findByFileName(filePath);
			if(filesToDelete!=null) {
				fileOperationsRepo.deleteAll(filesToDelete);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
