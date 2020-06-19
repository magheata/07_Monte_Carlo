/* Created by andreea on 10/06/2020 */
package Presentation;

import Application.Controller;
import Config.Constants;
import Domain.FlagColors;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Window extends JFrame {

    private Controller controller;
    private ImgUtility imgUtility;
    private JFileChooser fileChooser;
    private JLayeredPane layeredPane;
    private JButton chooseFileButton, findCountryButton;
    private JSlider iterationsSlider, samplesSlider;
    private JLabel iterationsLabel, sampelsLabel, flagLabel, countryLabel;
    private JScrollPane possibleCountriesScrollPane;
    private JPanel chooseFilePanel, flagCountryPanel, possibleCountriesPanel;
    private GridBagConstraints buttonsPanelContraints, iterationsPanelContraints, sampelsPanelContraints;

    public Window(Controller controller){
        this.controller = controller;
        this.imgUtility = new ImgUtility();
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

        controlPanel.add(initButtonsPanel(), buttonsPanelContraints);
        controlPanel.add(initIterationsPanel(), iterationsPanelContraints);
        controlPanel.add(initSamplesPanel(), sampelsPanelContraints);

        initFlagCountryPanel();
        initPossibleCountriesPanel();
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(initChooseFilePanel(), BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void initPossibleCountriesPanel(){
        possibleCountriesPanel = new JPanel();
        possibleCountriesPanel.setLayout(new FlowLayout());
        possibleCountriesScrollPane = new JScrollPane(possibleCountriesPanel);
        possibleCountriesScrollPane.setPreferredSize(Constants.DIM_SCROLL_PANEL);
        possibleCountriesScrollPane.setMinimumSize(Constants.DIM_SCROLL_PANEL);
    }

    private void initLayeredPane(){

        layeredPane = new JLayeredPane();

        layeredPane.setMinimumSize(Constants.DIM_FILE_PANEL);
        layeredPane.setPreferredSize(Constants.DIM_FILE_PANEL);
        layeredPane.setSize(Constants.DIM_FILE_PANEL);

        flagLabel = new JLabel("");
        flagLabel.setMinimumSize(Constants.DIM_FILE_PANEL);
        flagLabel.setPreferredSize(Constants.DIM_FILE_PANEL);
        flagLabel.setSize(Constants.DIM_FILE_PANEL);
        flagLabel.setHorizontalAlignment(JLabel.CENTER);

        layeredPane.add(flagLabel, 0);

        countryLabel = new JLabel("");
        countryLabel.setHorizontalAlignment(JLabel.CENTER);
        countryLabel.setFont(new Font("Sans", Font.BOLD, 90));
        countryLabel.setMinimumSize(Constants.DIM_FILE_PANEL);
        countryLabel.setPreferredSize(Constants.DIM_FILE_PANEL);
        countryLabel.setSize(Constants.DIM_FILE_PANEL);
        countryLabel.setHorizontalAlignment(JLabel.CENTER);

        layeredPane.add(countryLabel, 1);
        layeredPane.setVisible(true);

    }

    private void initFlagCountryPanel(){
        flagCountryPanel = new JPanel();
        flagCountryPanel.setLayout(new BorderLayout());

        findCountryButton = new JButton(Constants.TEXT_FIND_COUNTRY_BUTTON);

        findCountryButton.setMinimumSize(Constants.DIM_BUTTON);
        findCountryButton.setPreferredSize(Constants.DIM_BUTTON);

        findCountryButton.addActionListener(e -> {
            if (findCountryButton.getText().equals(Constants.TEXT_TRY_AGAIN_BUTTON)){
                findCountryButton.setText(Constants.TEXT_FIND_COUNTRY_BUTTON);
                flagLabel.setIcon(null);
                countryLabel.setText("");
                layeredPane.setLayer(flagLabel, 0);
                layeredPane.setLayer(countryLabel, 1);
                possibleCountriesPanel.removeAll();
                this.remove(flagCountryPanel);
                this.add(chooseFilePanel, BorderLayout.SOUTH);
            } else {
                findCountryButton.setText(Constants.TEXT_TRY_AGAIN_BUTTON);
                showFlagCountry(controller.getCountryForFlag(fileChooser.getSelectedFile().getAbsolutePath(),
                        iterationsSlider.getValue(),
                        samplesSlider.getValue()));
            }
            this.repaint();
            this.revalidate();
        });

        initLayeredPane();

        flagCountryPanel.add(layeredPane, BorderLayout.NORTH);
        flagCountryPanel.add(findCountryButton, BorderLayout.SOUTH);
    }


    public void addFlagToWindow(String flagPath){
        this.remove(chooseFilePanel);
        try {
            BufferedImage flag = ImageIO.read(new File(flagPath));
            BufferedImage resizedImage = imgUtility.resize(flag, flagLabel.getWidth(), flagLabel.getHeight());
            flagLabel.setIcon(new ImageIcon(resizedImage));
            layeredPane.setLayer(flagLabel, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.add(flagCountryPanel);
        this.revalidate();
        this.repaint();
    }

    public void showFlagCountry(String country){
        countryLabel.setText(country);
        if (country.length() > 10){
            countryLabel.setFont(Constants.FONT_SMALL_LABEL);
        } else {
            countryLabel.setFont(Constants.FONT_LABEL);
        }
        layeredPane.setLayer(countryLabel, 1);
        //flagCountryPanel.remove(findCountryButton);
        flagCountryPanel.add(possibleCountriesScrollPane, BorderLayout.CENTER);
        //flagCountryPanel.add(findCountryButton, BorderLayout.SOUTH);
    }

    public void showAllPossibleCountries(ArrayList<FlagColors> possibleFlags, HashMap<String, Integer> sortedFlags){
        String country;
        if (possibleFlags != null){
            for (int i = 0; i < possibleFlags.size(); i++){
                String flagPath = possibleFlags.get(i).getFlagImagePath();
                country = controller.getCountryForFlag(flagPath);
                possibleCountriesPanel.add(createFlagCountryLabel(country, flagPath));
            }
        } else {
            Object [] countries = sortedFlags.keySet().toArray();
            for (int i = 0; i < countries.length; i++){
                String flagPath = (String) countries[i];
                country = controller.getCountryForFlag(flagPath);
                possibleCountriesPanel.add(createFlagCountryLabel(country + " (" + sortedFlags.get(flagPath) + ")", flagPath));
            }
        }
    }

    private JLabel createFlagCountryLabel(String text, String flagPath){
        JLabel country = new JLabel(text);
        country.setHorizontalTextPosition(JLabel.CENTER);
        country.setVerticalTextPosition(JLabel.BOTTOM);
        try {
            BufferedImage flag = ImageIO.read(new File(Constants.USER_PATH + "/flags/" + flagPath));
            BufferedImage resized = imgUtility.resize(flag, (int) (Constants.DIM_SCROLL_PANEL.getWidth() * 0.25), (int) (Constants.DIM_SCROLL_PANEL.getHeight() * 0.25));
            country.setIcon(new ImageIcon(resized));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return country;
    }

    private JPanel initButtonsPanel(){
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        JButton useTraditionalButton = new JButton("Use traditional algorithm");
        JButton useProbabilisticButton = new JButton("Use probabilistic algorithm");

        deactivateButton(useProbabilisticButton);
        activateButton(useTraditionalButton);

        useTraditionalButton.addActionListener(e -> {
            deactivateButton(useProbabilisticButton);
            controller.setUseProbabilisticAlgorithm(false);
            activateButton(useTraditionalButton);
        });

        useProbabilisticButton.addActionListener(e -> {
            deactivateButton(useTraditionalButton);
            controller.setUseProbabilisticAlgorithm(true);
            activateButton(useProbabilisticButton);
        });

        buttonsPanel.add(useTraditionalButton);
        buttonsPanel.add(useProbabilisticButton);

        buttonsPanelContraints = new GridBagConstraints();
        buttonsPanelContraints.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanelContraints.gridx = 0;
        buttonsPanelContraints.gridy = 0;
        buttonsPanelContraints.gridwidth = 3;

        return buttonsPanel;
    }

    private JPanel initIterationsPanel(){
        JPanel iterationsPanel = new JPanel(new GridBagLayout());

        iterationsPanel.setPreferredSize(Constants.DIM_ITERATIONS_PANEL);
        iterationsPanel.setSize(Constants.DIM_ITERATIONS_PANEL);
        iterationsPanel.setMinimumSize(Constants.DIM_ITERATIONS_PANEL);

        iterationsSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 101, 1);
        iterationsSlider.setPreferredSize(Constants.DIM_SLIDER);
        iterationsSlider.setMinimumSize(Constants.DIM_SLIDER);

        iterationsSlider.setMajorTickSpacing(25);
        iterationsSlider.setMinorTickSpacing(5);
        iterationsSlider.setPaintTicks(true);
        iterationsSlider.setPaintLabels(true);
        iterationsSlider.setSnapToTicks(true);

        GridBagConstraints iterationsSliderContraints = new GridBagConstraints();
        iterationsSliderContraints.fill = GridBagConstraints.HORIZONTAL;
        iterationsSliderContraints.gridx = 2;
        iterationsSliderContraints.gridy = 0;
        iterationsSliderContraints.gridwidth = 2;

        iterationsLabel = new JLabel("Number of iterations: ");

        GridBagConstraints iterationsLabelContraints = new GridBagConstraints();
        iterationsLabelContraints.fill = GridBagConstraints.HORIZONTAL;
        iterationsLabelContraints.gridx = 0;
        iterationsLabelContraints.gridy = 0;
        iterationsLabelContraints.insets = new Insets(0, 0, 0, 108);
        iterationsSliderContraints.gridwidth = 1;

        iterationsPanel.add(iterationsLabel, iterationsLabelContraints);
        iterationsPanel.add(iterationsSlider, iterationsSliderContraints);

        iterationsPanelContraints = new GridBagConstraints();
        iterationsPanelContraints.fill = GridBagConstraints.HORIZONTAL;
        iterationsPanelContraints.gridx = 0;
        iterationsPanelContraints.gridy = 1;
        iterationsPanelContraints.gridwidth = 3;

        return iterationsPanel;
    }

    private JPanel initSamplesPanel(){
        JPanel samplesPanel = new JPanel();
        samplesPanel.setPreferredSize(Constants.DIM_SAMPLES_PANEL);
        samplesPanel.setSize(Constants.DIM_SAMPLES_PANEL);
        samplesPanel.setMinimumSize(Constants.DIM_SAMPLES_PANEL);

        samplesSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, 1);
        samplesSlider.setPreferredSize(Constants.DIM_SLIDER);
        samplesSlider.setMinimumSize(Constants.DIM_SLIDER);

        samplesSlider.setMajorTickSpacing(250);
        samplesSlider.setMinorTickSpacing(50);
        samplesSlider.setPaintTicks(true);
        samplesSlider.setPaintLabels(true);
        samplesSlider.setSnapToTicks(true);

        sampelsLabel = new JLabel("Number of samples to take from flag: ");
        samplesPanel.add(sampelsLabel);
        samplesPanel.add(samplesSlider);

        sampelsPanelContraints = new GridBagConstraints();
        sampelsPanelContraints.fill = GridBagConstraints.HORIZONTAL;
        sampelsPanelContraints.gridx = 0;
        sampelsPanelContraints.gridy = 2;
        sampelsPanelContraints.gridwidth = 3;
        return samplesPanel;
    }
    private JPanel initChooseFilePanel(){
        fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPG", "jpg"));

        chooseFileButton = new JButton("Import flag...");
        chooseFileButton.addActionListener(evt -> {
            JButton button = (JButton) evt.getSource();
            switch (fileChooser.showOpenDialog(button.getParent())) {
                case JFileChooser.APPROVE_OPTION:
                    addFlagToWindow(fileChooser.getSelectedFile().getAbsolutePath());
                    break;
            }
        });


        chooseFilePanel = new JPanel();

        chooseFilePanel.setSize(Constants.DIM_FILE_PANEL);
        chooseFilePanel.setPreferredSize(Constants.DIM_FILE_PANEL);
        chooseFilePanel.setMinimumSize(Constants.DIM_FILE_PANEL);

        chooseFileButton.setSize(Constants.DIM_FILE_PANEL);
        chooseFileButton.setPreferredSize(Constants.DIM_FILE_PANEL);
        chooseFileButton.setMinimumSize(Constants.DIM_FILE_PANEL);

        chooseFilePanel.add(chooseFileButton);

        return chooseFilePanel;
    }


    /**
     *
     * @param button
     */
    private void deactivateButton(JButton button){
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }


    /**
     *
     * @param button
     */
    private void activateButton(JButton button){
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }
}
