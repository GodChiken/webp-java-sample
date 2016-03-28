package com.leekyoungil.webp.sample.service;

import com.leekyoungil.webp.sample.config.Define;
import com.leekyoungil.webp.sample.model.ImageInfo;
import com.luciad.imageio.webp.WebPWriteParam;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Kellin on 3/26/16.
 */
@Service
public class WebpConvertService {

    /**
     * convert and save webP file to remote storage.
     * @param imageFile
     * @return
     */
    public CopyOnWriteArrayList<ImageInfo> doUploadWebP (File imageFile) {
        CopyOnWriteArrayList<ImageInfo> imageInfos = new CopyOnWriteArrayList<>();

        Arrays.asList(Define.WEBP_TYPE).stream().parallel().forEach(compressType -> {
            File webpFileTmp = convertToWebp(imageFile, compressType);

            if (webpFileTmp != null && webpFileTmp.canRead()) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setFileName(webpFileTmp.getName());
                imageInfo.setFileSize(webpFileTmp.length());
                imageInfo.setFileUrl("/upload/"+ webpFileTmp.getName());
                imageInfo.setStatus(true);
                imageInfo.setImageFile(webpFileTmp);

                imageInfos.add(imageInfo);
            }
        });

        return imageInfos;
    }

    /**
     * The "lossy" compression type.
     * LOSSY_COMPRESSION_TYPE = Lossy
     * The "lossless" compression type.
     * LOSSLESS_COMPRESSION_TYPE = Lossless
     */

    /**
     * convert to webP
     *
     * @param oldFile
     * @param compressionType
     * @return
     */
    private File convertToWebp (File oldFile, String compressionType) {
        File webpFile = null;

        // check the file can read.
        if (oldFile.canRead()) {
            // chek compressionType
            if ("Lossy".equals(compressionType) || "Lossless".equals(compressionType)) {
                String[] newFilePathTmpArray = oldFile.getAbsolutePath().split("\\.");
                newFilePathTmpArray[newFilePathTmpArray.length-2] += "_"+compressionType;
                newFilePathTmpArray[newFilePathTmpArray.length-1] = "webp";

                try {
                    RenderedImage renderedImage = loadImage(oldFile.getAbsolutePath());

                    if (imageWriterCompression(renderedImage, compressionType, 1F, String.join(".", newFilePathTmpArray))) {
                        webpFile = new File(String.join(".", newFilePathTmpArray));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    webpFile = null;
                }

                return webpFile;
            }
        }

        return webpFile;
    }

    /**
     * create RenderedImage object using real path of the file.
     *
     * @param name
     * @return
     * @throws IOException
     */
    private RenderedImage loadImage(String name) throws IOException {
        return ImageIO.read(new File(name));
    }

    /**
     * @param outputName
     * @return
     */
    private File createOutputFile(final String outputName) {
        return new File(outputName);
    }

    /**
     * compress the files.
     *
     * @param im
     * @param compressionType
     * @param compressionQuality
     * @param outputName
     * @return Boolean
     * @throws IOException
     */
    private Boolean imageWriterCompression(RenderedImage im, String compressionType, float compressionQuality, String outputName) throws IOException {
        Boolean result = true;

        String extension = FilenameUtils.getExtension(outputName);

        ImageWriter imgWriter = null;
        ImageWriteParam imgWriteParams = null;

        try {
            // if the error occurred in this line. you have to check the 'webp' native library is loaded.
            imgWriter = ImageIO.getImageWritersByFormatName(extension).next();
            // set to compression quality.
            imgWriteParams = new WebPWriteParam(null);
            imgWriteParams.setCompressionType(compressionType);
            imgWriteParams.setCompressionQuality(compressionQuality);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }

        if (imgWriter != null && imgWriteParams != null) {
            ImageOutputStream imageOutputStream = null;

            try {
                File file = createOutputFile(outputName);
                imageOutputStream = ImageIO.createImageOutputStream(file);

                imgWriter.setOutput(imageOutputStream);
                imgWriter.write(null, new IIOImage(im, null, null), imgWriteParams);
            } catch (Exception ex) {
                ex.printStackTrace();
                result = false;
            } finally {
                try {
                    if (imageOutputStream != null) {
                        imageOutputStream.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }

        return result;
    }
}
