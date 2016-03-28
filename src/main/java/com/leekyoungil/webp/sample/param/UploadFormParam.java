package com.leekyoungil.webp.sample.param;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kellin on 3/25/16.
 */
public class UploadFormParam {

    @NotNull
    List<MultipartFile> imageFile;

    public List<MultipartFile> getImageFile() {
        // to avoid NullPointException.
        if (imageFile == null) {
            this.imageFile = new ArrayList<>();
        }

        return imageFile;
    }

    public void setImageFile(List<MultipartFile> imageFile) {
        this.imageFile = imageFile;
    }
}
