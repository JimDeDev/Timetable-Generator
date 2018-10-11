/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetablegenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author gjr5542
 */
public class PaperDatabase 
{
    private ArrayList<Paper> paperArrayList;
    private ArrayList<Paper> selectedPapers;
    public ArrayList<Timetable> timetableArrayList = new ArrayList();

    private Timetable aTimetable = new Timetable();
    private final String sqlUrl = "jdbc:derby:TimeTableDB; create=true";

    public boolean checkPaperAdded(int selection)
    {
        boolean clashes = false;
        
        for(Paper aPaper : selectedPapers)
        {
            if(aPaper.getPaperCode().equals(paperArrayList.get(selection).getPaperCode()))
            {
                clashes = true;
                break;
            }
        }
        return clashes;
    }
    
    public ArrayList<Paper> getPapersArrayList(){
        return this.paperArrayList;
    }
    
    public ArrayList<Paper> getSelectedArrayList(){
        return this.selectedPapers;
    }
    
    public void addPaperAtIndex(int i)
    {
        selectedPapers.add(this.paperArrayList.get(i));
    }
    
    public void removePaperAtIndex(int i)
    {
        selectedPapers.remove(i);
    }
    
    public PaperDatabase()
    {
        this.paperArrayList = new ArrayList();
        this.selectedPapers = new ArrayList();
        this.readInDB();
    }

    private void readInDB() 
    {
        try {
            // statement.executeUpdate("update session set end_time = 14 where stream_no = 50 and day = 'TUE' "
            // + "and paper_id = (select paper_id from paper where paper_code = 'ENEL611')");
            // variable initilization
            String queryPaper, queryStream, querySession, paper_code, paper_name, stream_no, room = "", day = "";
            
            float start_time = 0, end_time = 0;
            int totalPapers = 0, totalStreams = 0;

            Paper aPaper = null;
            Stream aStream = null;
            Session aSession = null;
            Time aTime = null;
            
            ResultSet paperResults, sessionResults, streamResults, rowResults;
      
            // connecting to the db
            Connection conn = DriverManager.getConnection(sqlUrl);
            Statement statement = conn.createStatement();

            // finds how many papers there are in the database, uses this to build objects and loop thorugh each entry
            rowResults = statement.executeQuery("select count(*) from paper");
            while(rowResults.next())
            {
                totalPapers = Integer.parseInt(rowResults.getString(1));
            }

            // for each paper in the batabase
            for(int currentPaper = 1; currentPaper <= totalPapers; currentPaper++)
            {
                queryPaper = "select paper_name, paper_code from paper where paper_id = " + Integer.toString(currentPaper);
                
                // get the main paper details
                paperResults = statement.executeQuery(queryPaper);
                while(paperResults.next())
                {
                    paper_name = paperResults.getString(1);
                    paper_code = paperResults.getString(2);
                    aPaper = new Paper(paper_name, paper_code);
                }
                
                // counts how many streams there are in the current paper
                rowResults = statement.executeQuery("select count(*) from stream where paper_id = " + Integer.toString(currentPaper));
                while(rowResults.next())
                {
                    totalStreams = Integer.parseInt(rowResults.getString(1));
                }
                
                // for each stream in the current paper
                for(int currentStream = 0 ; currentStream < totalStreams; currentStream++)
                {
                    queryStream = "select stream_no from stream where paper_id = " + Integer.toString(currentPaper)
                                 +" and stream_no = " + Integer.toString(currentStream + 50);
                    
                    // get the stream number that we are looking at, create a stream object with that number
                    streamResults = statement.executeQuery(queryStream);
                    while(streamResults.next())
                    {
                        stream_no = streamResults.getString(1);
                        aStream = new Stream(Integer.parseInt(stream_no));
                    }

                    querySession = "select room, day, start_time, end_time from session where paper_id = " + Integer.toString(currentPaper)
                                        + " and stream_no = " + Integer.toString(currentStream + 50);

                    sessionResults = statement.executeQuery(querySession);
                    while(sessionResults.next())
                    {
                        room = sessionResults.getString(1);
                        day = sessionResults.getString(2); 
                        start_time = sessionResults.getFloat(3);
                        end_time = sessionResults.getFloat(4);
                        aTime = new Time(start_time, end_time, day);
                        aSession = new Session(aTime, room);
                        aStream.addSession(aSession);
                    }
                            
                    aPaper.addStream(aStream);
                }
                paperArrayList.add(aPaper);
            }
            conn.close();
            statement.close();
            
        }catch (Exception ex) {
            
           System.err.println("error reading DB | " + ex);
        }

    }
    
    public String[] getPaperTitles(ArrayList<Paper> paperArrayList){
        
        String[] paperTitles = new String[paperArrayList.size()];
        
        for(int i = 0; i < paperArrayList.size(); i++)
        {
            paperTitles[i] = paperArrayList.get(i).getPaperName();
        }
        
        return paperTitles;
    }
    
        /**
     * Method will take a timetable object and check if there are any clashing sessions 
     * @param aTimetable
     * @return boolean -  true if there is a clash in the timetable
     */
    public static boolean isClash(Timetable aTimetable)
    {
        boolean isClash = false;
        
            Paper paper1, paper2;
            
            for (int paper1Index = 0; paper1Index < 4; paper1Index++)
            {
                paper1 = aTimetable.getNewPaperAtIndex(paper1Index);

                for (int paper2Index = paper1Index + 1; paper2Index < 4; paper2Index++)
                {
                    paper2 = aTimetable.getNewPaperAtIndex(paper2Index);

                    for(Session paper1Session : paper1.getStreamAtIndex(0).getSessions())
                    {
                        for(Session paper2Session : paper2.getStreamAtIndex(0).getSessions())
                        {
                            if(paper1Session.getTime().getDay().toLowerCase().contains(paper2Session.getTime().getDay().toLowerCase()))
                            {
                                if(paper1Session.getTime().getStartTime() >= paper2Session.getTime().getStartTime() &&
                                   paper1Session.getTime().getStartTime() <  paper2Session.getTime().getEndTime())
                                {
                                    isClash = true;
                                }
                                else if(paper1Session.getTime().getEndTime() >  paper2Session.getTime().getStartTime() &&
                                        paper1Session.getTime().getEndTime() <= paper2Session.getTime().getEndTime())

                                {
                                    isClash = true;
                                }
                            }
                        }
                    }
                }
            }
        
            return isClash;
    }

    /**
     * this method creates all possible timetables
     */
    public void createAllTimetables(){
        
        int timetableCounter = 1;
        
        for(int a = 0; a < aTimetable.getPaperAtIndex(0).getStream().size(); a++)
        {
            for(int b = 0; b < aTimetable.getPaperAtIndex(1).getStream().size(); b++)
            {
                for(int c = 0; c < aTimetable.getPaperAtIndex(2).getStream().size(); c++)
                {
                    for(int d = 0; d < aTimetable.getPaperAtIndex(3).getStream().size(); d++)
                    {
                        Timetable tempTimetable = new Timetable(timetableCounter);
                        tempTimetable.createPerms(a, b, c, d);

                        // if there is no clash, parse in the timetable onject into an arraylist of timetables to be printed
                        boolean clash = isClash(tempTimetable);
                        if(clash == false)
                        {
                            timetableArrayList.add(tempTimetable);
                            timetableCounter++;
                        }
                    }
                }
            }
        }
    }

    public Timetable getaTimetable() {
        return aTimetable;
    }

    public void setaTimetable(Timetable aTimetable) {
        this.aTimetable = aTimetable;
    }

    /**
     * @return the timetableArrayList
     */
    public ArrayList<Timetable> getTimetableArrayList() {
        return timetableArrayList;
    }

    /**
     * @param timetableArrayList the timetableArrayList to set
     */
    public void setTimetableArrayList(ArrayList<Timetable> timetableArrayList) {
        this.timetableArrayList = timetableArrayList;
    }
    
}
