package com.fms.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FormatChecker {

	public boolean excelFormatChecker(MultipartFile file) {
		if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				|| file.getContentType().equals("application/vnd.ms-excel.sheet.binary.macroEnabled.12")
				|| file.getContentType().equals("application/vnd.ms-excel")
				|| file.getContentType().equals("application/vnd.ms-excel.sheet.macroEnabled.12")) {
			return true;
		} else {
			return false;
		}
	}
}
