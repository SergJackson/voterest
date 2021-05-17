package ru.nomadin.voterest.web.restaurant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.nomadin.voterest.HasIdAndName;
import ru.nomadin.voterest.HasIdAndPhone;
import ru.nomadin.voterest.repository.RestaurantRepository;
import ru.nomadin.voterest.web.GlobalExceptionHandler;

@Component
@AllArgsConstructor
public class UniqueNameValidator implements org.springframework.validation.Validator {

    private final RestaurantRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndPhone.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndName restaurant = ((HasIdAndName) target);
        if (StringUtils.hasText(restaurant.getName())) {
            if (repository.getByName(restaurant.getName().toLowerCase())
                    .filter(u -> !u.getId().equals(restaurant.getId())).isPresent()) {
                errors.rejectValue("name", "", GlobalExceptionHandler.EXCEPTION_DUPLICATE_PHONE);
            }
        }
    }
}
