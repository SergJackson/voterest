package ru.nomadin.voterest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ru.nomadin.voterest.HasIdAndName;
import ru.nomadin.voterest.util.validation.NoHtml;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "restaurants") //, uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurants_unique_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"user"})
public class Restaurant extends AbstractBaseEntity implements HasIdAndName {

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    @Size(min = 2, max = 120)
    @NoHtml
    private String name;

    @Column(name = "date_ins", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date dateIns;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;


    public Restaurant(Restaurant u) {
        this(u.getId(), u.getName(), u.getDateIns());
    }

    public Restaurant(Integer id, String name) {
        this(id, name, new Date());
    }

    public Restaurant(Integer id, String name, Date dateIns) {
        super(id);
        this.name = name;
        this.dateIns = dateIns;
    }
}
