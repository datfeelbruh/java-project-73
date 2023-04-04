package hexlet.code.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * A class BaseModel has the fields necessary for each entity, all entity classes inherit this superclass.
 * @author sobadxx
 */
@Data
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;
}
