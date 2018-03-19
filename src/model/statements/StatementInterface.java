package model.statements;

import exceptions.DivideByZero;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.ProgramState;

public interface StatementInterface
{
    String toString();
    ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero, FatalError;
}
