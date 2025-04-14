package org.levinson.photoetcher.server.processing;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static byte[] getFileContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert file to byte array: " + e.getMessage(), e);
        }
    }
}
