/*
 * Sample.java
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
package nars.operators;

import java.util.ArrayList;

import nars.entity.Task;
import nars.storage.Memory;

/**
 * A class used as a template for Operator definition.
 */
public class Sample extends Operator {

    protected Sample() {
        super("^sample");
    }

    // called from Operator
    @Override
    protected ArrayList<Task> execute(ArrayList args, Memory memory) {
        System.out.println("Executed: " + this);
        for (Object t : args) {
            System.out.println(" --- " + t);;
        }
        return null;
    }

}
