package com.work.calendar.dto;

public class Base64DTO {
	
	 private  String fileName;

     private String fileInBase64;

     private String fileType;

     public Base64DTO(){

     }

 public Base64DTO(String fileName, String fileInBase64, String fileType) {
     this.fileName = fileName;
     this.fileInBase64 = fileInBase64;
     this.fileType = fileType;
 }

 public String getFileName() {
     return fileName;
 }

 public void setFileName(String fileName) {
     this.fileName = fileName;
 }

 public String getFileInBase64() {
     return fileInBase64;
 }

 public void setFileInBase64(String fileInBase64) {
     this.fileInBase64 = fileInBase64;
 }

 public String getFileType() {
     return fileType;
 }

 public void setFileType(String fileType) {
     this.fileType = fileType;
 }

}
