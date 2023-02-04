package domain;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class MedicineValidator {

    public void validateMedicine(Medicine checkMedicine) throws MedicineException {
        try {
            BigDecimal checkPrice = checkMedicine.getPrice();
            int checkStock = checkMedicine.getStock();
            if (checkPrice.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new MedicineException("The medicine price should be greater than 0\n");
            }
            if (checkStock <= 0) {
                throw new MedicineException("The medicine stock should be greater than 0\n");
            }
        } catch(IllegalArgumentException i) {
            System.out.println("Wrong input");
        }
    }
}
