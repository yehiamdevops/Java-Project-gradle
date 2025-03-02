package org.example;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageUtils {
    public static Image loadCorrectedImage(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        int rotation = getRotation(file);

        if (rotation != 0) {
            bufferedImage = rotateImage(bufferedImage, rotation);
        }

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private static int getRotation(File file) {
        try {
            // Read EXIF metadata
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                return switch (orientation) {
                    case 3 -> 180;
                    case 6 -> 90;
                    case 8 -> 270;
                    default -> 0;
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // No rotation needed
    }

    private static BufferedImage rotateImage(BufferedImage img, int angle) {
        int w = img.getWidth();
        int h = img.getHeight();
        AffineTransform transform = new AffineTransform();

        if (angle == 90) {
            transform.translate(h, 0);
        } else if (angle == 180) {
            transform.translate(w, h);
        } else if (angle == 270) {
            transform.translate(0, w);
        }

        transform.rotate(Math.toRadians(angle));
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(img, null);
    }
}
