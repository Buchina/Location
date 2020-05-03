package listviewexample;

public class Organization {

    private String name;
    private String bossName;
    private int personnel;

    public Organization(String name, String bossName, int personnel) {
        this.name = name;
        this.bossName = bossName;
        this.personnel = personnel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public int getPersonnel() {
        return personnel;
    }

    public void setPersonnel(int personnel) {
        this.personnel = personnel;
    }

    public boolean isTheSameBoss(Organization t) {
        return bossName.equals(t.bossName);
    }

    @Override
    public  String toString() {
        return name;
    }

}
