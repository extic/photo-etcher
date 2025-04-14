package org.levinson.photoetcher.server;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PhotoService {

    public String handlePhoto(MultipartFile file) {
        var fileContent = getFileContent(file);
        var latLongDate = extractCoordinates(fileContent);
        return patchImage(file, latLongDate);
    }

    public byte[] getFileContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert file to byte array: " + e.getMessage(), e);
        }
    }

    private LatLongDate extractCoordinates(byte[] fileContent) {
        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            var dateTaken = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class).stream()
                    .findFirst()
                    .map(ExifSubIFDDirectory::getDateOriginal)
                    .orElse(null);

            return metadata.getDirectoriesOfType(GpsDirectory.class).stream()
                    .findFirst()
                    .map(GpsDirectory::getGeoLocation)
                    .map(geoLocation -> new LatLongDate(
                            geoLocation.getLatitude(),
                            geoLocation.getLongitude(),
                            dateTaken
                    ))
                    .orElseThrow(() -> new RuntimeException("No GPS data found in the file."));
        } catch (Exception e) {
            throw new RuntimeException("Error processing file: " + e.getMessage());
        }
    }

    private String patchImage(MultipartFile file, LatLongDate latLongDate) {
        try {
            // Load the image from the file content
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            BufferedImage image = ImageIO.read(inputStream);

            if (image == null) {
                throw new RuntimeException("Failed to load image from file.");
            }

            // Create a graphics context
            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set font and color
            graphics.setFont(new Font("Arial", Font.BOLD, 20));
            graphics.setColor(Color.RED);

            // Add text to the image
            String text = latLongDate.toString();
            int x = 10; // X-coordinate for the text
            int y = image.getHeight() - 10; // Y-coordinate for the text
            graphics.drawString(text, x, y);

            // Dispose of the graphics context
            graphics.dispose();

            // Save the modified image to a temporary file
            File tempFile = File.createTempFile("patched-image-", ".jpg");
            ImageIO.write(image, "jpg", tempFile);

            // Return the path to the temporary file
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to patch image: " + e.getMessage(), e);
        }
    }

    private record LatLongDate(double latitude, double longitude, Date date) {

        @Override
        public String toString() {
            return "Latitude: " + latitude + ", Longitude: " + longitude + ", Date: " + date;
        }
    }
}
