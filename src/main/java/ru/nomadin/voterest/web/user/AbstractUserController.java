package ru.nomadin.voterest.web.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.nomadin.voterest.HasId;
import ru.nomadin.voterest.model.User;
import ru.nomadin.voterest.repository.UserRepository;
import ru.nomadin.voterest.util.UserUtil;

import static ru.nomadin.voterest.util.validation.ValidationUtil.assureIdConsistent;
import static ru.nomadin.voterest.util.validation.ValidationUtil.checkSingleModification;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniquePhoneValidator phoneValidator;

    @Autowired
    private LocalValidatorFactoryBean defaultValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(phoneValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    //@CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        checkSingleModification(repository.delete(id), "User id=" + id + " not found");
    }

    public ResponseEntity<User> getWithVotes(int id) {
        log.info("getWithVotes {}", id);
        return ResponseEntity.of(repository.getWithVotes(id));
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(phoneValidator, defaultValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}

