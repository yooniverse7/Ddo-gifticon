package com.example.ddo_pay.common.config.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// 외부 설정에서 prefix를 받아오도록 (기본값은 "uploads")
	@Value("${cloud.aws.s3.prefix:uploads}")
	private String prefix;

	public String uploadFile(MultipartFile file) throws IOException {
		String originalFileName = file.getOriginalFilename();
		String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());

		String s3Key = prefix + "/" + uniqueFileName;
		amazonS3Client.putObject(bucket, s3Key, file.getInputStream(), metadata);
		return amazonS3Client.getUrl(bucket, s3Key).toString();
	}
}
