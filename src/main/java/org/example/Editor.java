package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Editor is the main class of the PhotoEditor application
 *
 * @author Ilia Parunashvili
 * @version 1.0
 */
public class Editor extends JFrame {
    final private Picture PICTURE = new Picture();

    /**
     * Constructor that sets up JFrame window
     * @throws UnsupportedLookAndFeelException the system default look and feel is replaced by the FlatLaf library
     */
    public Editor () throws UnsupportedLookAndFeelException {
        super("Ilia's Editor");
        FlatDarkLaf.setup();
        UIManager.setLookAndFeel(new FlatDarkLaf());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0x929292));
        getRootPane().setBorder(BorderFactory.createMatteBorder(0, 15, 15, 15, new Color(0x000)));

        add(new MenuBar(PICTURE), BorderLayout.NORTH);
        add(new Controls(PICTURE), BorderLayout.EAST);
        add(new Assistant(PICTURE), BorderLayout.WEST);
        add(PICTURE, BorderLayout.CENTER);

        // set default image on startup
        try {
            PICTURE.setImage(ImageIO.read(this.getClass().getResource("/images/default.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        pack();
        setVisible(true);

        // update picture to fit viewport on window resize event
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                PICTURE.update();
            }
        });
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        new Editor();
    }
}
