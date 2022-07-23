package com.fms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.fms.model.ExcelFile;
import com.fms.service.FileOperationsService;
import com.fms.utils.APIJSONInput;
import com.fms.views.FileOperationsView.FileOperationsBasicView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
public class FileController {
	
	@Autowired
	private FileOperationsService fileOperationsService;
	
	@ApiOperation(value = "Upload xls and xlsx file to database", notes = APIJSONInput.API_FILE_UPLOAD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ExcelFile uploaded and data saved to database."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@PostMapping("/upload/excelFile")
	public ResponseEntity<String> uploadExcelAttachment(@RequestParam("file") MultipartFile file,@RequestParam("filePath") String filePath) {
		
		ExcelFile fileUploaded = fileOperationsService.saveFileAttachment(file,filePath);
		return new ResponseEntity<>("ExcelFile uploaded and data saved to database with id:"+fileUploaded.getId(),HttpStatus.OK);
	}

	@ApiOperation(value = "List all the uploaded excel files.", notes = APIJSONInput.API_LIST_FILES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetched the list of uploaded files."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(FileOperationsBasicView.class)
	@GetMapping("/files")
	public List<ExcelFile> fetchAllUploadedExcelAttachments(){
		
		return fileOperationsService.fetchAllUploadedExcelAttachments();
	}
	
	@ApiOperation(value = "List the content of specific file.", notes = APIJSONInput.API_FILE_REVIEW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetched the content of specific file."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(FileOperationsBasicView.class)
	@GetMapping("/files/{fileId}/review")
	public String reviewRecordsOfFile(@PathVariable("fileId")Long fileId) {
		return fileOperationsService.reviewRecordsOfFile(fileId);
	}
	
	@ApiOperation(value = "Delete specific file.", notes = APIJSONInput.API_FILE_DELETE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@DeleteMapping("/files/file")
	public void deleteFile(@RequestParam("filePath") String filePath){
		fileOperationsService.deleteFileDetails(filePath);
	}
	
	@ApiOperation(value = "Status of specific file upload.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ExcelFile Uploaded."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(FileOperationsBasicView.class)
	@GetMapping("/files/{fileId}")
	public ExcelFile checkProgressOfFile(@PathVariable("fileId") Long fileId) {
		return fileOperationsService.checkProgressOfFile(fileId) ;
	}
}
