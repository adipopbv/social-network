package socialnetwork.domain.validators;

import socialnetwork.domain.Message;
import socialnetwork.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        boolean okTo = true;
        for (Long toId : entity.getTo())
            if (toId < 1000 || toId > 9999) {
                okTo = false;
                break;
            }
        if ((entity.getFrom() < 1000 || entity.getFrom() > 9999) ||
                !okTo ||
                entity.getTo().contains(entity.getFrom()) ||
                entity.getMessage().isEmpty())
            throw new ValidationException("invalid message data");
    }
}
