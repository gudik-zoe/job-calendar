package com.work.calendar.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
	private Logger log = LoggerFactory.getLogger(EmailSender.class);
	
	 @Autowired
	 private JavaMailSender javaMailSender;
	 
	  public void sendEmailWithAttachment( String email , String subject , String text , InputStreamSource  data) throws MessagingException {
	        try {
	            MimeMessage msg = javaMailSender.createMimeMessage();
	          MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	          helper.setTo(email);
	          helper.setFrom("tonykhoury@gmail.com");
	          helper.setSubject(subject);
	          helper.setText(text);
	          helper.addAttachment("test.xls", data);
	          javaMailSender.send(msg);
	      }catch (Exception e){
	        throw new InternalError("cannot send email with attachment " + e);
	      }
	    }
	  
//	  public InputStreamSource createPdfDocumentToBeAttached(String text) throws DocumentException {
//	       ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	       Document document = new Document(PageSize.A4);
//	       PdfWriter.getInstance(document , baos);
//	       document.open();
//	       document.add(new Paragraph("you have added a new post" + "\n"  + "postText: " + text ));
//	       document.close();
//	        InputStreamSource attachment = new ByteArrayResource(baos.toByteArray());
//	       return attachment;
//
//	   }
	  
	  private ByteArrayOutputStream createInMemoryDocument(byte[] data) throws IOException {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        outputStream.write(data);
	        return outputStream;
	    }

}
