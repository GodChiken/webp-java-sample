package com.leekyoungil.webp.sample.controller;

import com.leekyoungil.webp.sample.config.Define;
import com.leekyoungil.webp.sample.model.ImageInfo;
import com.leekyoungil.webp.sample.model.ResponseFileUploadResult;
import com.leekyoungil.webp.sample.param.UploadFormParam;
import com.leekyoungil.webp.sample.service.FileUploadService;
import com.leekyoungil.webp.sample.service.WebpConvertService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kellin on 3/18/16.
 */
@RestController
public class FileUploadController {

    @Inject
    private FileUploadService fileUploadService;
    @Inject
    private WebpConvertService webpConvertService;

    @RequestMapping(value = "/doUpload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseFileUploadResult uploadFile (@ModelAttribute UploadFormParam uploadFormParam) {
        final ResponseFileUploadResult responseFileUploadResult = new ResponseFileUploadResult();

        if (uploadFormParam.getImageFile().size() > 0) {
            // the tmp directory create.
            Boolean directoryIsCreated = fileUploadService.createDirectory(Define.UPLOAD_ROOT_DIRECTORY);

            if (directoryIsCreated) {
                uploadFormParam.getImageFile().stream().parallel().forEach(fileObj -> {
                    CopyOnWriteArrayList<ImageInfo> imageInfos = new CopyOnWriteArrayList<>();

                    // basic image upload
                    ImageInfo originImageInfo = fileUploadService.doUpload(fileObj);

                    if (originImageInfo.getStatus()) {
                        imageInfos.add(originImageInfo);

                        // do WebP
                        CopyOnWriteArrayList<ImageInfo> webPImages = webpConvertService.doUploadWebP(originImageInfo.getImageFile());

                        if (webPImages.size() > 0) {
                            webPImages.stream().parallel().forEach(imageInfo -> {
                                imageInfos.add(imageInfo);
                            });
                        }
                    }

                    responseFileUploadResult.getResultData().put(fileObj.getOriginalFilename(), imageInfos);
                });
            }
        } else {
            responseFileUploadResult.setStatus(500);
            responseFileUploadResult.setMessage("Please check the file of Your request.");
        }

        return responseFileUploadResult;
    }
}
