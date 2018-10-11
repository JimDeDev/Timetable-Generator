/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetablegenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author gjr5542
 */
public class TimetableGeneratorPanel extends JPanel implements ActionListener, ChangeListener
{
    private final PaperDatabase model;
    private final JLabel papersLabel, selectionLabel, calenderLabel, timetableDetailsLabel;
    private final JList<String> paperList, selectionList;
    private final JButton addButton, removeButton, infoButton, generateButton, nextButton, previousButton, resetButton;
    private final JTextPane calenderAxisLabel;
    private final ArrayList<JTextPane> sessions = new ArrayList();
    private final JSlider calenderSlider;
    private JTextPane[] colouredBoxes = new JTextPane[4];
    private final Border blackline = BorderFactory.createLineBorder(Color.GRAY);
    private final Font fontBold = new Font(null , Font.BOLD, 13);
    private final Font boxFont = new Font(null, Font.BOLD, 11);
    private final Color colors[] = {new Color(225, 247, 213), 
                                    new Color(255, 189, 189),
                                    new Color(181, 201, 255),
                                    new Color(250, 193, 255)};

    public TimetableGeneratorPanel(PaperDatabase model)
    {
        this.model = model;
        this.setLayout(null); // manual layout
        
        //Info Button
        this.infoButton = new JButton("Info");
        this.infoButton.setLocation(10, 10);
        this.infoButton.setSize(90, 20);
        this.infoButton.addActionListener(this);
        this.add(infoButton);
        
        // Reset Button
        this.resetButton = new JButton("Reset");
        this.resetButton.setLocation(120, 10);
        this.resetButton.setSize(90, 20);
        this.resetButton.addActionListener(this);
        this.add(resetButton);
        
        // Gernate Buttton
        this.generateButton = new JButton("Generate Timetables");
        this.generateButton.setLocation(265, 152);
        this.generateButton.setSize(200, 20);
        this.generateButton.setEnabled(false);
        this.generateButton.addActionListener(this);
        this.add(generateButton);
        
        // Jlabel
        this.papersLabel = new JLabel("Avalable Papers");
        this.papersLabel.setLocation(10, 40);
        this.papersLabel.setSize(100, 20);
        this.add(papersLabel);
        
        // Jlabel
        this.selectionLabel = new JLabel("Selected Papers");
        this.selectionLabel.setLocation(265, 40);
        this.selectionLabel.setSize(100, 20);
        this.add(selectionLabel);
        
        // Jlabel
        this.calenderLabel = new JLabel("Generated Timetables will display here");
        this.calenderLabel.setLocation(480, 40);
        this.calenderLabel.setSize(400, 20);
        this.add(calenderLabel);

        // Add the ADD button
        this.addButton = new JButton(">");
        addButton.setLocation(215, 75);
        addButton.setSize(45, 25);
        addButton.addActionListener(this);
        this.add(addButton);
        
        // Add the REMOVE button
        removeButton = new JButton("<");
        removeButton.setLocation(215, 115);
        removeButton.setSize(45, 25);
        removeButton.addActionListener(this);
        removeButton.setEnabled(false);
        this.add(removeButton); 
        
        //inserting the JList into a scroll pane and adding it to the frame
        this.paperList =  new JList(this.model.getPaperTitles(this.model.getPapersArrayList()));
        this.paperList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane papersScrollPane = new JScrollPane(paperList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        papersScrollPane.setLocation(10, 60);
        papersScrollPane.setSize(200, 300);
        this.add(papersScrollPane);
       
        //inserting the JList into a scroll pane and adding it to the frame
        this.selectionList =  new JList();
        this.selectionList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane selectionScrollPane = new JScrollPane(selectionList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        selectionScrollPane.setLocation(265, 60);
        selectionScrollPane.setSize(200, 90);
        this.add(selectionScrollPane);

        // details jLabel
        this.timetableDetailsLabel = new JLabel("Timetable Details will display here");
        this.timetableDetailsLabel.setLocation(265, 170);
        this.timetableDetailsLabel.setSize(200, 50);
        this.add(timetableDetailsLabel);
        
        // The coloured text boxes that maps to the timetable and paper details
        int y = 204;
        for(int i = 0; i < 4; i++)
        {
            this.colouredBoxes[i] = new JTextPane();
            this.colouredBoxes[i].setBackground(colors[i]);
            this.colouredBoxes[i].setBorder(blackline);
            this.colouredBoxes[i].setFont(boxFont);
            this.colouredBoxes[i].setSize(200, 35);
            this.colouredBoxes[i].setLocation(265, y);
            this.colouredBoxes[i].setEditable(false);
            this.colouredBoxes[i].setFocusable(false);

            y += 40;
        }

        // the x and y label for the timetable
        this.calenderAxisLabel = new JTextPane();
        this.calenderAxisLabel.setText("          08    -    09    -    10    -    11    -    12    -    13    -    14    -    15    -    16    -    17    -    18   \n\n"
                                + "MON \n\n\nTUE \n\n\nWED \n\n\nTHU \n\n\nFRI");
        this.calenderAxisLabel.setLocation(480, 60);
        this.calenderAxisLabel.setSize(600, 299);
        this.calenderAxisLabel.setEditable(false);
	this.calenderAxisLabel.setFocusable(false);
	this.calenderAxisLabel.setBorder(blackline);
	this.calenderAxisLabel.setFont(fontBold);
        this.calenderAxisLabel.setOpaque(false);
        this.add(calenderAxisLabel);
        
        //Next button
        this.nextButton = new JButton(">");
        this.nextButton.setLocation(530, 365);
        this.nextButton.setSize(45, 25);
        this.nextButton.addActionListener(this);
        this.nextButton.setEnabled(false);
        this.add(nextButton);
        
        //previous button
        this.previousButton = new JButton("<");
        this.previousButton.setLocation(480, 365);
        this.previousButton.setSize(45, 25);
        this.previousButton.addActionListener(this);
        this.previousButton.setEnabled(false);
        this.add(previousButton); 
        
        // slider
        this.calenderSlider = new JSlider();
        this.calenderSlider.setLocation(583, 370);
        this.calenderSlider.setSize(500, 20);
        this.calenderSlider.setEnabled(false);
        this.calenderSlider.setValue(0);
        this.calenderSlider.addChangeListener(this);
        this.add(calenderSlider);
    }

    /**
     * This method displays a visual representation of the timetable passed in.
     * it will clear the current timetable if there is one displayed.
     * this is called when generate is clicked or the slider is moved.
     * @param aTimetable 
     */
    public void updateCalenderView(Timetable aTimetable)
    {
        /*
        Size of 1 hr session 42px wide, 40px high
        day ofset = 54px
        */
        
        int xOffset = 525;
        int yOffset = 88;
        float sessionWidth;
        int dayOffset = 0;
        float hourOffset = 0;
        int colorindex = 0;
        int numberOfTimetables = this.model.getTimetableArrayList().size();
        
        
        //clears the calender view if there is already a timetable displayed
        if(!sessions.isEmpty())
        {
            ClearCalenderView();
        }

       //updating some JLabels to display relavant data
        this.calenderLabel.setText("Timetable " + aTimetable.getTimetableNumber() + " of " + numberOfTimetables);
        this.timetableDetailsLabel.setText("Timetable " + aTimetable.getTimetableNumber() + " details");
        
        // enables and sets the range of the slider based off number of generated timetables
        this.calenderSlider.setMaximum(numberOfTimetables - 1);
        this.calenderSlider.setEnabled(true);
        
        // loops through each paper in the timetable
        for(Paper aPaper : aTimetable.getNewPapers())
        {
            this.colouredBoxes[colorindex].setText(aPaper.getPaperName() + "\nStream No: " + aPaper.getStream().get(0).getStreamNumber());
            this.add(colouredBoxes[colorindex]);
            
            //Loops through each session in the current paper and generates the session boxes
            for(Session aSession : aPaper.getStream().get(0).getSessions())
            {
                //sets the day offset
                switch(aSession.getTime().getDay())
                {
                    case "MON": dayOffset = 0; break;
                    case "TUE": dayOffset = 54; break;
                    case "WED": dayOffset = 108; break;
                    case "THU": dayOffset = 162; break;
                    case "FRI": dayOffset = 216;     
                }
                //sets the width of each session box based on session duration
                sessionWidth = (aSession.getTime().getEndTime() - aSession.getTime().getStartTime()) * 50 - 2;
                //sets the hour offset based of the session start time
                hourOffset = (aSession.getTime().getStartTime() - 8) * 50;
                
                //moves sessions that start on the half hour to the correct x position
                if((aSession.getTime().getStartTime() % 1) > 0)
                {
                    hourOffset = (aSession.getTime().getStartTime() - 8) * 50 + 10;
                }
                
                JTextPane aSessionView = new JTextPane();

                aSessionView.setLocation( xOffset + (int)hourOffset, yOffset + dayOffset);
                aSessionView.setSize((int) sessionWidth, 40);
                aSessionView.setText(aPaper.getPaperCode() + "\n" + aSession.getRoom());
                
                aSessionView.setEditable(false);
                aSessionView.setFocusable(false);
                aSessionView.setBorder(blackline);
                aSessionView.setFont(boxFont);
                aSessionView.setBackground(colors[colorindex]);
                this.add(aSessionView);
                sessions.add(aSessionView);          
            }
            //increased the colorindex so each paper displays in a different color
            colorindex++;
        }
        //updates the view with the new components
        this.revalidate();
    }
    /**
     * This method will clear the calender and timetable details in the view
     */
    public void ClearCalenderView()
    {

        //removes all the JTextBoxes on the calender display
        sessions.forEach((aJTextPane) -> {
            this.remove(aJTextPane);
        });
        
        //Clears the JtextPanes from the arraylist
        sessions.clear(); 
        //refreshes the panel
        this.revalidate();
        this.repaint();
    }
    /**
     * this method changes the state of the buttons
     */
    public void updateView(){
        
        this.selectionList.setListData(this.model.getPaperTitles(this.model.getSelectedArrayList()));
        
        // sets the state of the add button
        if(this.model.getSelectedArrayList().size() > 3)
        {
            this.addButton.setEnabled(false);
            this.generateButton.setEnabled(true);
        }
        else
        {
            this.addButton.setEnabled(true); 
            this.generateButton.setEnabled(false);
        }

        // sets the state of the remove button
        if(this.model.getSelectedArrayList().isEmpty())
        {
            this.removeButton.setEnabled(false);
        }
        else
        {
            this.removeButton.setEnabled(true);
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e) 
    {
        Object action = e.getSource();
        
        if(action == calenderSlider)
        {
            int selection = calenderSlider.getValue();
            updateCalenderView(model.getTimetableArrayList().get(selection));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {     
        Object action = e.getSource();
        
        if(action == addButton)
        {
            try{
                int selection = this.paperList.getSelectedIndex();

                if(this.model.checkPaperAdded(selection) == false)
                {
                    this.model.addPaperAtIndex(this.paperList.getSelectedIndex());
                    updateView();
                }
                else
                {
                     JOptionPane.showMessageDialog (null, "You have already added " 
                     + this.model.getPapersArrayList().get(selection).getPaperName(), "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch(Exception ex)
            {
                System.err.println("You pressed the add button whilst not selecting anything from the list!" + ex);
            }
            
        }
        else if(action == removeButton)
        {
            try{
                int selection = this.selectionList.getSelectedIndex();
                this.model.removePaperAtIndex(selection);
                updateView();
            }
            catch(Exception ex)
            {
                System.err.println("You pressed the remove button whilst not selecting anything from the selected list!" + ex);
            }
        }
        else if(action == infoButton)
        {
            String message = "This program was made by | Matt Ripia - 1385931 | Jaime King - 16959932\n\n"
                           + "The purpose is to display all posible timetables that do not clash, from 4 papers you have chosen\n\n"
                           + "The papers have been taken from the BCIS programme | AUT | Semester 2 | 2018";
            JOptionPane.showMessageDialog (null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(action == generateButton)
        {
            this.model.getTimetableArrayList().clear();
            this.model.getaTimetable().getOriginalPapers().clear();
            
            this.model.getaTimetable().getOriginalPapers().addAll(this.model.getSelectedArrayList());
            this.model.createAllTimetables();

            if(this.model.getTimetableArrayList().isEmpty())
            { 
                JOptionPane.showMessageDialog (this, "There were no timetables without clashes given this selection.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                updateCalenderView(this.model.getTimetableArrayList().get(0));
                this.calenderSlider.setValue(0);
                this.nextButton.setEnabled(true);
                this.previousButton.setEnabled(true);
            }
        }
        else if(action == nextButton)
        {
            calenderSlider.setValue(calenderSlider.getValue() + 1);
        }
        else if(action == previousButton)
        {
            calenderSlider.setValue(calenderSlider.getValue() - 1);
        }
        else if(action == resetButton)
        {
            this.model.getSelectedArrayList().clear();
            this.calenderLabel.setText("Your Generated Timetables will display here");
            this.timetableDetailsLabel.setText("Timetable Details will display here");
            
            // removes the four colored boxes in the timetables details
            for(int i = 0; i < 4; i++)
            {
                this.remove(colouredBoxes[i]);
            }
            this.calenderSlider.setEnabled(false);
            this.nextButton.setEnabled(false);
            this.previousButton.setEnabled(false);
            ClearCalenderView();
            updateView();
        }
    }

    private void doDrawing(Graphics g)
    {
        Graphics2D dottedLine = (Graphics2D) g.create();
        Graphics2D rectangle1 = (Graphics2D) g;
        Graphics2D rectangle2 = (Graphics2D) g;

        rectangle1.setColor(this.paperList.getBackground());
        rectangle1.fillRect(480, 60, 600, 299);
        
        rectangle2.setColor(this.paperList.getBackground());
        rectangle2.fillRect(265, 204, 200, 154);
        rectangle2.setColor(Color.GRAY);
        rectangle2.drawRect(265, 204, 199, 154);
        
        float[] dashMeasurements = {3f, 2f, 1f};

        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dashMeasurements, 2f);
        
        dottedLine.setStroke(stroke);
        dottedLine.setColor(Color.GRAY);
        int xOffset = 525;
        int yOffset = 88;

        for (int lineLoop = 0; lineLoop < 11; lineLoop++)
        {
            dottedLine.drawLine(xOffset, yOffset, xOffset, yOffset + 257);
            xOffset += 50;
        }

        dottedLine.dispose();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
}
