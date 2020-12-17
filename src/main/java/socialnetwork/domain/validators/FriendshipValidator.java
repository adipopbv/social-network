package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getFirstFriendId().getValue() < 1 || entity.getFirstFriendId().getValue() > 9999 ||
                entity.getSecondFriendId().getValue() < 1 || entity.getSecondFriendId().getValue() > 9999)
            throw new ValidationException("invalid friendship data");
    }
}
