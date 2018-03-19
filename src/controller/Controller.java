package controller;

import exceptions.*;
import model.ProgramState;
import repository.RepositoryInterface;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {

    private RepositoryInterface repo;
    private ExecutorService executor;
    private ArrayList<ProgramState> debug_program_list = new ArrayList<>();
    private List<ProgramState> prgList;

    public Controller(RepositoryInterface repo)
    {
        this.repo = repo;
    }

    private Map<Integer,Integer> conservativeGarbageCollector(Collection<Integer> symTableValues, Map<Integer,Integer> heap)
    {
        return heap.entrySet().stream().filter(e->symTableValues.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList)
    {
        return inPrgList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    private List<ProgramState> debug_removeCompletedPrg(List<ProgramState> inPrgList)
    {
        for(ProgramState prg : inPrgList)
            if(!prg.isNotCompleted())
                this.debug_program_list.add(prg);
        return inPrgList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    private void logAllPrograms(List<ProgramState> prgList)
    {
        prgList.forEach(prg -> {
            try {
                repo.logProgramState(prg);
            } catch (FatalError fatalError) {
                throw new RuntimeException(fatalError.getMessage());
            }
        });
    }

    private void closeAllFiles(List<ProgramState> prgList)
    {
        prgList.forEach(p->p.getFileTable().clear());
    }

    private void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {
        List<Callable<ProgramState>> callList = prgList.stream().map((ProgramState p)->(Callable<ProgramState>)(p::oneStep)).collect(Collectors.toList());

        List<ProgramState> newProgramList = executor.invokeAll(callList).stream().map(future->{try {return future.get();}
        catch(ExecutionException e)
        {
            Throwable exp = e.getCause();
            String msg = exp.getMessage();
            throw new RuntimeException(msg);
        }
        catch (InterruptedException ignored)
        {
            // nothing to do here, as all our threads (should) work independently of each other.
        }
        return null;}).filter(Objects::nonNull).collect(Collectors.toList());

        prgList.addAll(newProgramList);

        repo.setProgramList(prgList);
    }

    private ArrayList<Integer> gatherAllValues(List<ProgramState> prgList)
    {
        ArrayList<Integer> result = new ArrayList<>();
        for(ProgramState prg : prgList)
            result.addAll(prg.getSymbolTable().values());
        return result;
    }

    public void allSteps() throws ControllerException
    {
        executor=Executors.newFixedThreadPool(2);

        prgList=removeCompletedPrg(repo.getProgramList().getContent());

        logAllPrograms(prgList);

        while(prgList.size() > 0)
        {
            try
            {
                oneStepForAllPrg(prgList);
            }
            catch (InterruptedException ignored)
            {
                // nothing to do here either
            }
            catch (RuntimeException r)
            {
                throw new ControllerException(r.getMessage());
            }

            prgList.get(0).getHeap().setContent(conservativeGarbageCollector(gatherAllValues(prgList), prgList.get(0).getHeap().getContent()));

            logAllPrograms(prgList);

            // no need to update in the repo just yet (just a waste of resources)

            prgList=removeCompletedPrg(repo.getProgramList().getContent());
        }

        executor.shutdownNow();

        closeAllFiles(prgList);

        // we finally update the repo
        repo.setProgramList(prgList);
    }

    public void gui_prepare_one_step()
    {
        executor=Executors.newFixedThreadPool(2);

        prgList=removeCompletedPrg(repo.getProgramList().getContent());

        logAllPrograms(prgList);
    }

    public boolean gui_one_step() throws ControllerException
    {
        boolean check = (prgList.size()!=0);

        if (!check) {
            executor.shutdownNow();

            closeAllFiles(prgList);

            repo.setProgramList(prgList);
        } else {
            try
            {
                oneStepForAllPrg(prgList);
            }
            catch (InterruptedException ignored) {}
            catch (RuntimeException r)
            {
                throw new ControllerException(r.getMessage());
            }

            prgList.get(0).getHeap().setContent(conservativeGarbageCollector(gatherAllValues(prgList), prgList.get(0).getHeap().getContent()));

            logAllPrograms(prgList);

            prgList=removeCompletedPrg(repo.getProgramList().getContent());
        }

        return check;
    }

    // used for testing and debugging only. does not remove completed programs as they finish. the only change is in the two lines that manage removing completed programs: another function that stores deleted programs temporarily is called.
    public void debug_all_steps() throws ControllerException
    {
        executor=Executors.newFixedThreadPool(2);

        prgList=debug_removeCompletedPrg(repo.getProgramList().getContent());

        logAllPrograms(prgList);

        while(prgList.size() > 0)
        {
            try
            {
                oneStepForAllPrg(prgList);
            }
            catch (InterruptedException ignored)
            {
            }
            catch (RuntimeException r)
            {
                throw new ControllerException(r.getMessage());
            }

            prgList.get(0).getHeap().setContent(conservativeGarbageCollector(gatherAllValues(prgList), prgList.get(0).getHeap().getContent()));

            logAllPrograms(prgList);

            prgList=debug_removeCompletedPrg(repo.getProgramList().getContent());
        }
        executor.shutdownNow();

        closeAllFiles(prgList);

        repo.setProgramList(debug_program_list);
    }

    public void gui_debug_prepare()
    {
        executor=Executors.newFixedThreadPool(2);

        prgList=debug_removeCompletedPrg(repo.getProgramList().getContent());

        logAllPrograms(prgList);
    }

    public boolean gui_debug_one_step() throws ControllerException
    {
        boolean check = (prgList.size()!=0);

        if (!check) {
            executor.shutdownNow();

            closeAllFiles(prgList);

            repo.setProgramList(debug_program_list);
        } else {
            try
            {
                oneStepForAllPrg(prgList);
            }
            catch (InterruptedException ignored) {}
            catch (RuntimeException r)
            {
                throw new ControllerException(r.getMessage());
            }

            prgList.get(0).getHeap().setContent(conservativeGarbageCollector(gatherAllValues(prgList), prgList.get(0).getHeap().getContent()));

            logAllPrograms(prgList);

            prgList=debug_removeCompletedPrg(repo.getProgramList().getContent());
        }

        return check;
    }

    public void logKittyException(String err) throws FatalError
    {
        repo.logKittyException(err);
    }

    public void clear() throws FatalError
    {
        this.repo.getProgramList().stream().forEach(ProgramState::clear);
        this.repo.clearLogFile();
    }
}