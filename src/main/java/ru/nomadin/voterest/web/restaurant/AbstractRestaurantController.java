package ru.nomadin.voterest.web.restaurant;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.nomadin.voterest.HasId;
import ru.nomadin.voterest.model.Restaurant;
import ru.nomadin.voterest.repository.RestaurantRepository;

import static ru.nomadin.voterest.util.validation.ValidationUtil.assureIdConsistent;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    private UniqueNameValidator nameValidator;

    @Autowired
    private LocalValidatorFactoryBean defaultValidator;

    @InitBinder("Restaurant")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(nameValidator);
    }

    public ResponseEntity<Restaurant> getWithUser(int id) {
        log.info("getWithUser {}", id);
        return ResponseEntity.of(repository.getWithUser(id));
    }

    protected void validateBeforeUpdate(HasId restaurant, int id) throws BindException {
        assureIdConsistent(restaurant, id);
        DataBinder binder = new DataBinder(restaurant);
        binder.addValidators(nameValidator, defaultValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}

