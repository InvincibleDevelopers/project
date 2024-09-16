package invincibleDevs.bookpago.common;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class S3Service {
        private final AmazonS3 s3Client;

        private final S3Presigner s3Presigner;
//    public S3Service(AmazonS3Client s3Client,S3Presigner s3Presigner){
//        this.s3Client = s3Client;
//        this.s3Presigner = s3Presigner;
//    }
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String defaultUrl = "https://s3.amazonaws.com/";

    // 다운로드를 위한 Pre-signed URL 생성 메서드
    public String generatePreSignedUrl(String bucketName, String fileName) {
        // S3 객체에 대한 GET 요청 생성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        // Pre-signed URL 요청 생성 (유효기간 설정)
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofDays(1))
                .build();

        //Pre-signed URL 생성
        String preSignedUrl = s3Presigner.presignGetObject(presignRequest).url().toString();
        return preSignedUrl;
    }

    public String uploadFile(MultipartFile file) throws IOException { //
        String fileName = generateFileName(file);
        try {
            String presignedUrl = generatePreSignedUrl(bucketName,fileName);
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            //위에 주소는 뭔지?
            return presignedUrl;
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
