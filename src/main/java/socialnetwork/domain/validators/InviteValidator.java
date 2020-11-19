package socialnetwork.domain.validators;

import socialnetwork.domain.Invite;
import socialnetwork.domain.exceptions.ValidationException;

public class InviteValidator implements Validator<Invite> {
    @Override
    public void validate(Invite entity) throws ValidationException {
//        if (entity.getFirstName().isEmpty() || entity.getLastName().isEmpty())
//            throw new ValidationException("invalid user data");
    }
}
