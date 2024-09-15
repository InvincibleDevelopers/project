package invincibleDevs.bookpago.common;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String defaultUrl = "https://s3.amazonaws.com/";

    public String uploadFile(MultipartFile file) throws IOException { //
        String fileName = generateFileName(file);
        try {
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            return defaultUrl + fileName;
        } catch(SdkClientException e) {
            throw new IOException("Error uploading file to S3", e);
        }
    }

    public void deleteFile(String fileUrl) {
        String deleteUrl = extractFileKeyFromUrl(fileUrl);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, deleteUrl));
    }


    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private String generateFileName(MultipartFile file) {

        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }

    //URL에서 파일 키를 추출하는 메서드
    private String extractFileKeyFromUrl(String fileUrl) {
        String s3Prefix = "https://s3.amazonaws.com/";
        return fileUrl.substring(s3Prefix.length());
    }

}
