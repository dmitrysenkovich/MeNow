package com.sam_solutions.app.utils;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * Loads and saves user images.
 */
public class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class);

    public static String USERS_PATH = "resources" + File.separator +
                                            "images" + File.separator +
                                            "users" + File.separator;
    public static String RESOURCES_PATH = "";

    public FileUtils() {
        if (RESOURCES_PATH.isEmpty()) {
            File classPath = new File(this.getClass().getClassLoader().getResource("").getPath());
            RESOURCES_PATH = classPath.getParentFile().getParentFile().getPath() + File.separator;
            String message = MessageFormat.format("RESOURCES_PATH: {0}", RESOURCES_PATH);
            logger.info(message);
        }
    }

    /**
     * Returns filename extension.
     * @param filename filename which extensions is returned.
     * @return filename extension.
     */
    public String getExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int extensionPos = filename.lastIndexOf('.');
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        }
        else {
            return filename.substring(index + 1);
        }
    }

    /**
     * Loads image from specified path.
     * @param path path where the image resides.
     * @return loaded image.
     */
    public byte[] loadImage(String path) {
        File imageFile = new File(path);
        ByteArrayOutputStream imageInByteStream = new ByteArrayOutputStream();
        byte[] image = null;
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            ImageIO.write(originalImage, this.getExtension(path), imageInByteStream);
            image = imageInByteStream.toByteArray();
        } catch (IOException e) {
            String message = MessageFormat.format("Error loading image {0}: {1}", path, e.getCause());
            logger.error(message);
        }
        if (image != null) {
            String message = MessageFormat.format("Image {0} was successfully loaded", path);
            logger.info(message);
        }
        return image;
    }

    /**
     * Saves image in specified path.
     * @param image image to be saved.
     * @param path path where to save image.
     */
    public void saveImage(byte[] image, String path) {
        InputStream in = new ByteArrayInputStream(image);
        try {
            BufferedImage bufferedImage = ImageIO.read(in);
            File userPath = new File(path);
            if (!userPath.exists())
                userPath.mkdirs();
            ImageIO.write(bufferedImage, getExtension(path), userPath);
        } catch (IOException e) {
            String message = MessageFormat.format("Error saving image {0}: {1}", path, e.getCause());
            logger.error(message);
            return;
        }
        String message = MessageFormat.format("Image {0} was successfully saved", path);
        logger.info(message);
    }

    /**
     * Saves specified user image in user path.
     * @param login user login.
     * @param image raw image.
     * @param imageName image file name.
     */
    public void saveUserImage(String login, byte[] image, String imageName) {
        if (image == null) {
            String warning = MessageFormat.format("Array of image {0} bytes is null", imageName);
            logger.warn(warning);
            return;
        }
        String imagePath = RESOURCES_PATH + USERS_PATH +
                login + File.separator + imageName;
        saveImage(image, imagePath);
    }

    /**
     * Loads user image.
     * @param login user login.
     * @param imageName image name.
     * @return raw image.
     */
    public byte[] loadUserImage(String login,  String imageName) {
        String imagePath = RESOURCES_PATH + USERS_PATH +
                login + File.separator + imageName;
        byte[] image = loadImage(imagePath);
        return image;
    }
}
