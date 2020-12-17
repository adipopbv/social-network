package socialnetwork.domain.validators;

import socialnetwork.domain.Id;
import socialnetwork.domain.Message;
import socialnetwork.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        boolean okTo = true;
        for (Id toId : entity.getToIds())
            if (toId.getValue() < 1000 || toId.getValue() > 9999) {
                okTo = false;
                break;
            }
        if ((entity.getFromId().getValue() < 1000 || entity.getFromId().getValue() > 9999) ||
                !okTo ||
                entity.getTo().contains(entity.getFrom()) ||
                entity.getText().isEmpty())
            throw new ValidationException("invalid message data");
    }
}
