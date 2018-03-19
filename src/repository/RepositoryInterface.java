package repository;
import exceptions.FatalError;
import model.util.MyListInterface;
import model.ProgramState;

import java.util.List;

public interface RepositoryInterface
{
    void add(ProgramState prg);
    void logProgramState(ProgramState prg) throws FatalError;
    void clearLogFile() throws FatalError;
    void logKittyException(String err) throws FatalError;
    MyListInterface<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> states);
}
