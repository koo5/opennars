/*
 * ExperienceReader.java
 *
 * Copyright (C) 2008  Pei Wang
 *
 * This file is part of Open-NARS.
 *
 * Open-NARS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Open-NARS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open-NARS.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.io;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import nars.main_nogui.NAR;

/**
 * To read and write experience as Task streams
 */
public class ExperienceReader implements InputChannel {

    /**
     * Reference to the reasoner
     */
    private NAR reasoner;
    /**
     * Input experience from a file
     */
    private BufferedReader inExp;
    /**
     * Remaining working cycles before reading the next line
     */
    private int timer;

    /**
     * Default constructor
     *
     * @param reasoner Backward link to the reasoner
     */
    public ExperienceReader(NAR reasoner) {
        this.reasoner = reasoner;
        inExp = null;
    }

    /**
     * Open an input experience file with a FileDialog
     */
    public void openLoadFile() {
        FileDialog dialog = new FileDialog((FileDialog) null, "Load experience", FileDialog.LOAD);
        dialog.setVisible(true);
        String directoryName = dialog.getDirectory();
        String fileName = dialog.getFile();
        String filePath = directoryName + fileName;
        openLoadFile(filePath);
    }

    /**
     * Open an input experience file from given file Path
     *
     * @param filePath File to be read as experience
     */
    public void openLoadFile(String filePath) {
        try {
            inExp = new BufferedReader(new FileReader(filePath));
        } catch (IOException ex) {
            System.out.println("i/o error: " + ex.getMessage());
        }
        reasoner.addInputChannel(this);
    }

    /**
     * Close an input experience file (close the reader in fact)
     */
    public void closeLoadFile() {
        try {
            inExp.close();
        } catch (IOException ex) {
            System.out.println("i/o error: " + ex.getMessage());
        }
        reasoner.removeInputChannel(this);
    }

    public void setBufferedReader(BufferedReader inExp) {
        this.inExp = inExp;
        reasoner.addInputChannel(this);
    }

    /**
     * Process the next chunk of input data;
     * TODO some duplicated code with
     * {@link nars.gui.InputWindow#nextInput()}
     *
     * @return Whether the input channel should be checked again
     */
    @Override
    public boolean nextInput() {
        if (timer > 0) {
            timer--;
            return true;
        }
        if (inExp == null) {
            return false;
        }
        String line = null;
        while (timer == 0) {
            try {
                line = inExp.readLine();
                if (line == null) {
                    inExp.close();
                    inExp = null;
                    return false;
                }
            } catch (IOException ex) {
                reasoner.output("i/o error: " + ex.getMessage());
            }
            
            parse(line);
        }
        return true;
    }
    
    public void parse(String line) {
        // read NARS language or an integer
        if (line.trim().length() > 0) {
            try {
                timer = Integer.parseInt(line);
                reasoner.output("Thinking... (" + timer + " cycles)");
                reasoner.walk(timer);
            } catch (NumberFormatException e) {
                try {
                    reasoner.textInputLine(line);
                }
                catch (StringParser.InvalidInputException ep) {
                    reasoner.output("Parse Error: " + ep.toString() + "\n" + Arrays.asList(ep.getStackTrace()) );
                }
            }
        }       
    }
}
