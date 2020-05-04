package listviewexample;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private int id;
    private List<Location> locations;

    public User(int id) {
        this.id = id;
        locations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void sortLocations() {
        locations.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void addAtListOfLocations(Date date, Place place, int x, int y) {
        locations.add(new Location(date, place, x, y));
    }

    @Override
    public String toString() {
        return ("user's id: " + id);
    }
}
