package com.example.ddo_pay.common.config.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// 외부 설정에서 prefix를 받아오도록 (기본값은 "uploads")
	@Value("${cloud.aws.s3.prefix:uploads}")
	private String prefix;

	@PostMapping("/multiple")
	public ResponseEntity<?> uploadMultipleFiles(@RequestParam("file") List<MultipartFile> files) {
		List<String> successUrls = new ArrayList<>();
		List<String> failedFiles = new ArrayList<>();

		for (MultipartFile file : files) {
			try {
				// 원본 파일명을 가져와서 UUID를 붙여 고유화
				String originalFileName = file.getOriginalFilename();
				String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(file.getContentType());
				metadata.setContentLength(file.getSize());

				// 외부에서 주입받은 prefix를 사용하여 S3 key를 생성
				String s3Key = prefix + "/" + uniqueFileName;
				amazonS3Client.putObject(bucket, s3Key, file.getInputStream(), metadata);

				String fileUrl = amazonS3Client.getUrl(bucket, s3Key).toString();
				successUrls.add(fileUrl);

			} catch (IOException e) {
				e.printStackTrace();
				failedFiles.add(file.getOriginalFilename());
			}
		}

		// 일부 파일이라도 실패했으면 성공/실패 내역 함께 반환
		if (!failedFiles.isEmpty()) {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.body("성공한 파일: " + successUrls + ", 실패한 파일: " + failedFiles);
		}
		return ResponseEntity.ok(successUrls);
	}
}