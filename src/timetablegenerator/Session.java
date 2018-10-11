package timetablegenerator;

/**
 * Sessions consist of a room and a time object
 * @author 1385931, 16959932
 */

public class Session {
 
    private String room;
    private Time time;
   
    public Session(Time time, String room){
        this.room = room;
        this.time = time;
    }

    public Session() {
        
        Time newTime = new Time();
        this.time = newTime;
        this.room = "room unknown";
    }
    
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
    
    @Override
    public String toString(){
        
        return getTime().toString() + " in room " + getRoom();
    }
}
