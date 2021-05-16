package ru.nomadin.voterest.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.nomadin.voterest.HasIdAndPhone;
import ru.nomadin.voterest.util.validation.NoHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserTo extends BaseTo implements HasIdAndPhone, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @NoHtml
    String name;

    @NotBlank
    @Size(max = 10)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String phone;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    public UserTo(Integer id, String name, String phone, String password) {
        super(id);
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
}
