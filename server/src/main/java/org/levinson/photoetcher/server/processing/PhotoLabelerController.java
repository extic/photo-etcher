package org.levinson.photoetcher.server.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoLabelerController {

    @PostMapping("/label")
    public String extractMetadata(@RequestParam("file") MultipartFile file, @RequestParam("location") String location, @RequestParam("date") String date) {
        try (var inputStream = new ByteArrayInputStream(file.getBytes())) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new RuntimeException("Failed to load image from file.");
            }

            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            graphics.setFont(new Font("Arial", Font.BOLD, (int)(image.getHeight() * 0.06)));


            String text = location + " " + date;
            var offset = 2;
            drawString(graphics, 40-offset, image.getHeight() - 40-offset, Color.BLACK, text);
            drawString(graphics, 40, image.getHeight() - 40-offset, Color.BLACK, text);
            drawString(graphics, 40+offset, image.getHeight() - 40-offset, Color.BLACK, text);
            drawString(graphics, 40-offset, image.getHeight() - 40, Color.BLACK, text);
            drawString(graphics, 40+offset, image.getHeight() - 40, Color.BLACK, text);
            drawString(graphics, 40-offset, image.getHeight() - 40+offset, Color.BLACK, text);
            drawString(graphics, 40, image.getHeight() - 40+offset, Color.BLACK, text);
            drawString(graphics, 40+offset, image.getHeight() - 40+offset, Color.BLACK, text);
            drawString(graphics, 40, image.getHeight() - 40, Color.RED.darker(), text);

            graphics.dispose();

            File tempFile = File.createTempFile("patched-image-", ".png");
            ImageIO.write(image, "png", tempFile);

            System.out.println(tempFile.getAbsolutePath());
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to patch image: " + e.getMessage(), e);
        }
    }

    private void drawString(Graphics2D graphics, int x, int y, Color color, String text) {
        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }
}
