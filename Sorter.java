import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class Sorter {
    public File sortFile(File dataFile) throws IOException {
        File a = new File(dataFile.getPath());
        File b = new File(a.getParent(), "b.txt");
        File c = new File(a.getParent(), "c.txt");
        long iteration = 1;      

        while (true) {
            if (split(a, b, c, iteration) == 1) {
                break;
            } else {
               iteration = merge(a, b, c, iteration);
            }
            
            System.gc();
        }

        Files.delete(b.toPath());
        Files.delete(c.toPath());
        
        return a;
    }
    
    private long split(File a, File b, File c, long iteration) throws IOException {
        long resault = 1;
        long count = 0;
        long length = getLongsQty(a);
        long pointer = 0;
        byte factor = 1;
        
        if (length < 2) {
            return resault;
        }

        try (Scanner scanner = new Scanner(new FileInputStream(a));
                PrintWriter writerB = new PrintWriter(b);
                    PrintWriter writerC = new PrintWriter(c)){
            while (pointer < length) {
                if (count == iteration) {
                    count = 0;                                        
                    resault++;
                    factor *= -1;
                }

                long number = scanner.nextLong();
                pointer++;

                if (factor == 1) {
                    writerB.println(number);
                } else {
                    writerC.println(number);
                }

                count++;                
            }
            
            writerB.flush();
            writerC.flush();
        }

        return resault;
    }

    private long merge(File a, File b, File c, long iteration) throws IOException {
        long lengthB = getLongsQty(b);
        long lengthC = getLongsQty(c);
        long countB = iteration;
        long countC = iteration;
        long number1 = 0;
        long number2 = 0;
        long pointerB = 0;
        long pointerC = 0;

        boolean isGotB = false;
        boolean isGotC = false;
        boolean isBEnds = false;
        boolean isCEnds = false;
        
        try (PrintWriter writer = new PrintWriter(a);
                Scanner scannerB = new Scanner(new FileInputStream(b));
                    Scanner scannerC = new Scanner(new FileInputStream(c))) {
            while (!isBEnds || !isCEnds) {
                if (countB == 0 && countC == 0) {
                    countB = iteration;
                    countC = countB;
                }

                if (pointerB < lengthB) {
                    if (countB > 0 && !isGotB) {
                        number1 = scannerB.nextLong();
                        isGotB = true;
                        pointerB++;
                    }
                } else {
                    isBEnds = true;
                }

                if (pointerC < lengthC) {
                    if (countC > 0 && !isGotC) {
                        number2 = scannerC.nextLong();
                        isGotC = true;
                        pointerC++;
                    }
                } else {
                    isCEnds = true;
                }

                if (isGotB) {
                    if (isGotC) {
                        if (number1 < number2) {
                            writer.println(number1);
                            isGotB = false;
                            countB--;
                        } else {
                            writer.println(number2);
                            isGotC = false;
                            countC--;
                        }
                    } else {
                        writer.println(number1);
                        isGotB = false;
                        countB--;
                    }
                } else if (isGotC) {
                    writer.println(number2);
                    isGotC = false;
                    countC--;
                }
            }

            writer.flush();
        }

        return iteration * 2;
    }
    
    private long getLongsQty(File a) throws IOException {
        long resault = 0;

        try (Scanner scanner = new Scanner(new FileInputStream(a))) {
            while (scanner.hasNextLong()) {
                scanner.nextLong();
                resault++;
            }
        }

        return resault;
    }
}
