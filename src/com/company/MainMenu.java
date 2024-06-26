package com.company;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainMenu extends JFrame {
    JPanel panelMain;
    private JButton playButton;
    private JButton exitButton;
    protected JComboBox comboBox1;
    private JLabel backgroundImageLabel;
    public static Clip clip;



    public MainMenu(boolean visibility) {
        // Initializer
        setIconImage(new ImageIcon("src/com/company/assets/images/logo.png").getImage()); // for icon
        setContentPane(panelMain);
        setTitle("Guess The Path ?");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setSize(800, 680); // still need this so that if minimized it's not ugly
        setLocationRelativeTo(null);
        setVisible(visibility);
        getRootPane().setDefaultButton(playButton); // to set play button to default, so 'enter' will start at play

        // listener for window closing
        addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                clip.close();
            }
        });

        // set background music
        music();

        // Event handler
        exitButton.addActionListener(e -> {
            System.out.println("Thanks for using this Application ~UWU~");
            System.exit(0);
        });
        playButton.addActionListener(e -> {
            Main.setStageTypeConnector(Objects.requireNonNull(comboBox1.getSelectedItem()).toString());
            Stage stage = new Stage(true);
            Main.getFrameConnector().setVisible(false);

        });
    }


    private void music() {
        /*
         The assets used for the music is
         Track: Lost Sky - Fearless pt.II (feat. Chris Linton) [NCS Release]
Music provided by NoCopyrightSounds.
Watch: https://youtu.be/S19UcWdOA-I
Free Download / Stream: http://ncs.io/Fearless2YO
        */
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/com/company/assets/music/music.wav"));
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-25.0f);
            clip.start();
        }
        catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("Error on Audio system");
        }
    }

}
