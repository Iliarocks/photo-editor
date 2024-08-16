package org.example;

import io.github.namankhurpia.Pojo.MyModels.EasyVisionRequest;
import io.github.namankhurpia.Pojo.Vision.VisionApiResponse;
import io.github.namankhurpia.Service.EasyVisionService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Assistant class handles requests to and responses from the GPT-4 vision model
 *
 * @author Ilia Parunashvili
 * @version 1.0
 */
public class Assistant extends JPanel implements ActionListener {
    private Picture PICTURE;
    private JTextArea guide;
    private JButton submit;
    private JTextArea answer;

    /**
     * Constructor that sets up the assistant panel
     * @param picture the Picture object the assistant should reference
     */
    public Assistant (Picture picture) {
        PICTURE = picture;

        setBackground(new Color(0x4e4f4f));
        setPreferredSize(new Dimension(300, 400));
        setBorder(BorderFactory.createLineBorder(new Color(0x2e2e2e), 10));

        UIManager.put("TextArea.background", new Color(0x4e4f4f));

        guide = new JTextArea("If you need help editing your photo, ask the assistant for some pointers by clicking the below:");
        guide.setLineWrap(true);
        guide.setWrapStyleWord(true);
        guide.setColumns(23);

        submit = new JButton("Get Editing Advice");
        submit.addActionListener(this);
        submit.setPreferredSize(new Dimension(255, 25));

        answer = new JTextArea();
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setColumns(23);

        add(guide);
        add(submit);
        add(answer);
    }

    /**
     * Requests editing advice from GPT-4 vision on the current image in the PICTURE object and returns the response
     * @return editing advice on the current image in the PICTURE object
     */
    private String ask () {
        VisionApiResponse response = null;
        try {
            response = new EasyVisionService().VisionAPI(accessToken, new EasyVisionRequest()
                    .setModel("gpt-4-vision-preview")
                    .setPrompt("can you give some unique, concise, and specific color grading tips for this image")
                    .setImageUrls(new ArrayList<String>() {{
                        add("data:image/jpeg;base64," + PICTURE.getBase64());
                    }}));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.choices.get(0).getSystemMessage().content;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        answer.setText(ask());
    }
}
