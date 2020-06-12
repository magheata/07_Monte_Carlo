/* Created by andreea on 10/06/2020 */
package Presentation;

import Application.Controller;
import Config.Constants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Window extends JFrame {

    private Controller controller;
    private JFileChooser fileChooser;
    private JButton chooseFileButton;
    private JSlider iterationsSlider, samplesSlider;
    private JLabel iterationsLabel, sampelsLabel;

    public Window(Controller controller){
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        this.setSize(Constants.DIM_WINDOW);
        this.setPreferredSize(Constants.DIM_WINDOW);
        this.setMinimumSize(Constants.DIM_WINDOW);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.closeConnection();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setSize(Constants.DIM_CONTROL_PANEL);
        controlPanel.setPreferredSize(Constants.DIM_CONTROL_PANEL);
        controlPanel.setMinimumSize(Constants.DIM_CONTROL_PANEL);

        JPanel buttonsPanel = new JPanel(new FlowLayout());

        JButton useTraditionalButton = new JButton("Use traditional algorithm");
        useTraditionalButton.addActionListener(e -> controller.setUseProbabilisticAlgorithm(false));

        JButton useProbabilisticButton = new JButton("Use probabilistic algorithm");
        useProbabilisticButton.addActionListener(e -> controller.setUseProbabilisticAlgorithm(true));

        buttonsPanel.add(useTraditionalButton);
        buttonsPanel.add(useProbabilisticButton);

        GridBagConstraints buttonsPanelContraints = new GridBagConstraints();
        buttonsPanelContraints.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanelContraints.gridx = 0;
        buttonsPanelContraints.gridy = 0;
        buttonsPanelContraints.gridwidth = 3;

        JPanel iterationsPanel = new JPanel(new FlowLayout());
        iterationsSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 1);
        iterationsLabel = new JLabel("Number of iterations: ");
        iterationsPanel.add(iterationsLabel);
        iterationsPanel.add(iterationsSlider);

        GridBagConstraints iterationsPanelContraints = new GridBagConstraints();
        iterationsPanelContraints.fill = GridBagConstraints.HORIZONTAL;
        iterationsPanelContraints.gridx = 0;
        iterationsPanelContraints.gridy = 1;
        iterationsPanelContraints.gridwidth = 3;

        JPanel samplesPanel = new JPanel();
        samplesSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 1);
        sampelsLabel = new JLabel("Number of samples to take from flag: ");
        samplesPanel.add(sampelsLabel);
        samplesPanel.add(samplesSlider);

        GridBagConstraints sampelsPanelContraints = new GridBagConstraints();
        sampelsPanelContraints.fill = GridBagConstraints.HORIZONTAL;
        sampelsPanelContraints.gridx = 0;
        sampelsPanelContraints.gridy = 2;
        sampelsPanelContraints.gridwidth = 3;

        controlPanel.add(buttonsPanel, buttonsPanelContraints);
        controlPanel.add(iterationsPanel, iterationsPanelContraints);
        controlPanel.add(samplesPanel, sampelsPanelContraints);

        fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPG", "jpg"));

        chooseFileButton = new JButton("Import flag...");
        chooseFileButton.addActionListener(evt -> {
            JButton button = (JButton) evt.getSource();
            JComponent parent = (JComponent) button.getParent();
            switch (fileChooser.showOpenDialog(button.getParent())) {
                case JFileChooser.APPROVE_OPTION:
                    controller.getCountryForFlag(controller.findColorPercentageOfImage(fileChooser.getSelectedFile().getAbsolutePath()));
                    break;
            }
        });
        this.add(controlPanel, BorderLayout.NORTH);

        JPanel chooseFilePanel = new JPanel();
        chooseFilePanel.setSize(Constants.DIM_FILE_PANEL);
        chooseFilePanel.setPreferredSize(Constants.DIM_FILE_PANEL);
        chooseFilePanel.setMinimumSize(Constants.DIM_FILE_PANEL);
        chooseFileButton.setSize(Constants.DIM_FILE_PANEL);
        chooseFileButton.setPreferredSize(Constants.DIM_FILE_PANEL);
        chooseFileButton.setMinimumSize(Constants.DIM_FILE_PANEL);

        chooseFilePanel.add(chooseFileButton);
        this.add(chooseFilePanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }
}
