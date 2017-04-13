package logic;

import java.util.Date;

/**
 * Класс-сущность "период" - временной промежуток, за который необходимо отразить счета
 */
public class Period {
    private Date startDate;
    private Date endDate;

    public Period() {}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
