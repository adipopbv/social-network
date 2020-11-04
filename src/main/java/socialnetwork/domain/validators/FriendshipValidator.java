package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getFriend1() < 1 || entity.getFriend1() > 9999 ||
                entity.getFriend2() <1 || entity.getFriend2() > 9999)
            throw new ValidationException("invalid friendship data");
    }
}
