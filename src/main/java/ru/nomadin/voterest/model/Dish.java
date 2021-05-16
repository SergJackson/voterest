package ru.nomadin.voterest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.nomadin.voterest.util.validation.NoHtml;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "dishs", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "title", "date_menu"}, name = "dishs_unique_restaurant_title_datemenu_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant", "user"})
public class Dish extends AbstractBaseEntity {

    @Column(name = "title", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    @NoHtml
    private String title;

    @Column(name = "date_menu", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date dateMenu;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 1, max = 5000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public Dish(Integer id, String title, int price, Date dateMenu) {
        super(id);
        this.title = title;
        this.price = price;
        this.dateMenu = dateMenu;
    }
}
