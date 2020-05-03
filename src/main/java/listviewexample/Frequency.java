package listviewexample;

import java.util.Date;

public class Frequency {
    private Date date;
    private int count;

    public Frequency(Date date) {
        this.date = date;
        count = 1;
    }

    public Date getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public void add() {
        count++;
    }

    @Override
    public String toString() {
        return ("Дата: " + date + " Кол-во: " + count);
    }
}
