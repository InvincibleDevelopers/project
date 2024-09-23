package invincibleDevs.bookpago.common;

import invincibleDevs.bookpago.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")

public class S3Controller {

    private final S3Service s3Service;
    private final ImageService imageService;

    @PostMapping("/upload")
    @Operation(summary = "Upload file to S3", description = "Uploads a file to S3 and returns the URL and FileKey")
    public ResponseEntity<String> uploadtoS3(@RequestPart("file") MultipartFile file) {
        try {
            String url = s3Service.uploadFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/getimage/{fileKey}")
    @Operation(summary = "Get image by fileKey", description = "Retrieves the image URL from S3 using the provided fileKey")
    public ResponseEntity<String> getimage(@PathVariable("fileKey") String fileKey) {
        try {
//            String url = imageService.getImage(fileKey);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Image not found for fileKey: " + fileKey);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Image not found for fileKey: " + fileKey);
        }
    }

}
