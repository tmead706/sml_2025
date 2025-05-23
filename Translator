package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import lombok.extern.java.Log;

import sml.instructions.*;

/**
 * This class is the main translation mechanism.
 * <p>
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 *
 * @author KLM and xxx
 */
 @Log
public final class Translator {

    private static final String PATH = "";

    // word + line is the part of the current line that's not yet processed
    // word has no whitespace
    // If word and line are not empty, line begins with whitespace
    private final String fileName; // source file of SML code
    private String line = "";

    public Translator(final String file) {
        //fileName = PATH + file;
				fileName = PATH + file;
    }

    // translate the small program in the file into lab (the labels) and
    // prog (the program)
    // return "no errors were detected"

    public boolean readAndTranslate(final Labels lab, final List<Instruction> prog) {
        try (var sc = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            // Scanner attached to the file chosen by the user
            // The labels of the program being translated
            lab.reset();
            // The program to be created
            prog.clear();

            try {
                line = sc.nextLine();
            } catch (NoSuchElementException ioE) {
                return false;
            }

            // Each iteration processes line and reads the next input
            // line into "line"
            while (line != null) {
                // Store the label in label
                var label = scan();

                if (label.length() > 0) {
                    var ins = getInstruction(label);
                    if (ins != null) {
                        lab.addLabel(label);
                        prog.add(ins);
                    }
                }

                try {
                    line = sc.nextLine();
                } catch (NoSuchElementException ioE) {
                    return false;
                }
            }
        } catch (IOException ioE) {
            System.err.println("File: IO error " + ioE);
            return false;
        }
        return true;
    }

    // The input line should consist of an SML instruction, with its label already removed.
    // Translate line into an instruction with label "label" and return the instruction.
    public Instruction getInstruction(final String label) {
        /* Commented removal of original switch implementation

        if (line.equals("")) {
            return null;
        }
        var opCode = scan();
				Instruction instruction = null;

        switch (opCode){
            case "add":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
              instruction =  new AddInstruction(label, r, s1, s2);
							break;
            
						// TODO: You have to write code here for the other instructions.

						case "lin":
								r = scanInt();
								s1 = scanInt();
								//s2 = scanInt();
            instruction = new LinInstruction(label, r, s1);
						break;
						
						case "sub":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instruction = new SubInstruction(label, r, s1, s2);
								break;
            
						case "mul" :
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instruction = new MulInstruction(label, r, s1, s2);
								break;
            
						case "div":
                r = scanInt();
                s1 = scanInt();
                s2 = scanInt();
                instruction = new DivInstruction(label, r, s1, s2);
								break;
            
						case "bnz":
                //r = scanInt();
                s1 = scanInt();
                String branchLabel = scan();
                instruction = new BnzInstruction(label, s1, branchLabel);
								break;
            
						case "out":
                //r = scanInt();
                s1 = scanInt();
                instruction = new OutInstruction(label, s1);
								break;
            

            default:
							System.err.println("Unknown instruction: " + opCode);
							instruction = null;
							 break; // Eliminated the return null by using break and return null instruction
        		}
						return instruction;
        */

      // // In the second phase you will replace the switch with...
        // return returnInstruction(label, opCode);
        if (line.equals("")) {
            return null;
        }
        
        var opCode = scan();
        return returnInstruction(label, opCode);
    }

        // In the second phase you will replace the switch with...
        // return returnInstruction(label, opCode);

    /*
     * Return the first word of line and remove it from line. If there is no word,
     * return ""
     */
    private String scan() {
        line = line.trim();
        if (line.length() == 0) {
            return "";
        }

        int i = 0;
        while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
            i = i + 1;
        }
        String word = line.substring(0, i);
        line = line.substring(i);
        return word;
    }

    // Return the first word of line as an integer. 
    // If there is any error, return the maximum int
    private int scanInt() {
        String word = scan();
        if (word.length() == 0) {
            return Integer.MAX_VALUE;
        }

        try {
            return Integer.parseInt(word);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    private Instruction returnInstruction(final String label, String opCode) {
        String pkg = "sml.instructions"; // hardwired = bad!
        String base = "Instruction"; // ditto

        // Transform add -> sml.instructions.AddInstruction
        String fqcn = pkg + "." + opCode.substring(0, 1).toUpperCase(Locale.ROOT) + opCode.substring(1) + base;

        // get the class
        Class<?> clazz;
        try {
            clazz = Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown instruction: " + fqcn);
            return null;
        }

        // find the correct constructor
        Constructor<?> cons = findConstructor(clazz);
        if (cons == null) {
            System.err.println("No suitable constructor found for: " + fqcn);
            return null;
        }
        
        var objArray = argsForConstructor(cons, label);
        if (objArray == null) {
            System.err.println("Could not create arguments for constructor: " + fqcn);
            return null;
        }

        try {
            return (Instruction) cons.newInstance(objArray); // create an instance with the ctor args
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NullPointerException e) {
            log.severe(String.format("In %s: issue with creating the instruction %s: %s", 
                this.getClass().getSimpleName(), fqcn, e.getMessage()));
        }
        return null; // bad!!!
    }

    private Constructor<?> findConstructor(Class<?> cl) {
        Constructor<?>[] constructors = cl.getConstructors();
        
        // Look for a constructor that matches expected patterns
        for (Constructor<?> constructor : constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            
            // Check if this constructor works with instruction patterns
            if (isValidInstructionConstructor(paramTypes)) {
                return constructor;
            }
        }
        
        return null; // No suitable constructor found
    }
    
    /**
     * Check if the constructor parameter types match expected instruction patterns
     */
    private boolean isValidInstructionConstructor(Class<?>[] paramTypes) {
        // All instruction constructors should start with String (label)
        if (paramTypes.length < 2 || !paramTypes[0].equals(String.class)) {
            return false;
        }
        
        // Valid patterns:
        // (String, int) - out instruction
        // (String, int, int) - lin instruction  
        // (String, int, int, int) - add, sub, mul, div instructions
        // (String, int, String) - bnz instruction
        
        switch (paramTypes.length) {
            case 2:
                return paramTypes[1].equals(int.class); // out: (String, int)
            case 3:
                return (paramTypes[1].equals(int.class) && paramTypes[2].equals(int.class)) || // lin: (String, int, int)
                       (paramTypes[1].equals(int.class) && paramTypes[2].equals(String.class)); // bnz: (String, int, String)
            case 4:
                return paramTypes[1].equals(int.class) && paramTypes[2].equals(int.class) && 
                       paramTypes[3].equals(int.class); // add,sub,mul,div: (String, int, int, int)
            default:
                return false;
        }
    }

    private Object[] argsForConstructor(Constructor<?> cons, String label) {
        Class<?>[] paramTypes = cons.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        
        // First parameter is always the label
        args[0] = label;
        
        // Based on the constructor pattern
        switch (paramTypes.length) {
            case 2: // out: (String, int)
                args[1] = scanInt();
                break;
                
            case 3:
                if (paramTypes[2].equals(String.class)) {
                    // bnz: (String, int, String)
                    args[1] = scanInt();
                    args[2] = scan();
                } else {
                    // lin: (String, int, int)
                    args[1] = scanInt();
                    args[2] = scanInt();
                }
                break;
                
            case 4: // add, sub, mul, div: (String, int, int, int)
                args[1] = scanInt();
                args[2] = scanInt();
                args[3] = scanInt();
                break;
                
            default:
                log.severe("Unsupported constructor pattern with " + paramTypes.length + " parameters");
                return null;
        }
        
        // Validate that all integer arguments were successfully parsed
        for (int i = 1; i < args.length; i++) {
            if (args[i] instanceof Integer && ((Integer) args[i]).equals(Integer.MAX_VALUE)) {
                log.severe("Failed to parse integer argument at position " + i);
                return null;
            }
        }
        
        return args;
    }
}
