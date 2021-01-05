package com.shopify.common.file;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopify.common.file.exception.FileDownloadException;
import com.shopify.common.file.exception.FileUploadException;
import com.shopify.common.util.UtilFn;
import com.shopify.config.FileUploadConfig;

@Service
public class FileUploadDownloadService {
	private final Path fileLocation;
	@Autowired private UtilFn util;
	@Autowired
	public FileUploadDownloadService(FileUploadConfig prop) {
		this.fileLocation = Paths.get(prop.getUploadDir())
				.toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(this.fileLocation);
		}catch(Exception e) {
			throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
		}
	}
	
	public String storeFile(MultipartFile file, int notiIdx) {
		String realName = (String.valueOf(notiIdx)+"_"+util.getUid()+"_"+String.valueOf(file.getSize()));
		try {
			if (file != null && realName != null) {
				if (realName.contains("..")) { // 파일명에 부적합 문자가 있는지 확인한다.
					throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + realName) ;						
				}
				Path targetLocation = this.fileLocation.resolve(realName) ;
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING) ;
			}
			return realName;
		} catch(Exception e) {
			throw new FileUploadException("["+realName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e) ;
		}
	}
	
	public String storeFile(MultipartFile file) {
		String fileName = null;
		
		try {
			if(file != null) {
				fileName = StringUtils.cleanPath(file.getOriginalFilename());
			}
			if(fileName != null) {
				// 파일명에 부적합 문자가 있는지 확인한다.
				if(fileName.contains(".."))
					throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
				
				Path targetLocation = this.fileLocation.resolve(fileName);
				
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			}
			return fileName;
		}catch(Exception e) {
			throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
		}
	}
	
	public Path storeFilePath(MultipartFile file) {
		Path targetLocation  = null;
		if(file != null) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			targetLocation = this.fileLocation.resolve(fileName);
		}
		
		return targetLocation;
			
	}
	
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			
			if(resource.exists()) {
				return resource;
			}else {
				throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.");
			}
		}catch(MalformedURLException e) {
			throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.", e);
		}
	}

}