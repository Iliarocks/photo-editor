package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * The MenuBar class handles the creation and functions of the of menu-bar items
 *
 * @author Ilia Parunashvili
 * @version 1.0
 */
public class MenuBar extends JPanel {
    private Picture PICTURE;
    public MenuBar (Picture picture) {
        setLayout(new BorderLayout());
        setBackground(new Color(0x000));
        setPreferredSize(new Dimension(0, 40));

        PICTURE = picture;

        UIManager.put("MenuBar.background", Color.BLACK); // set the menu-bar background to black

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(e -> openImage());

        JMenuItem export = new JMenuItem("Export");
        export.addActionListener(e -> saveImage());

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(open);
        fileMenu.add(export);
        fileMenu.setOpaque(true);
        fileMenu.setBackground(new Color(0x000));
        fileMenu.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));

        JMenuItem flipHorizontal = new JMenuItem("Flip Horizontal");
        flipHorizontal.addActionListener(e -> PICTURE.flipHorizontal());

        JMenuItem flipVertical = new JMenuItem("Flip Vertical");
        flipVertical.addActionListener(e -> PICTURE.flipVertical());

        JMenuItem blackAndWhite = new JMenuItem("Black and White");
        blackAndWhite.addActionListener(e -> PICTURE.setBlackAndWhite());

        JMenu photoMenu = new JMenu("Photo");
        photoMenu.add(flipHorizontal);
        photoMenu.add(flipVertical);
        photoMenu.add(blackAndWhite);
        photoMenu.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 500, 20);
        menuBar.add(fileMenu);
        menuBar.add(photoMenu);

        add(menuBar, BorderLayout.CENTER);
    }

    /**
     * Opens a file menu for users to choose an image file (jpg or png) to open in the editor
     */
    private void openImage () {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("images", "jpg", "png"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                PICTURE.setImage(ImageIO.read(fileChooser.getSelectedFile()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens a file menu for users to save their edited image to their computer
     */
    private void saveImage () {
        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(PICTURE.editedImage, "png", fileChooser.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

