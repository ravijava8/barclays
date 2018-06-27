package com.barclays.input;

import com.barclays.constants.Gate;
import com.barclays.pojos.LuggageBag;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class LuggageBagHandler implements InputHandler {

    private Map<String, LuggageBag> bagIdToBagMap;

    public void process() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        Scanner scanner = null;
        try{
            scanner = new Scanner(file);
            boolean startBagSection = false;
            boolean endBagSection = false;

            while (scanner.hasNextLine() && !endBagSection) {
                String line = scanner.nextLine();

                if (line.trim().equals("")){
                    continue;
                }

                if (line.startsWith("# Section:")) { //start of a new section
                    if (!line.endsWith("Bags")) { //skip
                        startBagSection = false;
                        continue;
                    } else if (line.endsWith("Bags")) {
                        startBagSection = true;
                        bagIdToBagMap = new LinkedHashMap<String, LuggageBag>();
                        continue;
                    } else if (startBagSection && !line.endsWith("Bags")) {
                        endBagSection = true;
                    }
                }

                if (startBagSection && !endBagSection) {
                    //Format: <bag_number> <entry_point> <flight_id>
                    String tokens[] = line.split(" ");
                    if (tokens.length != 3) {
                        throw new IOException("BAD INPUT FORMAT...");
                    }
                    String bagId = tokens[0];
                    Gate entryGate = Gate.getGate(tokens[1]);
                    String flightId = tokens[2];


                    if (entryGate == null) {
                        throw new IOException("INVALID ENTRY GATE FOUND...");
                    }

                    bagIdToBagMap.put(bagId, new LuggageBag(bagId, entryGate, flightId));
                }

            }
        }finally {
            scanner.close();
        }
    }

    public Map<String, LuggageBag> getBagIdToBagMap() {
        return bagIdToBagMap;
    }

    public void setBagIdToBagMap(Map<String, LuggageBag> bagIdToBagMap) {
        this.bagIdToBagMap = bagIdToBagMap;
    }
}
