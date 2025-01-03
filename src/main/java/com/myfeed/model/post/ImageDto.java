package com.myfeed.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {
    private String imageSrc;

    public ImageDto(Image image) {
        this.imageSrc = image.getImageSrc();
    }
}
