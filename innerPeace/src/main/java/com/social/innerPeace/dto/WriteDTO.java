package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriteDTO {
    private float post_map_lat;
    private float post_map_lng;
    private String post_image_thumbnail;
    private MultipartFile post_image;
    private String post_writer;
    private String post_content;
    private String post_tags;
}
