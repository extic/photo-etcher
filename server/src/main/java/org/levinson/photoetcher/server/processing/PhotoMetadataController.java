package org.levinson.photoetcher.server.processing;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import static org.levinson.photoetcher.server.processing.Utils.getFileContent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoMetadataController {

    @PostMapping("/metadata")
    public ResponseEntity<MetadataResponse> extractMetadata(@RequestParam("file") MultipartFile file) {
        var fileContent = getFileContent(file);

        var date = extractDate(fileContent);
        var geoLocation = extractGeoLocation(fileContent);

        return ResponseEntity.ok(new MetadataResponse(geoLocation, date));
    }

    private String extractDate(byte[] fileContent) {
        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            var date =  metadata.getDirectoriesOfType(ExifSubIFDDirectory.class).stream()
                    .findFirst()
                    .map(ExifSubIFDDirectory::getDateOriginal)
                    .orElse(null);

            var formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.format(date);
        } catch (Exception e) {
            throw new RuntimeException("Error processing file - cannot extract date: " + e.getMessage());
        }
    }

    private GeoLocation extractGeoLocation(byte[] fileContent) {
        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            return metadata.getDirectoriesOfType(GpsDirectory.class).stream()
                    .findFirst()
                    .map(GpsDirectory::getGeoLocation)
                    .map(geoLocation -> new GeoLocation(
                            geoLocation.getLatitude(),
                            geoLocation.getLongitude()
                    ))
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error processing file, cannot extract geolocation: " + e.getMessage());
        }
    }

    private record GeoLocation(double latitude, double longitude) {
    }

    public record MetadataResponse(GeoLocation location, String date) {
    }
}
