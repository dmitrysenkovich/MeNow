package com.sam_solutions.app.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * FileUtils test class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-context.xml"})
public class FileUtilsTest {
    @Autowired
    private FileUtils fileUtils;

    /**
     * Tests right extension subtraction
     */
    @Test
    public void testGetExtension() {
        String filename = "test.jpg";
        String extension = fileUtils.getExtension(filename);

        assertEquals("jpg", extension);
    }

    /**
     * Test image loading.
     */
    @Test
    public void testLoadImage() {
        String classPath = new File(this.getClass().getClassLoader().getResource("").getPath()).getAbsolutePath();
        String filename = "test.jpg";
        String filepath = classPath + File.separator + filename;
        byte[] image = fileUtils.loadImage(filepath);

        assertNotEquals(0, image.length);
    }

    /**
     * Tests image saving.
     * @throws IOException thrown when IO failure occurred.
     */
    @Test
    public void testSaveImage() throws IOException {
        String classPath = new File(this.getClass().getClassLoader().getResource("").getPath()).getAbsolutePath();
        String filename = "test.jpg";
        String filepath = classPath + File.separator + filename;
        byte[] image = fileUtils.loadImage(filepath);
        filename = "test0.jpg";
        filepath = classPath + File.separator + filename;
        fileUtils.saveImage(image, filepath);

        File imageFile = new File(filepath);
        assertTrue(imageFile.exists());

        imageFile.delete();
    }

    /**
     * Tests saving image in user directory.
     * @throws IOException
     */
    @Test
    public void testUserSaveImage() throws IOException {
        String classPath = new File(this.getClass().getClassLoader().getResource("").getPath()).getAbsolutePath();
        String filename = "test.jpg";
        String filepath = classPath + File.separator + filename;
        byte[] image = fileUtils.loadImage(filepath);
        String login = "test";
        fileUtils.saveUserImage(login, image, filename);

        filepath = fileUtils.RESOURCES_PATH + fileUtils.USERS_PATH + login + File.separator + filename;
        File imageFile = new File(filepath);
        assertTrue(imageFile.exists());

        File resourcesDirectory = new File(fileUtils.RESOURCES_PATH + "resources");
        org.apache.commons.io.FileUtils.deleteDirectory(resourcesDirectory);
    }

    /**
     * Tests loading image from use
     * @throws IOException
     */
    @Test
    public void testUserLoadImage() throws IOException {
        String classPath = new File(this.getClass().getClassLoader().getResource("").getPath()).getAbsolutePath();
        String filename = "test.jpg";
        String filepath = classPath + File.separator + filename;
        byte[] image = fileUtils.loadImage(filepath);
        String login = "test";
        fileUtils.saveUserImage(login, image, filename);
        image = fileUtils.loadUserImage(login, filename);

        assertNotEquals(0, image.length);

        File resourcesDirectory = new File(fileUtils.RESOURCES_PATH + "resources");
        org.apache.commons.io.FileUtils.deleteDirectory(resourcesDirectory);
    }
}