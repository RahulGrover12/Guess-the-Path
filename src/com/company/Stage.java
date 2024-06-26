package com.company;

import com.company.algo.bfs.BFSChecker;
import com.company.algo.dfs.DFSChecker;
import com.company.algo.dijkstra.DijkstraAlgorithmChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Stage extends JFrame {
    private JTextField textField1;
    private JButton submitButton;
    JPanel panelMain;
    private JLabel stageType;
    private JLabel gambar;
    private JLabel scoreLabel;
    private JLabel lifeLabel;
    private final int scoreGain; // score gain
    // for randomly selecting the file name
    private final int randomImageFileName;

    private void adjustSize() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();

        // Use the primary screen size for simplicity
        int screenWidth = gd[0].getDisplayMode().getWidth();
        int screenHeight = gd[0].getDisplayMode().getHeight();

        // Get the maximum bounds of the usable area (excluding taskbars, etc.)
        Rectangle usableBounds = ge.getMaximumWindowBounds();

        // Calculate the preferred size based on the usable area
        int preferredWidth = Math.min(Math.min(screenWidth, usableBounds.width),1000);
        int preferredHeight = Math.min(Math.min(screenHeight, usableBounds.height),800);

        // Set the frame size
        setSize(preferredWidth, preferredHeight);

        // Center the frame
        setLocationRelativeTo(null);
    }
    public Stage(boolean visibility) {
        // for initialization
        setIconImage(new ImageIcon("src/com/company/assets/images/logo.png").getImage()); // for icon
        setContentPane(panelMain);
        setTitle("Stage");
        setUndecorated(true); // so that there are no minimize close else to prevent cheating
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        pack();
//        setStageSize(); // so that when minimized it's not ugly
        setExtendedState(Frame.MAXIMIZED_BOTH); // full screen
        panelMain.setLayout(new BorderLayout());
        panelMain.add(gambar, BorderLayout.CENTER);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(gambar, BorderLayout.CENTER);
        panelMain.add(leftPanel, BorderLayout.WEST);
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowOpened(WindowEvent e) {
//                adjustSize();
//            }
//        });
        setLocationRelativeTo(null);
        setVisible(visibility);
        getRootPane().setDefaultButton(submitButton); // to make submitButton default enter operation
        // set score
        scoreGain = 5;
        updateLife();
        updateScore();


        // init listener for submit
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // set main-menu visibility
                Main.getFrameConnector().setVisible(true);

                // life - 1 to counter cheater
                Main.setLife(Main.getLife() - 1);
            }
        });

        // set top-right text
        stageType.setText(
                Main.getStageTypeConnector());

        // set Jlabel icon
        Random randomizer = new Random();
        randomImageFileName = randomizer.nextInt(Objects.requireNonNull(new File("src/com/company/soal/gambar").list()).length);
//        gambar.setIcon(new ImageIcon(String.format("src/com/company/soal/gambar/%d.png", randomImageFileName)));
        ImageIcon icon = new ImageIcon(String.format("src/com/company/soal/gambar/%d.png", randomImageFileName));
        Image scaledImage = icon.getImage().getScaledInstance(1750, 880, Image.SCALE_SMOOTH);
        gambar.setIcon(new ImageIcon(scaledImage));

        // clear text
        gambar.setText("");

        // Listener
        submitButton.addActionListener(e -> {
            // Submit button
            try {
                if (checkAnswer(textField1.getText().toLowerCase())) {
                    // correct
                    System.out.println("Status: Correct!");

                    // close window / game stage when correct
                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

                    // add life because the player wins fairly
                    Main.setLife(Main.getLife() + 1);

                    // score add
                    Main.setScore(Main.getScore() + scoreGain);
                    updateScore();

                } else {
                    // incorrect
                    System.out.println("Status: Incorrect!");
                    // life subtract
                    Main.setLife(Main.getLife() - 1);
                    updateLife();
                }
            } catch (IOException ex) {
                System.out.println("Error at checking answer (should be at IO level)");
            }
        });
    }
    // helper function for updating
    private void updateScore() {
        scoreLabel.setText(String.format("%d", Main.getScore()));
    }

    private void updateLife() {
        lifeLabel.setText(String.format("%d", Main.getLife()));

        // check if dead
        if (Main.getLife() <= 0) {
            setVisible(false);
            Losing losing = new Losing(true);
        }

    }

    // function for checking answer
    private boolean checkAnswer(String answer) throws IOException {
        String type = Main.getStageTypeConnector();

        // abstract class for checker
        switch (type) {
            case "Breadth First Search" -> {
                return (new BFSChecker(randomImageFileName)).getReturnValue(answer);
            }
            case "Depth First Search" -> {
                return (new DFSChecker(randomImageFileName)).getReturnValue(answer);
            }
            case "Shortest Path" -> {
                // Implementing Dijkstra Algorithm here
                return (new DijkstraAlgorithmChecker(randomImageFileName)).getReturnValue(answer);
            }
            default -> System.out.println("Error, stage type not found!");
        }

        // for error case
        System.out.println("ERROR, Checking answer failed");
        return false;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
