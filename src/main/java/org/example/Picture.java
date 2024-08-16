package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

/**
 * The Picture class handles rendering, editing, and transforming of a set image
 *
 * @author Ilia Parunashvili
 * @version 1.0
 */
public class Picture extends JPanel {
    public BufferedImage originalImage;
    public BufferedImage originalImageBlackAndWhite;
    public BufferedImage editedImage;
    private final Number[] operations = {(float) 1, 0, 0, 0, 0}; // array of operations on the original image: {exposure, contrast, color or greyscale, temperature, tint}
    private final ImageIcon imageIcon = new ImageIcon();
    private final JLabel imageLabel = new JLabel(imageIcon);

    /**
     * Constructor that sets up the image panel
     */
    public Picture () {
        setBackground(new Color(0x929292));
        setLayout(new GridBagLayout());
        add(imageLabel);
    }

    /**
     * Returns the width of the current image in pixels
     * @return the width of the original image in pixels
     */
    public int getImageWidth () {
        return originalImage.getWidth();
    }

    /**
     * Returns the height of the current image in pixels
     * @return the height of the original image in pixels
     */
    public int getImageHeight () {
        return originalImage.getHeight();
    }

    /**
     * Sets the image to be edited and calls the update method
     * @param image a selected BufferedImage
     */
    public void setImage (BufferedImage image) {
        originalImage = image;
        editedImage = copyImage(image);

        update();
    }

    /**
     * Returns a new BufferedImage object with a copy of the image passed
     * @param image the BufferedImage to be copied
     * @return a new BufferedImage object with a copy of the image passed
     */
    public BufferedImage copyImage (BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlpha = colorModel.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);

        return new BufferedImage(colorModel, raster, isAlpha, null);
    }

    /**
     * Rescales and renders the new edited image
     */
    public void update () {
        int newHeight = getHeight() == 0 ? 500 : getHeight(); // default height to 500 if zero, otherwise use current height
        int newWidth = (int) (newHeight * ((double) getImageWidth() / getImageHeight())); // calculate new width that maintains image aspect ratio

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(editedImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        imageIcon.setImage(resized);
        imageLabel.repaint();
    }

    /**
     * Swaps pixels of the original image across the horizontal axis
     */
    public void flipVertical () {
        BufferedImage flipped = copyImage(originalImage);

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                flipped.setRGB(x, y, originalImage.getRGB(x, getImageHeight() - y - 1));
            }
        }

        originalImage = flipped;

        // create a new black and white image if an outdated one exists
        if (!Objects.isNull(originalImageBlackAndWhite)) {
            makeBlackAndWhite();
        }

        runOperations();
    }

    /**
     * Swaps pixels of the original image across the vertical axis
     */
    public void flipHorizontal () {
        BufferedImage flipped = copyImage(originalImage);

        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                flipped.setRGB(x, y, originalImage.getRGB(getImageWidth() - x - 1, y));
            }
        }

        originalImage = flipped;

        // create a new black and white image if an outdated one exists
        if (!Objects.isNull(originalImageBlackAndWhite)) {
            makeBlackAndWhite();
        }

        runOperations();
    }

    /**
     * Increases or decreases the color temperature of the image
     * @param factor the amount by which to increase or decrease the color temperature
     */
    public void setTemperature (int factor) {
        operations[3] = factor;

        runOperations();
    }

    /**
     * Adds green or purple to the image
     * @param factor the amount by which to add green or purple
     */
    public void setTint (int factor) {
        operations[4] = factor;

        runOperations();
    }

    /**
     * Increases or decreases the brightness of the image
     * @param factor the decimal factor multiplied by every pixel's brightness
     */
    public void setExposure (float factor) {
        operations[0] = factor;

        runOperations();
    }

    /**
     * Increases or decreases image contrast
     * @param factor the amount by which to increase or decrease the contrast
     */
    public void setContrast (int factor) {
        operations[1] = factor;

        runOperations();
    }

    /**
     * Replace the color image with the black and white image as the canvas for the edits
     */
    public void setBlackAndWhite () {
        operations[2] = operations[2].equals(1) ? 0 : 1;

        // if the black and white image does not exist, create it
        if (Objects.isNull(originalImageBlackAndWhite)) {
            makeBlackAndWhite();
        }

        runOperations();
    }

    /**
     * Create a black and white version of the original image
     */
    private void makeBlackAndWhite() {
        originalImageBlackAndWhite = copyImage(originalImage);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(originalImage, originalImageBlackAndWhite);
    }

    /**
     * Apply the edits specified in the operations array relative to the original image to create the edited image.
     */
    private void runOperations () {
        BufferedImage source = operations[2].equals(1) ? originalImageBlackAndWhite : originalImage; // set the reference point as either the color or greyscale image

        // set the exposure and contrast of the image
        RescaleOp rescale = new RescaleOp((float) operations[0], (int) operations[1], null);
        rescale.filter(source, editedImage);

        // set the color temperature and tint of the image
        for (int x = 0; x < getImageWidth(); x++) {
            for (int y = 0; y < getImageHeight(); y++) {
                int rgb = editedImage.getRGB(x, y);
                // use Math.max() and Math.min() to ensure that the rgb values remain between 0 and 255
                int r = Math.max(0, Math.min(255, ((rgb >> 16) & 0xFF) + (int) operations[3] - (int) operations[4]));
                int g = Math.max(0, Math.min(255, ((rgb >> 8) & 0xFF) + (int) operations[4]));
                int b = Math.max(0, Math.min(255, (rgb & 0xFF) - (int) operations[3] - (int) operations[4]));

                editedImage.setRGB(x, y, r << 16 | g << 8 | b);
            }
        }

        update();
    }

    /**
     * Returns the edited image in Base64 form
     * @return the Base64 version of the edited image
     */
    public String getBase64 () {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(editedImage, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }
}
