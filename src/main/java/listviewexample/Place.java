package listviewexample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Place {
    private String name;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private List<Frequency> frequency;
    private int count;

    public Place(String name, int x1, int y1, int x2, int y2) {
        this.name = name;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        frequency = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public List<Frequency> getFrequency() {
        return frequency;
    }

    public void addAtListFrequency(Date date) {
        frequency.add(new Frequency(date));
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public int getCount() {
        return count;
    }

    public void addCount() {
        count++;
    }

    public boolean inThisPlace(int x, int y) {
        if ((x >= x1 && x <= x2 && y >= y1 && y <= y2) ||
                (x <= x1 && x >= x2 && y <= y1 && y >= y2) ||
                (x >= x1 && x <= x2 && y <= y1 && y >= y2) ||
                (x <= x1 && x >= x2 && y >= y1 && y <= y2))
            return true;
        return false;
    }
}
