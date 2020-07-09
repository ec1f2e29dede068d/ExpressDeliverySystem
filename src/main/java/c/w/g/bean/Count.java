package c.w.g.bean;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "count")
public class Count implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Deliverer.class)
    @JoinColumn(name = "delivererId", referencedColumnName = "id")
    private Deliverer deliverer;
    private Integer work;
    private Integer rest;
    private Integer workOverTime;
    private Integer month;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }

    public Integer getWork() {
        return work;
    }

    public void setWork(Integer work) {
        this.work = work;
    }

    public Integer getRest() {
        return rest;
    }

    public void setRest(Integer rest) {
        this.rest = rest;
    }

    public Integer getWorkOverTime() {
        return workOverTime;
    }

    public void setWorkOverTime(Integer workOverTime) {
        this.workOverTime = workOverTime;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
