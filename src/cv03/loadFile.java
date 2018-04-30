/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cv03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author alesf_000
 */
class loadFile {

    ArrayList<String> output;
    FileReader file;
    String separator;

    loadFile(FileReader file, String separator) {
        output = new ArrayList<>();
        this.file = file;
        this.separator = separator;
    }

    public void load() throws IOException {
        String line;
        BufferedReader bufferreader = new BufferedReader(file);
        line = bufferreader.readLine();
        output.add(line);
        while (line != null) {
            line = bufferreader.readLine();
            output.add(line);
        }
    }
}
