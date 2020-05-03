package listviewexample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Location {
    private Date date;
    private Place place;
    private int x;
    private int y;

    public Location(Date date, Place place, int x, int y) {
        this.date = date;
        this.place = place;
        this.x = x;
        this.y = y;
    }


    public Date getDate() {
        return date;
    }

    public Place getPlace() {
        return place;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return ("Дата: " + date + " Название:" + place.getName() + "Координаты" + x + " " + y);
    }
}
