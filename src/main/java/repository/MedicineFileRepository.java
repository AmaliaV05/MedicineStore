package repository;

import com.google.gson.reflect.TypeToken;
import domain.Medicine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicineFileRepository extends JSONFileRepository<Medicine> {
    public MedicineFileRepository(String filename, Type type) {
        super(filename, type);
    }

    @Override
    protected void readFile() {
        super.readFile();
        try (FileReader in = new FileReader(filename)) {
            try (BufferedReader bufferedReader = new BufferedReader(in)) {

                Type listType = new TypeToken<ArrayList<Medicine>>(){}.getType();
                String contents = bufferedReader.lines().collect(Collectors.joining());
                List<Medicine> medicineList = gson.fromJson(contents, listType);

                for (Medicine obj : medicineList) {
                    entities.put(obj.getIdEntity(), obj);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in opening the BufferedWriter" + ex);
        }
    }
}
