package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Id;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.MemoryRepository;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<E extends Entity> extends MemoryRepository<E> {
    protected String fileName;

    public AbstractFileRepository(String fileName) {
        this.fileName=fileName;
        loadData();
    }

    protected void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                E entity=extractEntity(Arrays.asList(line.split(";")));
                super.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e = super.save(entity);
        if (e == null)
            writeToFile(entity);
        return e;
    }

    @Override
    public E delete(Id id) {
        E e = super.delete(id);
        if (e != null)
            rewriteToFile();
        return e;
    }

    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewriteToFile(){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : entities.values()) {
                bW.write(createEntityAsString(entity));
                bW.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        rewriteToFile();
    }
}

