package net.gmx.onebyte.ttnmapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logs {
    public static List<Log> parse(String csv) throws IOException {
        String lines[] = csv.split("\n");
        if (lines.length < 3) {
            throw new IOException("Malformed CSV");
        }
        ArrayList<Log> result = new ArrayList<>();
        for (int i = 1; i < lines.length - 2; i++) {
            String[] cells = lines[i].split(",\\s*");
            result.add(new Log(
                    Integer.parseInt(cells[0], 10),
                    cells[1],
                    cells[4],
                    new Location(
                            Double.parseDouble(cells[10]),
                            Double.parseDouble(cells[11]),
                            Double.parseDouble(cells[12])
                    ),
                    Double.parseDouble(cells[7]),
                    Double.parseDouble(cells[8]),
                    Double.parseDouble(cells[14]),
                    Integer.parseInt(cells[15], 10)
            ));
        }
        return result;
    }
}
