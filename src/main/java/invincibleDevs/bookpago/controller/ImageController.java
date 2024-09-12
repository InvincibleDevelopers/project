package invincibleDevs.bookpago.controller;

import invincibleDevs.bookpago.model.Image;
import invincibleDevs.bookpago.repository.ImageRepository;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class ImageController {
    private final ImageRepository imageRepository;
//    private final Image image;

    @ApiOperation(value = "사진등록")
    @GetMapping("/test")
    public String state(){

        return "success";
    }

    @ApiOperation(value = "사진등록")
    @PostMapping("/image")
    public String state(@RequestBody Image image){

//        System.out.println(imgUrl);
//
//        Image image = new Image();
//        image.setImageUrl(imgUrl);
        System.out.println("--===========================================");
        System.out.println(image.getImageUrl());
        imageRepository.save(image);


        return "success";
    }
    @ApiOperation(value = "사진등록")
    @PostMapping("/upload")
    public String state(@RequestParam MultipartFile multipartFile){

        // 파일 처리 로직
        String filePath = "/path/to/save/" + multipartFile.getOriginalFilename();
//        multipartFile.transferTo(new multipartFile(filePath));

        // 파일 URL 반환
        String imageUrl = "https://your-domain.com/images/" + multipartFile.getOriginalFilename();
//        return ResponseEntity.ok("{ \"imageUrl\": \"" + imageUrl + "\" }");


        return "success";
    }

    @GetMapping("/")
    public String test() {
        return "hello world";
    }


}
