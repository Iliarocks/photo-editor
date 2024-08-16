package org.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * The Controls class handles the following edits: exposure, contrast, temperature, and tint
 *
 * @author Ilia Parunashvili
 * @version 1.0
 */
public class Controls extends JPanel implements ChangeListener {
    private Picture PICTURE;
    private JSlider temperatureSlider;
    private JLabel temperatureLabel;
    private JSlider tintSlider;
    private JLabel tintLabel;
    private JSlider exposureSlider;
    private JLabel exposureLabel;
    private JSlider contrastSlider;
    private JLabel contrastLabel;

    /**
     * Constructor that sets up the editing panel and the controls on it
     * @param picture the Picture object the controls should act on
     */
    public Controls (Picture picture) {
        setBackground(new Color(0x4e4f4f));
        setPreferredSize(new Dimension(300, 400));
        setBorder(BorderFactory.createLineBorder(new Color(0x2e2e2e), 10));

        PICTURE = picture;

        JPanel tempPanel = new JPanel();
        temperatureSlider = new JSlider(-100, 100, 0);
        temperatureSlider.addChangeListener(this);
        temperatureLabel = new JLabel("Temp");
        temperatureLabel.setForeground(new Color(0xb5b5b5));
        tempPanel.setBackground(new Color(0x4e4f4f));
        tempPanel.add(temperatureLabel);
        tempPanel.add(temperatureSlider);

        JPanel tintPanel = new JPanel();
        tintSlider = new JSlider(-50, 50, 0);
        tintSlider.addChangeListener(this);
        tintLabel = new JLabel("Tint");
        tintLabel.setForeground(new Color(0xb5b5b5));
        tintPanel.setBackground(new Color(0x4e4f4f));
        tintPanel.add(tintLabel);
        tintPanel.add(tintSlider);

        JPanel exposurePanel = new JPanel();
        exposureSlider = new JSlider(-100, 100, 1);
        exposureSlider.addChangeListener(this);
        exposureLabel = new JLabel("Exposure");
        exposureLabel.setForeground(new Color(0xb5b5b5));
        exposurePanel.setBackground(new Color(0x4e4f4f));
        exposurePanel.add(exposureLabel);
        exposurePanel.add(exposureSlider);

        JPanel contrastPanel = new JPanel();
        contrastSlider = new JSlider(-100, 100, 0);
        contrastSlider.addChangeListener(this);
        contrastLabel = new JLabel("Contrast");
        contrastLabel.setForeground(new Color(0xb5b5b5));
        contrastPanel.setBackground(new Color(0x4e4f4f));
        contrastPanel.add(contrastLabel);
        contrastPanel.add(contrastSlider);

        add(tempPanel);
        add(tintPanel);
        add(exposurePanel);
        add(contrastPanel);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // for efficiency, prevents any edits until the slider has stopped moving
        if (exposureSlider.getValueIsAdjusting() || contrastSlider.getValueIsAdjusting() || temperatureSlider.getValueIsAdjusting() || tintSlider.getValueIsAdjusting()) return;

        if (e.getSource() == exposureSlider) {
            PICTURE.setExposure((float) (1 + exposureSlider.getValue() / 100.0));
        } else if (e.getSource() == contrastSlider) {
            PICTURE.setContrast(-contrastSlider.getValue() * 2);
        } else if (e.getSource() == temperatureSlider) {
            PICTURE.setTemperature(temperatureSlider.getValue());
        } else if (e.getSource() == tintSlider) {
            PICTURE.setTint(-tintSlider.getValue());
        }
    }
}
