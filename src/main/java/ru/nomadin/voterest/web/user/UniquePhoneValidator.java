package ru.nomadin.voterest.web.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.nomadin.voterest.HasIdAndPhone;
import ru.nomadin.voterest.repository.UserRepository;
import ru.nomadin.voterest.web.GlobalExceptionHandler;

@Component
@AllArgsConstructor
public class UniquePhoneValidator implements org.springframework.validation.Validator {

    private final UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndPhone.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndPhone user = ((HasIdAndPhone) target);
        if (StringUtils.hasText(user.getPhone())) {
            if (repository.getByPhone(user.getPhone().toLowerCase())
                    .filter(u -> !u.getId().equals(user.getId())).isPresent()) {
                errors.rejectValue("phone", "", GlobalExceptionHandler.EXCEPTION_DUPLICATE_PHONE);
            }
        }
    }
}
