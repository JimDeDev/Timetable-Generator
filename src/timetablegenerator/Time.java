package timetablegenerator;

import java.text.DecimalFormat;

/**
 * Each session has a start and end time as well as a day
 * @author 1385931, 16959932
 */
class Time {
    
    private float startTime;
    private float endTime;
    private String day;
    
    public Time(float start, float end, String day){
        this.startTime = start;
        this.endTime = end;
        this.day = day;
    }
    
    /**
     * Default Constructor
     */
    public Time(){
        this.startTime = 0.0f;
        this.endTime = 999.9f;
        this.day = "day unknown";
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    
    @Override
    public String toString(){
        
        DecimalFormat timeFormat = new DecimalFormat("##.##");
        
        return getDay() + " " + timeFormat.format(getStartTime()) + " to " + timeFormat.format(getEndTime());
    }
}
