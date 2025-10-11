package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MedCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new MedCommand object
 */
public class MedCommandParser implements Parser<MedCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MedCommand
     * and returns a MedCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MedCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new MedCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MedCommand.MESSAGE_USAGE), pe);
        }
    }

}
