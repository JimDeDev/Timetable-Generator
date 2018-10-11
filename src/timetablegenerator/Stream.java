package timetablegenerator;

import java.util.ArrayList;

/**
 * Every paper has an array of Stream objects 
 * each stream has a stream number and a list of sessions
 * @author 1385931, 16959932
 */

class Stream {

    private ArrayList<Session> sessions;
    private Integer streamNumber;

    public Stream(ArrayList<Session> sessions, Integer streamNumber) {
        this.sessions = sessions;
        this.streamNumber = streamNumber;
    }
    
    public Stream(Session session, Integer sNumber){
        
        this.sessions = new ArrayList<>();
        sessions.add(session);
        this.streamNumber = sNumber;
    }
    
    public Stream(int streamNo)
    {
        this.streamNumber = streamNo;
        sessions = new ArrayList();
    }

    public Stream() {
        
        Session newSession = new Session();
        this.sessions = new ArrayList<>();
        
        this.sessions.add(newSession);
        this.streamNumber = 0;
    }
    
    public void addSession(Session parsedSession){
        sessions.add(parsedSession);
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }
    
    public Session getSessionAtIndex(int i){
        return this.sessions.get(i);
    }
    
    public int getSessionLength(){
        return this.sessions.size();
    }

    public Integer getStreamNumber() {
        return streamNumber;
    }

    public void setStreamNumber(Integer streamNumber) {
        this.streamNumber = streamNumber;
    }
    
    @Override
    public String toString(){
        
        String temp = "";
        
        for(int i = 0; i < getSessions().size(); i++)
        {
            temp += getSessions().get(i).toString() + "\n";
        }
        
        return  "StreamNo: " +getStreamNumber() + "\n" + "Session details:\n" + temp;
    }
}
