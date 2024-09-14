package invincibleDevs.bookpago.image;

import invincibleDevs.bookpago.image.Image;
import invincibleDevs.bookpago.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    public String addImage(String imageUrl){
        Image image = new Image();
        image.setImageUrl(imageUrl);

        int index = imageUrl.indexOf("_");
        String fileKey = imageUrl.substring(25,index);

        image.setImageKey(fileKey);

        imageRepository.save(image);
        return image.getImageKey();
    }

    public String getImage(String fileKey) {
        Optional<Image> image = imageRepository.findByImageKey(fileKey);
        return image.map(Image::getImageUrl)
                .orElseThrow(() -> new NoSuchElementException("No Photo"));
    }
}
