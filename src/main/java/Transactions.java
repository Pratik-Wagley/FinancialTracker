import java.sql.Time;
import java.util.Date;

public class Transactions {
    private Date date;
    private Time time;
    private String discription;
    private String vendor;

    public Transactions(Date date, Time time, String discription, String vendor) {
        this.date = date;
        this.time = time;
        this.discription = discription;
        this.vendor = vendor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
