package com.fms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.fms.views.FileOperationsView.FileOperationsBasicView;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "files")
public class ExcelFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(FileOperationsBasicView.class)
	private Long id;
	
	@JsonView(FileOperationsBasicView.class)
	private String name;
	
	private String type;
	
	private String path;
	
	private String[] data;
	
	@JsonView(FileOperationsBasicView.class)
	private Date lastAccessedOn;
	
	private String lastAccesedBy;

}
