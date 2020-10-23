package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UtilizatorFile;

public class Main {
    public static void main(String[] args) {
        String fileName="data/users.csv";
        Repository<Long,Utilizator> userFileRepository = new UtilizatorFile(fileName
                , new UtilizatorValidator());

        userFileRepository.findAll().forEach(System.out::println);

    }
}


