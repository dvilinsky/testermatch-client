public class TesterDTO {
    private String firstName;
    private String lastName;
    private int numBugs;

    public TesterDTO() {}

    public int getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(int numBugs) {
        this.numBugs = numBugs;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
