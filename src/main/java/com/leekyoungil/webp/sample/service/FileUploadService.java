package com.leekyoungil.webp.sample.service;

import com.leekyoungil.webp.sample.config.Define;
import com.leekyoungil.webp.sample.model.ImageInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Kellin on 3/25/16.
 */
@Service
public class FileUploadService {

    /**
     * save file to remote storage.
     *
     * @param uploadFileObj
     * @return
     */
    public ImageInfo doUpload (MultipartFile uploadFileObj) {
        ImageInfo originImageInfo = new ImageInfo();

        try {
            // get the file extension from uploadFileObj.
            String fileExt = FilenameUtils.getExtension(uploadFileObj.getOriginalFilename());

            // only jpg, jpeg, png, bmp
            if (Arrays.asList(Define.ACCEPT_FILE_EXT).contains(fileExt.toLowerCase())) {
                Path emptyFilePath = createEmptyFile(Define.UPLOAD_ROOT_DIRECTORY, uploadFileObj.getOriginalFilename());

                Boolean fileCheck = (Files.exists(emptyFilePath) && Files.isWritable(emptyFilePath)) ? true : false;

                if (emptyFilePath != null && fileCheck) {
                    // copy file uploadFileObj to tmpFile
                    File emptyFile = transferFileToEmptyFile(emptyFilePath, uploadFileObj);

                    if (emptyFile != null && emptyFile.canRead()) {
                        originImageInfo.setFileName(emptyFile.getName());
                        originImageInfo.setFileSize(emptyFile.length());
                        originImageInfo.setFileUrl("/upload/"+emptyFile.getName());
                        originImageInfo.setStatus(true);
                        originImageInfo.setImageFile(emptyFile);
                    }
                }
            } else {
                // this file format is not supported. just image file only.
                originImageInfo.setStatus(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            originImageInfo.setStatus(false);
        }

        return originImageInfo;
    }

    /**
     * get real path of the file.
     *
     * @return
     */
    private String getUploadRootPath () {
        String absoluteFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        // change file path classes to resources.
        if (absoluteFilePath.indexOf("classes") > -1) {
            absoluteFilePath = absoluteFilePath.replace("classes", "resources");
        }

        absoluteFilePath += "/public/";

        return absoluteFilePath;
    }

    /**
     * create directory by value of directoryName
     * @param directoryName
     * @return
     */
    public Boolean createDirectory (String directoryName) {
        Path path = Paths.get(getUploadRootPath() + directoryName);

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }

        return true;
    }

    /**
     * create empty file
     * @param directoryName
     * @param fileName
     * @return
     */
    public Path createEmptyFile (String directoryName, String fileName) {
        Path filePath = null;

        try {
            Path path = Paths.get(getUploadRootPath() + directoryName);
            String extenstion = FilenameUtils.getExtension(fileName);

            if (extenstion != null && !extenstion.isEmpty()) {
                extenstion = "."+extenstion;
            }

            filePath = Files.createTempFile(path, "", extenstion);
        } catch (Exception ex) {
            ex.printStackTrace();
            filePath = null;
        }

        return filePath;
    }

    /**
     * transfer to emptyFile
     * @param emptyFilePath
     * @param originFile
     * @return
     */
    public File transferFileToEmptyFile (Path emptyFilePath, MultipartFile originFile) {
        File emptyFile = null;

        if (Files.exists(emptyFilePath)) {
            emptyFile = new File(emptyFilePath.toUri());

            if (emptyFile.canWrite()) {
                try {
                    originFile.transferTo(emptyFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    emptyFile = null;
                }
            }
        }

        return emptyFile;
    }
}
