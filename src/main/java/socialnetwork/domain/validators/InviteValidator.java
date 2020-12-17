package socialnetwork.domain.validators;

import socialnetwork.domain.Invite;
import socialnetwork.domain.exceptions.ValidationException;

public class InviteValidator implements Validator<Invite> {
    @Override
    public void validate(Invite entity) throws ValidationException {
        if (entity.getFrom() == null || entity.getTo() == null)
            throw new ValidationException("invalid invite data");
    }
}
