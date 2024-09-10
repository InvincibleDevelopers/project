package invincibleDevs.bookpago.controller;

import invincibleDevs.bookpago.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/testupload")
@RequiredArgsConstructor
public class FileUploadController {
    private final S3Service s3Service;

    @PostMapping
    public String uploadtoS3(@RequestPart("file")MultipartFile file) throws IOException {
        String url  = s3Service.uploadFile(file);
        return url;
    }

//    @GetMapping("/getimage/{photoId}")
//    public String getimage(@PathVariable("photoId") Long photoId) {
//        String url = s3Service.getFile(photoId);
//        return url;
//    }

}
