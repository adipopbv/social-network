package socialnetwork.domain.validators;

import socialnetwork.domain.Message;
import socialnetwork.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
//        if (entity.getFrom() < 1 || entity.getFriend1() > 9999 ||
//                entity.getFriend2() <1 || entity.getFriend2() > 9999)
//            throw new ValidationException("invalid friendship data");
    }
}
