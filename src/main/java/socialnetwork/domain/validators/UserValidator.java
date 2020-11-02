package socialnetwork.domain.validators;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.ValidationException;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if (entity.getFirstName().isEmpty() || entity.getLastName().isEmpty())
            throw new ValidationException("invalid user data");
    }
}
