package models.db.math;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "math_sum")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Sum extends Model {

    public Sum(Integer numberOne, Integer numberTwo, Integer result, Date instant) {
        this.numberOne = numberOne;
        this.numberTwo = numberTwo;
        this.result = result;
        this.instant = instant;
    }

    @Id
    private Long id;

    @Column(nullable = false)
    private Integer numberOne;

    @Column(nullable = false)
    private Integer numberTwo;

    @Column(nullable = false)
    private Integer result;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private Date instant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOne() {
        return numberOne;
    }

    public void setNumberOne(Integer numberOne) {
        this.numberOne = numberOne;
    }

    public Integer getNumberTwo() {
        return numberTwo;
    }

    public void setNumberTwo(Integer numberTwo) {
        this.numberTwo = numberTwo;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getInstant() {
        return instant;
    }

    public void setInstant(Date instant) {
        this.instant = instant;
    }
}
